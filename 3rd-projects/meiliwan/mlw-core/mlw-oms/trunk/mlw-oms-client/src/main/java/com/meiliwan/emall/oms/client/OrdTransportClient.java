package com.meiliwan.emall.oms.client;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.oms.bean.OrdTransport;
import com.meiliwan.emall.service.BaseService;

public class OrdTransportClient {

	private static String getMethod(String method){
		return String.format("ordTransportService/%s", method);
	}
	
	public static void main(String[] args) {
		System.out.println(getMethod("getListByOrderId"));
	}
    public static List<OrdTransport> getListByOrderId(String orderId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.OMS_ICE_SERVICE,
                JSONTool.buildParams(getMethod("getListByOrderId"),orderId ));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<OrdTransport>>() {
        }.getType());
    }
}
