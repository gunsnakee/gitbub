package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserExtra;


 

/**
 * 用户user_extra模块client
 * @author jiawu.wu
 *
 */
public class UserExtraClient  {
	
	private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(UserExtraClient.class);
	
	private static final String SERVICE_NAME = "userExtraService";
	private static final String RETURN_STR = "resultObj";

    private UserExtraClient(){

    }
	
	/**
	 * 保存用户信息
	 * @param user
	 * @return
	 */
	public static boolean save(UserExtra user){
		String iceFuncName="add";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, user));
		return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
	}
	
	/**
	 * 更新用户额外信息
	 * @param user
	 * @return
	 */
	public static boolean update(UserExtra user){
		String iceFuncName="update";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, user));
		return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
	}
	/**
	 * 获取用户额外信息
	 * @param uid
	 * @return
	 */
	public static UserExtra getExtraByUid(Integer uid){
		String iceFuncName="getExtraByUid";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, uid));
		return new Gson().fromJson(obj.get("resultObj"), UserExtra.class);
	}
	
	
	
}
