package cj.netos.microapp.stub;

import java.util.List;

import cj.netos.microapp.args.MicroApp;
import cj.netos.microapp.args.MicroAppVersion;
import cj.netos.microapp.args.UpdateCommand;
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
			@CjStubInContentKey(key = "versions", usage = "移动端缓冲的版本集合") List<MicroAppVersion> versions);

	@CjStubMethod(usage = "获取一个微应用")
	MicroApp getMicroApp(@CjStubInParameter(key = "mcroappname", usage = "微应用名") String mcroappname);
}
