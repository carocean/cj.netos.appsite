package cj.netos.appsite.plugin.surface;

import cj.lns.chip.sos.cube.framework.*;
import cj.lns.chip.sos.cube.framework.lock.FileLockException;
import cj.netos.appsite.model.*;
import cj.netos.appsite.plugin.util.DBUtils;
import cj.netos.appsite.surface.IAppsiteVersionSurface;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.ultimate.util.StringUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.util.*;

@CjService(name = "appsiteVersionSurface")
public class AppsiteVersionSurface implements IAppsiteVersionSurface {
    @CjServiceRef
    DBUtils dbutils;

    @Override
    public String getNewestAppVersion(String appid) throws CircuitException {
        List<String> list = listAppVersion(appid);
        if (list.isEmpty()) return null;
        return list.get(0);
    }


    @Override
    public List<String> listAppVersion(String appid) {
        String appversionsPath = String.format("/%s/", appid);
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
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        return list;
    }

    @Override
    public Appsite getAppsite(String appid, String version) throws CircuitException {
        String appPath = String.format("/%s", appid);
        FileSystem fs = dbutils.fs();
        DirectoryInfo dir = fs.dir(appPath);
        if (!dir.exists()) {
            throw new CircuitException("404", "app不存在:" + appid);
        }
        try {
            String vappFile = String.format("%s/v-%s.yaml", appPath, version);
            FileInfo fi = fs.openFile(vappFile, OpenMode.onlyOpen);
            IReader reader = fi.reader(0);
            byte[] b = reader.readFully();
            reader.close();
            Yaml yaml = new Yaml();
            Appsite app = yaml.loadAs(new String(b), Appsite.class);
            return app;
        } catch (FileNotFoundException e) {
            throw new CircuitException("404", e);
        } catch (FileLockException | TooLongException e) {
            throw new CircuitException("500", e);
        }
    }

    @Override
    public Portal getPortal(String appid,String portalid) throws CircuitException {
		String portalPath = String.format("/%s/portals/%s/", appid,portalid);
		FileSystem fs = dbutils.fs();
		DirectoryInfo dir = fs.dir(portalPath);
		if (!dir.exists()) {
			throw new CircuitException("404",String.format("应用:%s 下不存在框架:%s",appid,portalid));
		}
		String fn = String.format("%sdescribe.yaml", portalPath);
		try {
			FileInfo fi = fs.openFile(fn, OpenMode.onlyOpen);
			IReader reader = fi.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			Portal portal = yaml.loadAs(new String(b), Portal.class);
            portal.setId(dir.dirName());
			if (StringUtil.isEmpty(portal.getDefaultStyle())) {
				throw new CircuitException("404",String.format("应用:%s 下的框架：%s 未设定应用默认样式",appid,portalid));
			}
			loadStyles(fs, portalPath, portal);
			loadDisplays(fs, portalPath, portal);
			return portal;
		} catch (FileNotFoundException e) {
			throw new CircuitException("404", e);
		} catch (FileLockException | TooLongException e) {
			throw new CircuitException("500", e);
		}
    }

	private void loadDisplays(FileSystem fs, String portalPath, Portal portalInfo)
			throws FileLockException, TooLongException {
        String displaysHome = String.format("%s/displays/", portalPath);
		DirectoryInfo dir = fs.dir(displaysHome);
		List<FileInfo> files = dir.listFiles();
		for (FileInfo f : files) {
			if (!f.name().endsWith(".yaml")) {
				continue;
			}
			IReader reader = f.reader(0);
			byte[] b = reader.readFully();
			reader.close();
			Yaml yaml = new Yaml();
			Display display = yaml.loadAs(new String(b), Display.class);
			String key = f.name();
			int pos = key.lastIndexOf(".yaml");
			key = key.substring(0, pos);
			display.setId(key);
			portalInfo.getDisplays().put(key, display);
			reader.close();
		}
	}

	private void loadStyles(FileSystem fs, String portalPath, Portal portalInfo)
			throws FileNotFoundException, FileLockException, TooLongException {
		String stylesHome = String.format("%s/styles/", portalPath);
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
			Style style = yaml.loadAs(new String(b), Style.class);
			String key = f.name();
			int pos = key.lastIndexOf(".yaml");
			key = key.substring(0, pos);
			style.setId(key);
			portalInfo.getStyles().put(key, style);
			reader.close();
		}
	}
}
