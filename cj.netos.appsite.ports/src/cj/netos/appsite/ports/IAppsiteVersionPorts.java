package cj.netos.appsite.ports;

import cj.netos.appsite.model.Appsite;
import cj.netos.appsite.model.Portal;
import cj.studio.ecm.net.CircuitException;
import cj.studio.openport.IOpenportService;
import cj.studio.openport.annotations.CjOpenport;
import cj.studio.openport.annotations.CjOpenportParameter;
import cj.studio.openport.annotations.CjOpenports;

import java.util.List;

@CjOpenports(usage = "应用版本开放api")
public interface IAppsiteVersionPorts extends IOpenportService {

    @CjOpenport(usage = "列出指定应用的所有版本号")
    List<String> listAppVersion(@CjOpenportParameter(usage = "应用标识", name = "appid") String appid) throws CircuitException;

    @CjOpenport(usage = "获取指定应用的最新可用版本")
    String getNewestAppVersion(@CjOpenportParameter(usage = "应用标识", name = "appid") String appid) throws CircuitException;

    @CjOpenport(usage = "获取一个应用", simpleModelFile = "/models/app.json")
    Appsite getAppsite(@CjOpenportParameter(usage = "应用标识", name = "appid") String appid,
                       @CjOpenportParameter(usage = "版本号", name = "version") String version) throws CircuitException;

    @CjOpenport(usage = "获取指定应用下的框架", simpleModelFile = "/models/portal.json")
    Portal getPortal(@CjOpenportParameter(usage = "应用标识", name = "appid") String appid,
                     @CjOpenportParameter(usage = "框架标识", name = "portalid") String portalid) throws CircuitException;
}
