package com.meiliwan.emall.base.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.base.dto.MailEntity;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 验证码发送
 * @author jiawu.wu
 *
 */
public class BaseMailAndMobileClient {

//	private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(BaseMailAndMobileClient.class);
	private static final String SERVICE_NAME = "baseMailAndMobileService";

	//短信成功发送后的返回码
	public static final int MOBILE_SUCESS_CODE = 605;

    private BaseMailAndMobileClient(){

    }


    /**
     * 通用邮件发送
     * @param mailEntity  邮件参数
     * @param cacheMap 缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey   缓存Key
     * @throws com.meiliwan.emall.commons.exception.JedisClientException
     */
    public static boolean sendMail(MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
       if(mailEntity==null || StringUtils.isBlank(mailEntity.getSender()) || StringUtils.isBlank(mailEntity.getReceivers()) || StringUtils.isBlank(mailEntity.getTitle())){
           return false;
       }
        String iceFuncName="sendMail";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, mailEntity,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }
    
    /**
     * @附件邮件发送
     * @param mailEntity  邮件参数
     * @param cacheMap 缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey   缓存Key
     * @param delAfterHandle 发送邮件后是否删除附件
     * @param filePath  发送的附件文件
     * */
    public static boolean sendMail(MailEntity mailEntity,Map<String,Object> cacheMap,
    		String cacheId,JedisKey jedisKey,boolean delAfterHandle,String... files){
    	
    	if (files == null || mailEntity == null) {
			return false;
		}
    	
    	String iceFuncName ="sendMailWithAttachment";
    	JsonObject jsonObject = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME + "/" + iceFuncName,
    			mailEntity,cacheMap,cacheId,jedisKey), delAfterHandle, files);
    	return new Gson().fromJson(jsonObject.get("resultObj"), Boolean.class);
    }
    /**
     * 通用邮件发送
     * @param mailEntity  邮件参数
     * @throws com.meiliwan.emall.commons.exception.JedisClientException
     */
    public static boolean sendMail(MailEntity mailEntity){
        return sendMail(mailEntity,null,null,null);
    }
    
    /**
     * 
     * @param to 目标邮件地址
     * @param fieldValue 用于填充模板的参数键值对
     * @param emailId 在第三方邮件平台中的模板ID
     * @return 发送成功返回true；发送失败返回false；
     */
    public static boolean sendMailByDM(String to,
			Map<String, Object> fieldValue, int emailId){
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/sendMailByDM",to, fieldValue, emailId));
    	
    	return new Gson().fromJson(obj.get("resultObj"), Boolean.class); 
    }
    
    /**
     * 
     * @param to
     * @param fieldValue
     * @param emailId 邮件id
     * @param actId 活动id
     * @param groupIds 组名
     * @return
     */
    public static boolean sendMailByDM(String to,
			Map<String, Object> fieldValue, int emailId, int actId, int[] groupIds){
    	JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/sendMailByDMWithGroupAndActId",to, fieldValue, emailId, actId, groupIds));
    	
    	return new Gson().fromJson(obj.get("resultObj"), Boolean.class); 
    }
    
    /**
     * 通用短信发送
     * @param mobileNum  手机号码
     * @param msgContent  短信内容
     * @param cacheMap  缓存数据
     * @param cacheId   缓存数据ID
     * @param jedisKey  缓存Key
     * @throws com.meiliwan.emall.commons.exception.BaseException
     */
    public static boolean sendMobile(String mobileNum,String msgContent,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        if( StringUtils.isBlank(mobileNum) || StringUtils.isBlank(msgContent)){
            return false;
        }
        String iceFuncName="sendMobile";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, mobileNum,msgContent,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }
    /**
     * 通用短信发送
     * @param mobileNum  手机号码
     * @param msgContent  短信内容
     * @throws com.meiliwan.emall.commons.exception.BaseException
     */
    public static boolean sendMobile(String mobileNum,String msgContent){
        return sendMobile(mobileNum, msgContent, null, null, null);
    }



    /**
     * 重置登录密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param mailEntity 邮件属性实体
     * @return
     */
    @Deprecated
    public static boolean sendMailResetPwd(Map<String,Object> dataMap,MailEntity mailEntity){
        String iceFuncName="sendMailResetPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, dataMap,mailEntity));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 找回登录密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param mailEntity 邮件属性实体
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    @Deprecated
    public static boolean sendMailFindPwd(Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailFindPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, dataMap,mailEntity,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 验证身份的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param mailEntity 邮件属性实体
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    @Deprecated
    public static boolean sendMailVI(Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailVI";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, dataMap,mailEntity,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }


    /**
     * 更改、设置、验证邮箱的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param mailEntity 邮件属性实体
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    @Deprecated
    public static boolean sendMailChange(Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailChange";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, dataMap,mailEntity,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }

    /**
     * 设置支付密码的邮件发送
     *
     * @param dataMap  ftl模版文件的数据
     * @param mailEntity 邮件属性实体
     * @param cacheMap 要设置到缓存的数据
     * @param cacheId  缓存ID
     * @param jedisKey 缓存使用的key
     * @return
     */
    @Deprecated
    public static boolean sendMailPayPwd(Map<String,Object> dataMap,MailEntity mailEntity,Map<String,Object> cacheMap,String cacheId,JedisKey jedisKey){
        String iceFuncName="sendMailPayPwd";
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, dataMap,mailEntity,cacheMap,cacheId,jedisKey));
        return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
    }




}
