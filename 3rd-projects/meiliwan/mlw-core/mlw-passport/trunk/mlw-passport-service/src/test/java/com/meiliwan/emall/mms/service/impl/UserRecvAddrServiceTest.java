package com.meiliwan.emall.mms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.service.UserRecvAddrService;

public class UserRecvAddrServiceTest extends BaseTest{

	@Autowired
	private UserRecvAddrService service;
	
	@Test
	public void testFindById(){
		JsonObject resultObj = new JsonObject();
		service.getUserAddressById(resultObj, 1);
		System.out.println(resultObj);
	}
	
}
