package com.meiliwan.emall.base.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.base.dto.IPLocation;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * 短信管理 短信推送
 * @author lzl
 */
public class BaseIPParseClient {
	
	/**
	 * 
	 * @param resultObj
	 * @param ip
	 */
	public static IPLocation getIPLocation(String ip){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseIPParseService/getIPLocation", ip));
		
        return new Gson().fromJson(obj.get("resultObj"), IPLocation.class);
	}
	
}
