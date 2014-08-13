package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.bean.Log;
import com.meiliwan.emall.monitor.dao.LogDao;

public class LogDaoTest extends MonitorBaseTest{

	@Autowired
	private LogDao logDao;
	
	@Test
	public void testInsert(){
		Log log = new Log();
		log.setTimeConsume(11111l);
		for (int i = 0; i < 1000; i++) {
			
			log.setType(i+"");
			logDao.insert(log);
		}
		
	}
	
	@Test
	public void testInsertBatch(){
		List<Log> list = new ArrayList<Log>();
		for (int i = 0; i < 12; i++) {
			Log log = new Log();
			log.setTimeConsume(11111l);
			log.setType(i+"");
			list.add(log);
		}
		logDao.insertBatch(list);
		
	}
	
	
}
