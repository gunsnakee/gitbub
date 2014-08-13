package com.meiliwan.emall.monitor.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.allocation.PlayerApp;

public class PlayerAppServiceTest extends MonitorBaseTest{

	@Autowired
	private PlayerAppService playerAppService;
	private JsonObject resultObj = new JsonObject();
	
	@Test
	public void getListByPlayerId(){
		playerAppService.getListByPlayerId( 1);
		System.out.println(resultObj);
	}
	
	@Test
	public void addOrUpdate(){
		
		String[] apps = new String[]{"112","asdf"};
		playerAppService.addOrUpdate(1,apps);
	}
	
	@Test
	public void getAllPlayerApp(){
		playerAppService.getAllPlayerApp();
		System.out.println(resultObj);
	}
	
}
