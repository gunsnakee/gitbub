package com.meiliwan.emall.base.client;

import com.meiliwan.emall.base.dto.IPLocation;

public class BaseIPParseClientTest {

	public static void main(String[] args) {
		IPLocation loc = BaseIPParseClient.getIPLocation("121.31.30.146");
		
		System.out.println(loc.getCountry());
		System.out.println(loc.getProvince());
		System.out.println(loc.getCity());
		System.out.println(loc.getDetail());
		System.out.println(loc.getArea());
	}
	
}
