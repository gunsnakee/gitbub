package com.meiliwan.emall.base.service;

import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.mlcs.core.ipdata.IPLocation;
import com.mlcs.core.ipdata.IPSeeker;
/**
 * 短信管理 短信推送
 * @author lzl
 */
@Service
public class BaseIPParseService extends DefaultBaseServiceImpl {
	
	/**
	 * 
	 * @param resultObj
	 * @param ip
	 */
	public void getIPLocation(JsonObject resultObj, String ip){
		IPLocation loc = IPSeeker.getInstance().getAddr(ip);
		JSONTool.addToResult(loc, resultObj);
	}
	
}
