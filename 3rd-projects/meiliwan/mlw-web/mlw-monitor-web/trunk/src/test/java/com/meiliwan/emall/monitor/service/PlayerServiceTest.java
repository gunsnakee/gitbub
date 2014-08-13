package com.meiliwan.emall.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Player;

public class PlayerServiceTest extends MonitorBaseTest {
	
	@Autowired
	private PlayerService playerService;
	
	JsonObject  resultObj = new JsonObject();
	
	public void print(){
		System.out.println(resultObj);
	}
	
	@Test(expectedExceptions = ServiceException.class)  
	public void addNameNull(){
		Player player = new Player();
		playerService.add( player);
		
	}
	
	@Test
	public void add(){
		Player player = new Player();
		player.setName("卓营高");
		playerService.add( player);
		
	}
	
	@Test(expectedExceptions = ServiceException.class)  
	public void updateIdNullNameNull(){
		Player player = new Player();
		playerService.update( player);
	}
	@Test
	public void update(){
		Player player = new Player();
		player.setPid(1);
		player.setName("test");

		playerService.update( player);
	}
	@Test
	public void getPlayerById(){
		
		playerService.getPlayerById( 1);
		print();
	}
	@Test
	public void getPagePlayer(){
		
		playerService.getPagePlayer( new PageInfo());
		print();
	}
	
	@Test
	public void delete(){
		playerService.delete( 12);
	}
	
}
