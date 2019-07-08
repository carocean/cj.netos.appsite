package cj.netos.microapp.program.stub;

import java.util.List;

import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.netos.microapp.is.IGberaUpdateIS;
import cj.netos.microapp.stub.IGberaUpdateManager;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;

@CjService(name = "/updateManager.service")
public class GberaUpdateManager extends GatewayAppSiteRestStub implements IGberaUpdateManager {
	@CjServiceRef(refByName = "MicroappPlugin.gberaUpdateIS")
	IGberaUpdateIS gberaUpdateIS;

	@Override
	public List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions) throws CircuitException{
		return gberaUpdateIS.checkMicroAppVersions(versions);
	}

	@Override
	public Microapp getMicroApp(String mcroappname)throws CircuitException {
		return gberaUpdateIS.getMicroApp(mcroappname);
	}
	@Override
	public List<String> listMicroAppVersion(String appnameWithoutVersion) {
		return gberaUpdateIS.listMicroAppVersion(appnameWithoutVersion);
	}
}
