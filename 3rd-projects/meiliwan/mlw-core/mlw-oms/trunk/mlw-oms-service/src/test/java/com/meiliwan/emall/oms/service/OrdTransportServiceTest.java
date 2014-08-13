package com.meiliwan.emall.oms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.oms.BaseTest;

public class OrdTransportServiceTest extends BaseTest{

	@Autowired
	private OrdTransportService ordTransportService;
	JsonObject resultObj = new JsonObject();
	
	@Test
	public void getList(){
		ordTransportService.getListByOrderId(resultObj, "1212131");
	}
}
