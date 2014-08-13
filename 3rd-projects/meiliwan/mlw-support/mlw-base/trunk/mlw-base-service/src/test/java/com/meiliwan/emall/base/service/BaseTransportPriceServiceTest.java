package com.meiliwan.emall.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.bean.BaseTransportPrice;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dto.TransportAreaDTO;
import com.meiliwan.emall.commons.PageInfo;

public class BaseTransportPriceServiceTest extends BaseTest{

	@Autowired
	BaseTransportPriceService baseTransportPriceService;
	private JsonObject resultObj=new JsonObject();
	
	@Test
	public void getListByParentCode(){
		
		baseTransportPriceService.findUnionStationFareByAreaCode(resultObj, "34343");
		System.out.println(resultObj);
	}
	
	@Test
	public void page(){
		
		baseTransportPriceService.page(resultObj, new PageInfo());
		System.out.println(resultObj);
	}
	@Test
	public void findUnionStationFareByAreaCode(){
		
		baseTransportPriceService.findUnionStationFareByAreaCode(resultObj, "1000110229");
		System.out.println(resultObj);
	}
	@Test
	public void getTransportPrice(){
		
		baseTransportPriceService.getTransportPrice(resultObj, "1000110112", 100);
		System.out.println(resultObj);
	}
	
	@Test
	public void delAddPrice(){
		String[] areaCodes = new String[]{
				"111","222","333"
		};
		
		BaseTransportPrice bean = new BaseTransportPrice();
		bean.setType("full");
		baseTransportPriceService.delAddPrice(resultObj, areaCodes, bean);
		
	}
	
	@Test
	public void getTransportAreaAndCODByPid() {
		JsonObject resultObj = new JsonObject();
		TransportAreaDTO dto = new TransportAreaDTO();
		dto.setParentCode("1000");
		dto.setTransCompanyId(Constants.DEFAULT_TRANS_ID);
		baseTransportPriceService.getTransportAreaAndCODByPid(resultObj, "1000");
		System.out.println(resultObj);
	}
	@Test
	public void del() {
		baseTransportPriceService.del(resultObj, "10001310");
		System.out.println(resultObj);
	}
	
	
}
