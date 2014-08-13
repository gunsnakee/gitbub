package com.meiliwan.emall.sp2.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.LotteryAddress;
import com.meiliwan.emall.sp2.bean.LotteryResult;

public class LotteryAddressClient {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(LotteryAddressClient.class);

	private static String getMethod(String method) {
		return String.format("lotteryAddressService/%s", method);
	}

	public static void insert(LotteryAddress addr) throws ServiceException {
		IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
				JSONTool.buildParams(getMethod("insert"), addr));
	}

	public static LotteryAddress getById(int id) {
		// TODO Auto-generated method stub
		try {
			JsonObject obj = null;
			obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
					JSONTool.buildParams(getMethod("getById"), id));
			return new Gson().fromJson(obj.get("resultObj"),
					LotteryAddress.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, null);
		}
		return null;
	}

}
