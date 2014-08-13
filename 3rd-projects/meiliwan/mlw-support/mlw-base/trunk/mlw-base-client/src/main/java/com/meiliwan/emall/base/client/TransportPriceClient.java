package com.meiliwan.emall.base.client;

import java.math.BigDecimal;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseTransportPrice;
import com.meiliwan.emall.base.dto.TransportAreaAndCOD;
import com.meiliwan.emall.base.dto.TransportPriceDTO;
import com.meiliwan.emall.base.dto.TransportPriceTip;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;

public class TransportPriceClient {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(TransportPriceClient.class);
	
	private static String method(String method) {
		String service = String.format("baseTransportPriceService/%s", method);
		return service;
	}

	public static List<TransportPriceDTO> getListByParentCode(String parentCode){
			JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
					JSONTool.buildParams(method("getListAreaFirstByParentCode"),parentCode));
			
			 return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<TransportPriceDTO>>() {
		        }.getType());
			 
		
	}
	
	public static PagerControl<TransportPriceTip> page(PageInfo pageInfo) {
		try {
			JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
					JSONTool.buildParams(method("page"),pageInfo));
			
			return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<TransportPriceTip>>() {
			}.getType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, pageInfo, null);
		}
		return new PagerControl<TransportPriceTip>();
	}

	public static void delAddPrice(String[] areaCodes, BaseTransportPrice bean) {
		// TODO Auto-generated method stub
		 try {
			IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
					JSONTool.buildParams(method("delAddPrice"),areaCodes,bean));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, areaCodes+bean.toString(), null);
		}
	}
	
	public static BigDecimal getPrice(String countryCode,double orderSaleAmout) {
		// TODO Auto-generated method stub
		BigDecimal price = null;
		try {
			JsonObject obj =  IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
					JSONTool.buildParams(method("getTransportPrice"),countryCode,orderSaleAmout));
			price = obj.get(BaseService.RESULT_OBJ).getAsBigDecimal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, countryCode+orderSaleAmout, null);
		}
		if(price==null){
			price = new BigDecimal(-1);
		}
		return price;
	}
	
	public static TransportPriceTip findUnionStationFareByAreaCode(String areaCode) {
		// TODO Auto-generated method stub
		try {
			JsonObject obj =  IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
					JSONTool.buildParams(method("findUnionStationFareByAreaCode"),areaCode));
			TransportPriceTip tip = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<TransportPriceTip>() {
			}.getType());
			if(tip==null||tip.getAreaCode()==null){
				return null;
			}
			return tip;
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			logger.error(e, areaCode, null);
		}
		return null;
	}

	/**
	 * 配送区域和货到付款
	 * @param parentCode
	 * @return
	 */
	public static List<TransportAreaAndCOD> getTransportAreaAndCODByPid(
			String parentCode) {
		// TODO Auto-generated method stub
		JsonObject obj =  IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(method("getTransportAreaAndCODByPid"),parentCode));
		return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<TransportAreaAndCOD>>() {
		}.getType());
		
	}

	public static void del(String areaCode) {
		// TODO Auto-generated method stub
		IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
				JSONTool.buildParams(method("del"),areaCode));
	}
	
	
}
