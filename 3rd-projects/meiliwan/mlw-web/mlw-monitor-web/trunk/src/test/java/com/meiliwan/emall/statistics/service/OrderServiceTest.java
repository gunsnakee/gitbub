package com.meiliwan.emall.statistics.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.service.OrderService;

public class OrderServiceTest extends MonitorBaseTest {

	@Autowired
	private OrderService orderService;
	
	@Test
	public void getCreateCODCount(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -1000000);
		int count = orderService.getCreateCODCount(cal.getTime());
		System.out.println(count);
	}
	
	@Test
	public void getCreatePayCount(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -1000000);
		int count = orderService.getCreatePayCount(cal.getTime());
		System.out.println(count);
	}
	
	@Test
	public void getPayFinishCount(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -1000000);
		int count = orderService.getPayFinishCount(cal.getTime());
		System.out.println(count);
	}
	
	@Test
	public void getCancelCount(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -1000000);
		int count = orderService.getCancelCount(cal.getTime());
		System.out.println(count);
	}
	
	
	
}
