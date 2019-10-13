package cj.netos.appsite.program;

import cj.netos.uc.port.IAuthPort;
import cj.studio.ecm.IServiceSite;
import cj.studio.openport.CheckTokenException;
import cj.studio.openport.ICheckTokenStrategy;
import cj.studio.openport.TokenInfo;
import cj.studio.openport.annotations.CjOpenport;

import java.util.List;
import java.util.Map;

public class CheckTokenStrategy implements ICheckTokenStrategy {
    IAuthPort port;
    String appid;//这是uc分发的应用id，与应用是两个概含，应用是gbera应用中抽象的逻辑应用概念，并且将名为gbera的应用视作官方应用

    @Override
    public void init(IServiceSite site) {
        port = (IAuthPort) site.getService("$openports.cj.netos.uc.port.IAuthPort");
        appid = (String) site.getProperty("uc.appid");
    }

    @Override
    public TokenInfo checkToken(String portsurl, String methodName, CjOpenport openport, String token) throws CheckTokenException {
        Map<String, Object> claims = null;
        try {
            claims = port.verification(appid, token);
        } catch (Exception e) {
            throw new CheckTokenException("801", e);
        }
        TokenInfo info = new TokenInfo();
        info.setUser((String) claims.get("sub"));
        List<Map<String, String>> appRoles = (List<Map<String, String>>) claims.get("app-roles");
        for (Map<String, String> objRole : appRoles) {
            if (!appid.equals(objRole.get("appId"))) {
                continue;
            }
            String roleid = objRole.get("roleId");
            info.getRoles().add(roleid);
        }
        info.getProps().put("accountid", claims.get("accountid"));
        info.getProps().put("accountName", claims.get("accountName"));
        info.getProps().put("appid", claims.get("appid"));
        info.getProps().put("tenantid", claims.get("iss"));
        info.getProps().put("issuetime", claims.get("iat"));
        return info;
    }
}
