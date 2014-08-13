package com.meiliwan.emall.sp2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.sp2.base.BaseTest;

@Service
public class LotterySettingServiceTest extends BaseTest{

	@Autowired
	private LotterySettingService lotterySettingService;
	JsonObject resultObj= new JsonObject();
	@Test
	public void aad(){
		lotterySettingService.allList(resultObj);
		System.out.println(resultObj);
	}
	
	
}
