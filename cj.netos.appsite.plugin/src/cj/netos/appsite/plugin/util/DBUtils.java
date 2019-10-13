package cj.netos.appsite.plugin.util;

import cj.lns.chip.sos.cube.framework.FileSystem;
import cj.lns.chip.sos.cube.framework.ICube;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;

@CjService(name = "dbutils")
public class DBUtils {
	@CjServiceRef(refByName = "mongodb.netos.appsites")
	ICube home;
	FileSystem fs;

	public FileSystem fs() {
		if (fs != null)
			return fs;
		fs = home.fileSystem();
		return fs;
	}
	public ICube home() {
		return home;
	}
}
