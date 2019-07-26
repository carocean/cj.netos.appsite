package cj.netos.microapp.program.ports;

import cj.netos.microapp.args.MicroPortal;
import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.netos.microapp.surface.IGberaSearcherSurface;
import cj.netos.microapp.ports.IGberaSearcherPorts;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;

import java.util.List;

@CjService(name = "/searcher.ports")
public class GberaSearcherPorts implements IGberaSearcherPorts {
	@CjServiceRef(refByName = "MicroappPlugin.gberaSearcherSurface")
	IGberaSearcherSurface gberaSearcherSurface;

	@Override
	public List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions) throws CircuitException{
		return gberaSearcherSurface.checkMicroAppVersions(versions);
	}

	@Override
	public Microapp getMicroAppInfo(String mcroappname)throws CircuitException {
		return gberaSearcherSurface.getMicroApp(mcroappname);
	}
	@Override
	public List<String> listMicroAppVersion(String appnameWithoutVersion) throws CircuitException{
		return gberaSearcherSurface.listMicroAppVersion(appnameWithoutVersion);
	}

	@Override
	public MicroPortal getMicroPortalInfo(String microportal) throws CircuitException {
		return gberaSearcherSurface.getMicroPortalInfo(microportal);
	}
}
