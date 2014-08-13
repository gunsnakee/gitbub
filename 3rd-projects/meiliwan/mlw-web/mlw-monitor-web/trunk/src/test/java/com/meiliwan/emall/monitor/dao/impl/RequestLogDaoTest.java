package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.testng.annotations.Test;

import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.dao.RequestLogDao;
import com.meiliwan.emall.monitor.statistics.RequestCount;

public class RequestLogDaoTest extends MonitorBaseTest{

	@Autowired
	private RequestLogDao requestLogDao;
	
	@Test
	public void testInsert(){
		RequestLog log = new RequestLog();
		log.setTimeConsume(11111l);
		for (int i = 0; i < 1000; i++) {
			
			log.setType(i+"");
			requestLogDao.insert(log);
		}
		
	}
	
	@Test
	public void testInsertBatch(){
		List<RequestLog> list = new ArrayList<RequestLog>();
		for (int i = 0; i < 12; i++) {
			RequestLog log = new RequestLog();
			log.setTimeConsume(11111l);
			log.setType(i+"");
			list.add(log);
		}
		requestLogDao.insertBatch(list);
		
	}
	
	@Test
	public void testGetRequestCountGroupByType(){
		List<RequestCount> list = requestLogDao.getRequestCountGroupByType();
		System.out.println(list);
		Assert.notEmpty(list, "not empty");
	}
}
