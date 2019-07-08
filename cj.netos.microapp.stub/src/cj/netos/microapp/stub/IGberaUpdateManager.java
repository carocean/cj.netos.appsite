package cj.netos.microapp.stub;

import java.util.List;

import cj.netos.microapp.args.Microapp;
import cj.netos.microapp.args.MicroappVersion;
import cj.netos.microapp.args.UpdateCommand;
import cj.studio.ecm.net.CircuitException;
import cj.studio.gateway.stub.annotation.CjStubInContentKey;
import cj.studio.gateway.stub.annotation.CjStubInParameter;
import cj.studio.gateway.stub.annotation.CjStubMethod;
import cj.studio.gateway.stub.annotation.CjStubReturn;
import cj.studio.gateway.stub.annotation.CjStubService;

@CjStubService(bindService = "/updateManager.service", usage = "更新管理器，供移动端维持微应用的更新")
public interface IGberaUpdateManager {
	@CjStubMethod(usage = "检查微应用的版本，并返回更新指令，版本为最新的微应用则忽略", command = "post")
	@CjStubReturn(elementType = UpdateCommand.class, usage = "更新命令集合")
	List<UpdateCommand> checkMicroAppVersions(
			@CjStubInContentKey(key = "versions", usage = "移动端缓冲的版本集合") List<MicroappVersion> versions)
			throws CircuitException;

	@CjStubMethod(usage = "获取一个微应用")
	Microapp getMicroApp(
			@CjStubInParameter(key = "microappname", usage = "微应用名。如果没有指定版本默认取应用配置版本，如果指定版本号则取应用指定版。格式为：myappname/1.1") String microappname)
			throws CircuitException;

	@CjStubMethod(usage = "列出一个应用的所有版本")
	List<String> listMicroAppVersion(
			@CjStubInParameter(key = "appnameWithoutVersion", usage = "微应用名,不带版本号") String appnameWithoutVersion);
}
