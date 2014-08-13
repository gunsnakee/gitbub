package com.meiliwan.emall.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.bean.OrderStatistics;

public class OrderStatisticsServiceTest extends MonitorBaseTest{

	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	private JsonObject resultObj = new JsonObject();
	
	@Test
	public void getTwoDayCount(){
		orderStatisticsService.getTwoDayCount();
		System.out.println(resultObj);
	}
	
	@Test
	public void add(){
		for (int i = 0; i < 7; i++) {
			
			OrderStatistics orderStatistics = new OrderStatistics();
			orderStatistics.setCodCancel(7);
			orderStatisticsService.add(orderStatistics);
			System.out.println(resultObj);
		}
	}
}
