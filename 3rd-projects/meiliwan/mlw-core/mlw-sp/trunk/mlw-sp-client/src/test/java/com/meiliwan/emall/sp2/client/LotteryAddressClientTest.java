package com.meiliwan.emall.sp2.client;

import org.testng.annotations.Test;

import com.meiliwan.emall.sp2.bean.LotteryAddress;

public class LotteryAddressClientTest {

	@Test
	public void insert(){
		//{"action":"lotteryAddressService/insert","params":{"elements":[
		//{"id":129,"uid":1923,"name":"111","phone":"111","province":"内蒙古自治区",
		//"provinceCode":"100015","city":"呼和浩特市","cityCode":"10001501",
		//"town":"市辖区","townCode":"1000150101","address":"111"}]}}
		
		LotteryAddress addr = new LotteryAddress();
		addr.setUid(11);
		addr.setAddress("addr");
		addr.setCity("city");
		addr.setName("name");
		addr.setPhone("phone");
		addr.setProvince("province");
		addr.setTown("tows");
		addr.setUid(11);
		LotteryAddressClient.insert(addr);
		
	}
	@Test
	public void getById(){
		LotteryAddress addr = LotteryAddressClient.getById(88);
		System.out.println(addr);
		
	}
}
