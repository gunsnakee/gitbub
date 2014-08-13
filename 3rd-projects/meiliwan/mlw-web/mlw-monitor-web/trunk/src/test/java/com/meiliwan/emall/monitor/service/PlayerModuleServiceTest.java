package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.allocation.PlayerModule;
import com.meiliwan.emall.monitor.allocation.PlayerModuleDTO;
import com.meiliwan.emall.monitor.allocation.PlayerModuleKey;

public class PlayerModuleServiceTest extends MonitorBaseTest{

	@Autowired
	private PlayerModuleService playerModuleService;
	
	private JsonObject resultObj = new JsonObject();
	
	@Test
	public void add(){
		PlayerModule playerModule = new PlayerModule();
		playerModule.setPid(1);
		playerModule.setModuleName("OrderController");
		playerModuleService.add( playerModule);
		System.out.println(resultObj);
	}
	
	@Test
	public void delete(){
		PlayerModuleKey playerModuleKey = new PlayerModuleKey();
		playerModuleKey.setPid(1);
		playerModuleKey.setModuleName("OrderController");
		playerModuleService.delete( playerModuleKey);
		System.out.println(resultObj);
	}
	
	@Test
	public void getByPlayerId(){
		playerModuleService.getByPlayerId( 1);
		System.out.println(resultObj);
	}
	
	@Test
	public void getAllPlayerModule(){
		List<PlayerModuleDTO> list = playerModuleService.getAllPlayerModule();
		System.out.println(list);
	}
	
	
	
}
