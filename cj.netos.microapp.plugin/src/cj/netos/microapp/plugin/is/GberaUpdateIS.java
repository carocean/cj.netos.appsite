package cj.netos.microapp.plugin.is;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import cj.lns.chip.sos.cube.framework.DirectoryInfo;
import cj.lns.chip.sos.cube.framework.FileInfo;
import cj.lns.chip.sos.cube.framework.FileSystem;
import cj.lns.chip.sos.cube.framework.IReader;
import cj.lns.chip.sos.cube.framework.OpenMode;
import cj.lns.chip.sos.cube.framework.TooLongException;
import cj.lns.chip.sos.cube.framework.lock.FileLockException;
import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.netos.microapp.is.IGberaUpdateIS;
import cj.netos.microapp.plugin.util.DBUtils;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.util.StringUtil;

@CjService(name = "gberaUpdateIS")
public class GberaUpdateIS implements IGberaUpdateIS {
	@CjServiceRef
	DBUtils dbutils;

	@Override
	public List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions) throws CircuitException {
		// 先判断版本是否存在，次判断是否是最新应用版本(客户端每次仅缓冲在用的一个版本，不缓冲历史版本）
		List<UpdateCommand> dirty = new ArrayList<UpdateCommand>();
		for (MicroappVersion version : versions) {
			try {
				checkVersionAndFillIt(version, dirty);
			} catch (FileNotFoundException | FileLockException | TooLongException e) {
				throw new CircuitException("500", e);
			}
		}
		return dirty;
	}

	private void checkVersionAndFillIt(MicroappVersion appversion, List<UpdateCommand> dirty)
			throws FileNotFoundException, FileLockException, TooLongException {
		String mcroappname = appversion.getMicroappname();
		String version = appversion.getMicroappversion();
		String appPath = String.format("/apps/%s/", mcroappname);
		String fn = String.format("%smicroapp.yaml", appPath);
		FileSystem fs = dbutils.fs();
		DirectoryInfo dir = fs.dir(appPath);
		if (!dir.exists() || !fs.existsFile(fn)) {
			UpdateCommand up = new UpdateCommand();
			up.setCommand("DA");
			up.setMicroappname(mcroappname);
			up.setMicroappversion(version);
			dirty.add(up);
			return;
		}
		String vappFile = String.format("%sversions/v-%s.yaml", appPath, version);
		if (!fs.existsFile(vappFile)) {
			UpdateCommand up = new UpdateCommand();
			up.setCommand("D");
			up.setMicroappname(mcroappname);
			up.setMicroappversion(version);
			dirty.add(up);
			return;
		}
		FileInfo fi = fs.openFile(fn, OpenMode.onlyOpen);
		IReader reader = fi.reader(0);
		byte[] b = reader.readFully();
		reader.close();
		Yaml yaml = new Yaml();
		Map<String, Object> conf = yaml.load(new String(b));
		String applyVersion = conf.get("apply-version") + "";

		vappFile = String.format("%sversions/v-%s.yaml", appPath, applyVersion);
		if (!applyVersion.equals(version)) {
			if (fs.existsFile(vappFile)) {// 如果指定版本不同且存在该版本则为新建版本
				UpdateCommand up = new UpdateCommand();
				up.setCommand("N");
				up.setMicroappname(mcroappname);
				up.setMicroappversion(version);
				dirty.add(up);
				return;
			}
			// 没有这个应用则报删除
			UpdateCommand up = new UpdateCommand();
			up.setCommand("DA");
			up.setMicroappname(mcroappname);
			up.setMicroappversion(version);
			dirty.add(up);
			return;
		}
	}

	@Override
	public Microapp getMicroApp(String mcroappname) throws CircuitException {
		int pos = mcroappname.lastIndexOf("/");
		String name = "";
		String version = "";
		if (pos > -1) {
			name = mcroappname.substring(0, pos);
			version = mcroappname.substring(pos + 1, mcroappname.length());
		} else {
			name = mcroappname;
		}
		String appPath = String.format("/apps/%s/", name);
		FileSystem fs = dbutils.fs();
		DirectoryInfo dir = fs.dir(appPath);
		if (!dir.exists()) {
			throw new CircuitException("404", "app不存在:" + mcroappname);
		}
		String fn = String.format("%smicroapp.yaml", appPath);
		try {
			FileInfo fi = fs.openFile(fn, OpenMode.onlyOpen);
			IReader reader = fi.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			Map<String, Object> conf = yaml.load(new String(b));
			String applyVersion = conf.get("apply-version") + "";
			if (StringUtil.isEmpty(version)) {
				version = applyVersion;
			}
			if (!version.startsWith("v-")) {
				version = "v-" + version;
			}
			String vappFile = String.format("%sversions/%s.yaml", appPath, version);
			fi = fs.openFile(vappFile, OpenMode.onlyOpen);
			reader = fi.reader(0);
			b = reader.readFully();
			reader.close();
			yaml = new Yaml();
			Microapp app = yaml.loadAs(new String(b), Microapp.class);
			app.setName(name);
			app.setTitle(StringUtil.isEmpty(conf.get("title") + "") ? "" : conf.get("title") + "");
			app.setDesc(StringUtil.isEmpty(conf.get("desc") + "") ? "" : conf.get("desc") + "");
			app.setDeveloper(StringUtil.isEmpty(conf.get("developer") + "") ? "" : conf.get("developer") + "");
			app.setApplyVersion(
					StringUtil.isEmpty(conf.get("apply-version") + "") ? "" : conf.get("apply-version") + "");
			return app;
		} catch (FileNotFoundException e) {
			throw new CircuitException("404", e);
		} catch (FileLockException | TooLongException e) {
			throw new CircuitException("500", e);
		}
	}

	@Override
	public List<String> listMicroAppVersion(String appnameWithoutVersion) {
		String appversionsPath = String.format("/apps/%s/versions", appnameWithoutVersion);
		FileSystem fs = dbutils.fs();
		DirectoryInfo dir = fs.dir(appversionsPath);
		List<String> list = new ArrayList<String>();
		if (!dir.exists()) {
			return list;
		}
		List<String> childs = dir.listFileNames();
		for (String name : childs) {
			if (!name.endsWith(".yaml")) {
				continue;
			}
			name = name.substring(0, name.lastIndexOf(".yaml"));
			if (name.startsWith("v-")) {
				name = name.substring("v-".length(), name.length());
			}
			list.add(name);
		}
		return list;
	}
}
