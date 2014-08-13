package com.meiliwan.emall.base.client;

import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;

/**
 * 
 * @author lsf
 *
 */
public class IdGenClient {

	public static String getPayId(){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams("idGenService/getPayId"));
		
		return obj.get(BaseService.RESULT_OBJ).getAsString();
	}
	
	public static String getCmbPayId(){
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams("idGenService/getCmbPayId"));
		
		return obj.get(BaseService.RESULT_OBJ).getAsString();
	}
	
}
