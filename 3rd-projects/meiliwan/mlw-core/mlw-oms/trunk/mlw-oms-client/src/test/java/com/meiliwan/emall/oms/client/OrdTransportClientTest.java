package com.meiliwan.emall.oms.client;

import org.testng.annotations.Test;

public class OrdTransportClientTest {

	@Test
	public void getListByOrderId(){
		OrdTransportClient.getListByOrderId("111");
	}
}
