package cj.netos.microapp.plugin.is;

import java.io.FileNotFoundException;
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
import cj.netos.microapp.args.MicroApp;
import cj.netos.microapp.args.MicroAppVersion;
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
	public List<UpdateCommand> checkMicroAppVersions(List<MicroAppVersion> versions) {
		
		return null;
	}

	@Override
	public MicroApp getMicroApp(String mcroappname) throws CircuitException {
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
			String vappFile = String.format("%sversions/%s.yaml", appPath,version);
			fi = fs.openFile(vappFile, OpenMode.onlyOpen);
			reader = fi.reader(0);
			b = reader.readFully();
			reader.close();
			yaml = new Yaml();
			MicroApp app = yaml.loadAs(new String(b),MicroApp.class);
			return app;
		} catch (FileNotFoundException e) {
			throw new CircuitException("404", e);
		} catch (FileLockException | TooLongException e) {
			throw new CircuitException("500", e);
		}
	}

}
