package com.meiliwan.emall.pay;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.BaseTest;
import com.meiliwan.emall.service.pay.PayCheckService;

public class PayServiceTest extends BaseTest{
	@Autowired
	protected PayCheckService payService ;
	
	@Test
	public void testPayService(){
		payService.autoCMBPayDZ(new JsonObject()) ;
	}
}
