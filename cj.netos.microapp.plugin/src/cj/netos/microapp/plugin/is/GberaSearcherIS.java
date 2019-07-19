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
import cj.netos.microapp.args.MicroPortal;
import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.Microdisplay;
import cj.netos.microapp.args.Microstyle;
import cj.netos.microapp.args.UpdateCommand;
import cj.netos.microapp.is.IGberaSearcherIS;
import cj.netos.microapp.plugin.util.DBUtils;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.util.StringUtil;

@CjService(name = "gberaSearcherIS")
public class GberaSearcherIS implements IGberaSearcherIS {
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
			app.setVersion(version.substring("v-".length(), version.length()));
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

	@Override
	public MicroPortal getMicroPortalInfo(String microportal) throws CircuitException {
		int pos = microportal.lastIndexOf("/");
		if (pos < 0) {
			throw new CircuitException("404", "缺少版本，格式：myportal/1.x");
		}
		String name = microportal.substring(0, pos);
		String remaining = microportal.substring(pos + 1, microportal.length());

		String selectStyle = "";
		String version = microportal.substring(pos + 1, microportal.length());
		pos = remaining.lastIndexOf("@");
		if (pos < 0) {
			version = remaining;
		} else {
			version = remaining.substring(0, pos);
			selectStyle = remaining.substring(pos + 1, remaining.length());
		}
		String portalPath = String.format("/portals/%s/", name);
		FileSystem fs = dbutils.fs();
		DirectoryInfo dir = fs.dir(portalPath);
		if (!dir.exists()) {
			throw new CircuitException("404", "portal不存在:" + microportal);
		}
		String fn = String.format("%smicroportal.yaml", portalPath);
		try {
			FileInfo fi = fs.openFile(fn, OpenMode.onlyOpen);
			IReader reader = fi.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			MicroPortal portalInfo = yaml.loadAs(new String(b), MicroPortal.class);
			portalInfo.setVersion(version);

			if (StringUtil.isEmpty(selectStyle)) {
				loadDefaultStyle(fs, portalPath, portalInfo);
			} else {
				portalInfo.setUseStyle(selectStyle);
			}
			loadStyles(fs, portalPath, portalInfo);
			loadDisplays(fs, portalPath, portalInfo);
			return portalInfo;
		} catch (FileNotFoundException e) {
			throw new CircuitException("404", e);
		} catch (FileLockException | TooLongException e) {
			throw new CircuitException("500", e);
		}
	}

	private void loadDisplays(FileSystem fs, String portalPath, MicroPortal portalInfo)
			throws FileLockException, TooLongException {
		String stylesHome = String.format("%sversions/v-%s/displays/", portalPath, portalInfo.getVersion());
		DirectoryInfo dir = fs.dir(stylesHome);
		List<FileInfo> files = dir.listFiles();
		for (FileInfo f : files) {
			if (!f.name().endsWith(".yaml")) {
				continue;
			}
			IReader reader = f.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			Microdisplay display = yaml.loadAs(new String(b), Microdisplay.class);
			String key = f.name();
			int pos = key.lastIndexOf(".yaml");
			key = key.substring(0, pos);
			portalInfo.getDisplays().put(key, display);
			reader.close();
		}
	}

	private void loadStyles(FileSystem fs, String portalPath, MicroPortal portalInfo)
			throws FileNotFoundException, FileLockException, TooLongException {
		String stylesHome = String.format("%sversions/v-%s/styles/", portalPath, portalInfo.getVersion());
		DirectoryInfo dir = fs.dir(stylesHome);
		List<FileInfo> files = dir.listFiles();
		for (FileInfo f : files) {
			if (!f.name().endsWith(".yaml")) {
				continue;
			}
			IReader reader = f.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			Microstyle style = yaml.loadAs(new String(b), Microstyle.class);
			String key = f.name();
			int pos = key.lastIndexOf(".yaml");
			key = key.substring(0, pos);
			portalInfo.getStyles().put(key, style);
			reader.close();
		}
	}

	private void loadDefaultStyle(FileSystem fs, String portalPath, MicroPortal portalInfo)
			throws FileNotFoundException, FileLockException, TooLongException {
		String fn = String.format("%sversions/v-%s/default_style.yaml", portalPath, portalInfo.getVersion());
		FileInfo fi = fs.openFile(fn, OpenMode.onlyOpen);
		IReader reader = fi.reader(0);
		byte[] b = reader.readFully();
		reader.close();
		Yaml yaml = new Yaml();
		Map<String, String> map = yaml.load(new String(b));
		portalInfo.setUseStyle(map.get("default"));
		reader.close();
	}
}
