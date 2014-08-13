package com.meiliwan.emall.base.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;

public class StationFareClient {

	private static String method(String method) {
		String service = String.format("baseStationFareService/%s", method);
		return service;
	}

	public static BaseStationFare getStationFare() throws ServiceException{
		JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(method("findById")));
		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), BaseStationFare.class);
	}

	public static void update(BaseStationFare fare) throws ServiceException{
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(method("update"),fare));
	}
}
