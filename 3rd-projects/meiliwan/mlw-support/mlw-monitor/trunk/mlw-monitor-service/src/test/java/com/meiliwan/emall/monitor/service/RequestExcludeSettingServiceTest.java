package com.meiliwan.emall.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.RequestExcludeSetting;


public class RequestExcludeSettingServiceTest extends MonitorBaseTest{
	
	@Autowired
	private RequestExcludeSettingService requestExcludeSettingService;
	
	private JsonObject resultObj = new JsonObject();
	
	@Test
	public void getRequestSettingById(){
		requestExcludeSettingService.getRequestSettingById(resultObj, 1);
		System.out.println(resultObj);
	}
	
	@Test(expectedExceptions = ServiceException.class)  
	public void addException(){
		RequestExcludeSetting requestExcludeSetting = new  RequestExcludeSetting();
		requestExcludeSetting.setName("name");
		//requestExcludeSettingService.add(resultObj, requestExcludeSetting);
	}
	
	@Test  
	public void add(){
		RequestExcludeSetting requestExcludeSetting = new  RequestExcludeSetting();
		requestExcludeSetting.setName("name");
		requestExcludeSetting.setType("dao");
		requestExcludeSetting.setResumeTime(400);
		//requestExcludeSettingService.add(resultObj, requestExcludeSetting);
	}
	
	public void getPageRequestExcludeSetting(){
		//requestExcludeSettingService.getPageRequestExcludeSetting(resultObj, new PageInfo());
	}
	
	
	
}
