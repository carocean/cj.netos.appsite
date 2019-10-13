package cj.netos.appsite.surface;

import java.util.List;

import cj.netos.appsite.model.Portal;
import cj.netos.appsite.model.Appsite;
import cj.studio.ecm.net.CircuitException;

public interface IAppsiteVersionSurface {

    List<String> listAppVersion(String appid) throws CircuitException;

    String getNewestAppVersion(String appid) throws CircuitException;

    Appsite getAppsite(String appid,String version) throws CircuitException;

    Portal getPortal(String appid,String portalid) throws CircuitException;

}
