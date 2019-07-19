package cj.netos.microapp.program.stub;

import java.util.List;

import cj.netos.microapp.args.MicroPortal;
import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.netos.microapp.is.IGberaSearcherIS;
import cj.netos.microapp.stub.IGberaSearcher;
import cj.studio.ecm.annotation.CjService;
import cj.studio.ecm.annotation.CjServiceRef;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.GatewayAppSiteRestStub;

@CjService(name = "/searcher.service")
public class GberaSearcher extends GatewayAppSiteRestStub implements IGberaSearcher {
	@CjServiceRef(refByName = "MicroappPlugin.gberaSearcherIS")
	IGberaSearcherIS gberaSearcherIS;

	@Override
	public List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions) throws CircuitException{
		return gberaSearcherIS.checkMicroAppVersions(versions);
	}

	@Override
	public Microapp getMicroAppInfo(String mcroappname)throws CircuitException {
		return gberaSearcherIS.getMicroApp(mcroappname);
	}
	@Override
	public List<String> listMicroAppVersion(String appnameWithoutVersion) throws CircuitException{
		return gberaSearcherIS.listMicroAppVersion(appnameWithoutVersion);
	}

	@Override
	public MicroPortal getMicroPortalInfo(String microportal) throws CircuitException {
		return gberaSearcherIS.getMicroPortalInfo(microportal);
	}
}
