package cj.netos.microapp.is;

import java.util.List;

import cj.netos.microapp.args.MicroApp;
import cj.netos.microapp.args.MicroAppVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.studio.ecm.net.CircuitException;

public interface IGberaUpdateIS {

	List<UpdateCommand> checkMicroAppVersions(List<MicroAppVersion> versions)throws CircuitException;

	MicroApp getMicroApp(String mcroappname) throws CircuitException;

}
