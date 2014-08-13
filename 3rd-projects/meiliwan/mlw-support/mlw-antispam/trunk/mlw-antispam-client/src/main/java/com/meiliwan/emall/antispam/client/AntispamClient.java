package com.meiliwan.emall.antispam.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.antispam.bean.AuditResult;
import com.meiliwan.emall.antispam.bean.ContentObject;
import com.meiliwan.emall.antispam.bean.ReloadResult;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

public class AntispamClient {
	
	/**
	 * 机器检查
	 * @param contentObject
	 * @return
	 */
	public static AuditResult checkContent(ContentObject contentObject){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("contentAuditServiceImpl/checkContent", contentObject));
		AuditResult result = null;
		
		if(returnObject != null){
			result = new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<AuditResult>(){}.getType());
			
		}
		return result;
	}
	
	/**
	 * 重载敏感词库
	 * @return
	 */
	public static boolean reload(){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("contentAuditServiceImpl/reload"));
		boolean result = false;
		
		if(returnObject != null){
			ReloadResult rr = new Gson().fromJson(returnObject.get("resultObj"), new TypeToken<ReloadResult>(){}.getType());
			if(ReloadResult.SUCC == rr){
				result = true;
			}
		}
		return result;
	}
}