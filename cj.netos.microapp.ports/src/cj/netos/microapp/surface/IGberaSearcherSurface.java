package cj.netos.microapp.surface;

import java.util.List;

import cj.netos.microapp.args.MicroPortal;
import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.studio.ecm.net.CircuitException;

public interface IGberaSearcherSurface {

	List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions)throws CircuitException;

	Microapp getMicroApp(String mcroappname) throws CircuitException;

	List<String> listMicroAppVersion(String appnameWithoutVersion)throws CircuitException;

	MicroPortal getMicroPortalInfo(String microportal)throws CircuitException;

}
