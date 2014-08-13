package com.meiliwan.emall.base.interp;

import Ice.Current;
import com.google.gson.JsonArray;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.service.SecurityInterceptor;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-11-13
 * Time: 上午9:58
 * To change this template use File | Settings | File Templates.
 */
@Service("baseMailAndMobileService/sendMobile")
public class BaseMessageSendInterp implements SecurityInterceptor {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());


    @Override
    public boolean allowAccess(JsonArray jsonArr, Current __current) {
        /*String[] ips = IceConnParser.parseIPs(__current);
        if(ips==null || ips.length==0){
            logger.error(null, "获取不到ICE clientIp", "");
            return false;
        }
        String clientIP = ips[0];
        if(StringUtils.isBlank(clientIP)){
            logger.error(null,"获取不到ICE clientIp","");
            return false;
        }
        String ck = null;
        try {
            ck = ConfigOnZk.getInstance().getValue(ZkPath.MSG_WHITELIST,clientIP);
        } catch (BaseException e) {
            logger.error(e,"ConfigOnZk.getInstance().getValue(ZkPath.MSG_WHITELIST,clientIP): {ZkPath.MSG_WHITELIST:"+ZkPath.MSG_WHITELIST+",clientIP:"+clientIP+"}",clientIP);
            return false;
        }
        return "true".equals(ck);*/
        return true;
    }
}
