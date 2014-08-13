package com.meiliwan.emall.sp2.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.LotterySetting;

public class LotterySettingClient {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(LotterySettingClient.class);
	
	private static String getMethod(String method){
		return String.format("lotterySettingService/%s", method);
	}
	public static List<LotterySetting> allList() throws ServiceException{
		 
		 JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
               JSONTool.buildParams(getMethod("allList")));
		 return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<LotterySetting>>() {
	        }.getType());
       
	}
	
	public static LotterySetting findById(int id) {
		 try {
			JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
			      JSONTool.buildParams(getMethod("findById"),id));
			 return new Gson().fromJson(obj.get("resultObj"), LotterySetting.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
			return null;
		}
      
	}
	
	public static Map<String, Integer> allMap() throws ServiceException{
		 
		List<LotterySetting> list = allList();
		Map<String, Integer> keyChanceMap = new HashMap<String, Integer>();
		for (LotterySetting setting : list) {
			keyChanceMap.put(setting.getId()+"", setting.getPossibility());
		}
		return keyChanceMap;
	}
	
	
	public static void del(int id) throws ServiceException{
		
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("del"), id));
		
	}
	
	public static void insertUpdate(LotterySetting setting) throws ServiceException{
		
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("insertUpdate"), setting));
		
	}

	/**
	 * 更新前的值
	 * @param id
	 * @throws ServiceException
	 */
	public static int minusOneTotal(int id) throws ServiceException{
		
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("updateMinusOneTotal"),id));
		return new Gson().fromJson(obj.get("resultObj"), Integer.class);
	}
}
