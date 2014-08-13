package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserForeign;

 

/**
 * 用户user_extra模块client
 * @author jiawu.wu
 *
 */
public class UserForeignClient  {
	
	private static final String SERVICE_NAME = "userForeignService";

	/**
	 * 保存合作网站用户信息
	 * @param user
	 * @return
	 */
	public static boolean save(UserForeign user){
		String iceFuncName="insert";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, user));
		return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
	}
	
	/**
	 * 更新合作网站用户信息
	 * @param user
	 * @return
	 */
	public static boolean update(UserForeign user){
		String iceFuncName="update";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, user));
		return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
	}
	
	/**
	 * 
	 * @param newUid
	 * @param oldUid
	 * @param source
	 * @return
	 */
	public static boolean update2NewUid(int newUid, int oldUid, String source){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/update2NewUid", newUid, oldUid, source));
		return new Gson().fromJson(obj.get("resultObj"), Boolean.class);
	}
	
	/**
	 * 获取合作网站用户信息
	 * @param uid
	 * @param source
	 * @return
	 */
	public static UserForeign getForeignByUid(Integer uid, String source){
		String iceFuncName="getForeignByUid";
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/"+iceFuncName, uid));
		return new Gson().fromJson(obj.get("resultObj"), UserForeign.class);
	}
	
	/**
	 * 
	 * @param fid
	 * @param source
	 * @return 根据第三方来源（比如qq、sina）和第三方用户的uid查出mlw中uid等信息
	 */
	public static UserForeign getForeignByFid(String fid, String source){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE, JSONTool.buildParams(SERVICE_NAME+"/getForeignByFid", fid, source));
		return new Gson().fromJson(obj.get("resultObj"), UserForeign.class);
	}
	
}
