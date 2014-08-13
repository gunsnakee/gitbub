package com.meiliwan.emall.sp2.client;

import org.testng.annotations.Test;

import com.meiliwan.emall.sp2.bean.LotteryUserTimes;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes.SOURCE;

public class LotteryUserTimesClientTest {

	@Test
	 public  void addTimes(){
		LotteryUserTimesClient.addTimes(1, 11, SOURCE.MOBILE_PASS, "111111111");
	}

}
