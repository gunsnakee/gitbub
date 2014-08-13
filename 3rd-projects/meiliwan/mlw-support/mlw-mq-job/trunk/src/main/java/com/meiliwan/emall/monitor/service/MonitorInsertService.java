package com.meiliwan.emall.monitor.service;

import java.util.List;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.dao.RequestLogDao;

/**
 * 监控服务,创建这一个是因为另一个被代理。此类没有继承baseService
 * @author rubi
 *
 */
public class MonitorInsertService  {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
	private RequestLogDao requestLogDao;

	public void setRequestLogDao(RequestLogDao requestLogDao) {
		this.requestLogDao = requestLogDao;
	}

	public void saveLog(RequestLog log) {
        if(log==null){
        		return ;
        }
        requestLogDao.insert(log);
    }
	
	public void insertBatch(List<RequestLog> list){
		if(list==null||list.size()<=0){
			return ;
		}
		try {
			requestLogDao.insertBatch(list);
			list.clear();
			list=null;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, list, null);
		}
	}
	
	
	
}
