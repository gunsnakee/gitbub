package com.meiliwan.emall.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Player;

public class PlayerRequestServiceTest extends MonitorBaseTest {
	
	@Autowired
	private PlayerRequestService playerRequestService;
	
	JsonObject  resultObj = new JsonObject();
	
	public void print(){
		System.out.println(resultObj);
	}
	
	@Test
	public void getPagePlayerRequest(){
		playerRequestService.getPagePlayerRequest( new PageInfo());
		System.out.println(resultObj);
	}
	
	@Test
	public void add(){
		playerRequestService.add(1);
		System.out.println(resultObj);
	}
	
	@Test
	public void delete(){
		playerRequestService.delete(1);
	}
	
	@Test
	public void getAllPlayer(){
		playerRequestService.getAllPlayer();
		print();
	}
	
}
