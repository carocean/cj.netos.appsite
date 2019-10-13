package cj.netos.appsite.program.ports;

import cj.netos.appsite.model.Portal;
import cj.netos.appsite.model.Appsite;
import cj.netos.appsite.surface.IAppsiteVersionSurface;
import cj.netos.appsite.ports.IAppsiteVersionPorts;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;

import java.util.List;

@CjService(name = "/version.ports")
public class AppsiteVersionPorts implements IAppsiteVersionPorts {
	@CjServiceRef(refByName = "AppsitePlugin.appsiteVersionSurface")
	IAppsiteVersionSurface appsiteVersionSurface;

	@Override
	public List<String> listAppVersion(String appid) throws CircuitException {
		return appsiteVersionSurface.listAppVersion(appid);
	}

	@Override
	public String getNewestAppVersion(String appid) throws CircuitException {
		return appsiteVersionSurface.getNewestAppVersion(appid);
	}

	@Override
	public Appsite getAppsite(String appid,String version) throws CircuitException {
		return appsiteVersionSurface.getAppsite(appid,version);
	}

	@Override
	public Portal getPortal(String portal, String version) throws CircuitException {
		return appsiteVersionSurface.getPortal(portal,version);
	}
}
