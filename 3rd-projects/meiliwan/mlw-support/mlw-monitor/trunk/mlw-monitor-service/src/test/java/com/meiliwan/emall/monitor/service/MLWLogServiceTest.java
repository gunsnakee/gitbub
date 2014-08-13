package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;
import com.meiliwan.emall.monitor.bean.RequestLogVO;

public class MLWLogServiceTest extends MonitorBaseTest{

	@Autowired
	private MonitorService monitorService;
	
	
	
	@Test
	public void getMLWLogListQueryBy10MinitueAgo(){
		List<MLWLog> list = monitorService.getMLWLogListQueryBy10MinitueAgo();
		System.out.println(list);
	}
	
	@Test
	public void testGetPageRequest(){
		JsonObject resultObj = new JsonObject();
		RequestLogVO log = new RequestLogVO();
		log.setName("getaaaProduct");
		PageInfo pi = new PageInfo();
		pi.setPage(1);
		pi.setPagesize(3);
		monitorService.getPageRequest( log, pi);
		System.out.println(resultObj);
	}
	
	@Test
	public void getMLWLogListByUuid(){
		
	}
	
}
