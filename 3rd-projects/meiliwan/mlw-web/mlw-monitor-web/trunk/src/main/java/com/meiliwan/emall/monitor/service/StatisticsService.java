package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.dao.MLWLogDao;
import com.meiliwan.emall.monitor.dao.RequestLogDao;
import com.meiliwan.emall.monitor.statistics.RequestCount;

@Service
public class StatisticsService {

	@Autowired
	private RequestLogDao requestLogDao;
	@Autowired
	private MLWLogDao monitorExceptionDao;
	
	public List<RequestCount> getRequestCountGroupByType(){
		
		List<RequestCount> list = requestLogDao.getRequestCountGroupByType();
		return list;
	}
	
	public List<RequestCount> getRequestCountGroupByHour(RequestLogVO vo){
		
		List<RequestCount> list = requestLogDao.getRequestCountGroupByHour(vo);
		for (int i=0;i<24;i++) {
			if(list.size()<=i){
				RequestCount rc = new RequestCount();
				rc.setCount(0);
				rc.setName(i+"");
				list.add(rc);
			}
		}
		return list;
	}
}
