package com.meiliwan.emall.monitor.service;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.bean.RequestLogVO;

public class StatisticsServiceTest extends MonitorBaseTest{
	 
	@Autowired
	private StatisticsService statisticsService;
	@Test
	public void testGetRequestTypeCount(){
		JsonObject resultObj = new JsonObject();
		statisticsService.getRequestCountGroupByType();
		System.out.println(resultObj);
	}
	
	@Test
	public void testGetRequestHourCount(){
		RequestLogVO vo = new RequestLogVO();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -1);
		vo.setStartTime(cal.getTime());
		JsonObject resultObj = new JsonObject();
		statisticsService.getRequestCountGroupByHour(vo);
		System.out.println(resultObj);
	}
	
}

