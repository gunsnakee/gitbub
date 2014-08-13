package com.meiliwan.emall.base.client;

import org.testng.annotations.Test;

import com.meiliwan.emall.base.dto.TransportPriceTip;

public class TransportPriceClientTest {

	@Test
	public void findUnionStationFareByAreaCode(){
		TransportPriceTip tip = TransportPriceClient.findUnionStationFareByAreaCode("444");
		System.out.println(tip);
	}
}
