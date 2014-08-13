package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;

public class MonitorLogTest extends MonitorBaseTest{

	@Autowired
	private MLWLogService mLWLogService;
	
	private JsonObject jsonObject = new JsonObject();
	
	@Test
	public void getAllApplication(){
		mLWLogService.getAllApplication();
		System.out.println(jsonObject);
	}
}
