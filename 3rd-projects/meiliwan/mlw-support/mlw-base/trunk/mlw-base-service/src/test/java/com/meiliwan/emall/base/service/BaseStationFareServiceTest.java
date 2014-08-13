package com.meiliwan.emall.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.bean.BaseStationFare;

public class BaseStationFareServiceTest extends BaseTest {

	@Autowired
	private BaseStationFareService baseStationFareService;
	private JsonObject resultObj = new JsonObject();
	@Test
	public void update() {
		BaseStationFare fare = new BaseStationFare();
		fare.setType(BaseStationFare.fixed);
		baseStationFareService.update(new JsonObject(),fare);
	}

	@Test
	public void getPrice() {
		baseStationFareService.getPrice(resultObj, 98);
		System.out.println(resultObj);
	}
	
	@Test
	public void findById() {
		baseStationFareService.findById(resultObj);
		System.out.println(resultObj);
	}
	
}
