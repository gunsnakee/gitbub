package com.meiliwan.emall.union.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.union.bean.OrdLogistics;
import com.meiliwan.emall.union.dao.TransportDao;

@Service
public class FetchLogisticsService {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(FetchLogisticsService.class);
	private static int TIMER_MINUTE=10;//心跳时间十分钟
	@Autowired
	private EMSService eMSService;
	@Autowired
	private SFService sFService;
	@Autowired
	private TransportDao transportDao;
	
	private FetchLogisticsService(){
		logger.info("FetchLogisticsService init", null, null);
	}

	public void run() {
		//查询,判断，发送
		synchronized(this){
			try {
				Set<OrdLogistics> set = queryOrderLogisticsMinitueAgo();
				for (OrdLogistics ol : set) {
					if(ol.isEMS()){
						logger.info("绑定订单EMS", ol, null);
						eMSService.orderIdBindingEmsNo(ol.getOrder_id(), ol.getLogistics_number());
					}
					if(ol.isSF()){
						logger.info("绑定订单SF", ol, null);
 						sFService.bindOrderAndMailNo(ol.getOrder_id(), ol.getLogistics_number());
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.error(e, null, null);
			}
		}
	}
	
	private Date getMinute(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -TIMER_MINUTE);
		return cal.getTime();
	}
	
	
	
	private Set<OrdLogistics> queryOrderLogisticsMinitueAgo(){
		Date minuteAgo = getMinute();
		//已经去重复
		Set<OrdLogistics> list = transportDao.queryOrderLogisticsMinitueAgo(minuteAgo);
		return list;
	}
}



