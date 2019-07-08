package cj.netos.microapp.is;

import java.util.List;

import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.studio.ecm.net.CircuitException;

public interface IGberaUpdateIS {

	List<UpdateCommand> checkMicroAppVersions(List<MicroappVersion> versions)throws CircuitException;

	Microapp getMicroApp(String mcroappname) throws CircuitException;

	List<String> listMicroAppVersion(String appnameWithoutVersion);

}
