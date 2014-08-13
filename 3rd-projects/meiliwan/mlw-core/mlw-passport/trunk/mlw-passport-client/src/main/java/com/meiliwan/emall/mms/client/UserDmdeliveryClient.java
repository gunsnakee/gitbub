package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import java.util.Map;

/**
 * 验证码发送
 * @author jiawu.wu
 *
 */
public class UserDmdeliveryClient {

	private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserDmdeliveryClient.class);
    private static final String SERVICE_DMDELIVER = "userDmdeliveryService";


    private UserDmdeliveryClient(){

    }

    /**
     * 重置登录密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param to 收件人（只允许一个人）
     * @return
     */
    public static boolean sendMailResetPwd(Map<String,Object> dataMap,String to){
        String iceFuncName="sendMailResetPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_DMDELIVER+"/"+iceFuncName, dataMap,to));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 找回登录密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param to 收件人（只允许一个人）
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    public static boolean sendMailFindPwd(Map<String,Object> dataMap,String to,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailFindPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_DMDELIVER+"/"+iceFuncName, dataMap,to,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 验证身份的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param to 收件人（只允许一个人）
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    public static boolean sendMailVI(Map<String,Object> dataMap,String to,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailVI";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_DMDELIVER+"/"+iceFuncName, dataMap,to,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }


    /**
     * 更改、设置、验证邮箱的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param to 收件人（只允许一个人）
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    public static boolean sendMailChange(Map<String,Object> dataMap,String to,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailChange";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_DMDELIVER+"/"+iceFuncName, dataMap,to,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 设置支付密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param to 收件人（只允许一个人）
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    public static boolean sendMailPayPwd(Map<String,Object> dataMap,String to,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailPayPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_DMDELIVER+"/"+iceFuncName, dataMap,to,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }




}
