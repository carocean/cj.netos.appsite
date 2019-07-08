package cj.netos.microapp.program.stub;

import java.util.List;

import cj.netos.microapp.args.MicroApp;
import cj.netos.microapp.args.MicroAppVersion;
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
	public List<UpdateCommand> checkMicroAppVersions(List<MicroAppVersion> versions) throws CircuitException{
		return gberaUpdateIS.checkMicroAppVersions(versions);
	}

	@Override
	public MicroApp getMicroApp(String mcroappname)throws CircuitException {
		return gberaUpdateIS.getMicroApp(mcroappname);
	}

}
