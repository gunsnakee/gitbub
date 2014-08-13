package com.meiliwan.emall.sp2.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes.SOURCE;

public class LotteryUserTimesClient {

	private static String getMethod(String method) {
		return String.format("lotteryUserTimesService/%s", method);
	}

	public static void addTimes(int uid, int times, SOURCE source, String ip)
			throws ServiceException {
		LotteryUserTimes lut = new LotteryUserTimes();
		lut.setUid(uid);
		lut.setTimes(times);
		if (source != null) {
			lut.setSource(source.name());
		}
		lut.setIp(ip);
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("insertTimes"), lut));

	}

	public static void addOneTimes(int uid, SOURCE source, String ip)
			throws ServiceException {
		addTimes(uid, 1, source, ip);
	}

	/**
	 * 返回次数
	 * @param uid
	 * @return  大于等于0
	 * @throws ServiceException
	 */
	public static int getTimes(int uid)
			throws ServiceException {
		if(uid<=0){
			return 0;
		}
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("getTimes"), uid));
		return new Gson().fromJson(obj.get("resultObj"),Integer.class);

	}
	
	/**
	 * 
	 * @param uid
	 * @return  
	 * @throws ServiceException
	 */
	public static void minusTimes(int uid)
			throws ServiceException {
		
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("minusTimes"), uid));

	}
}
