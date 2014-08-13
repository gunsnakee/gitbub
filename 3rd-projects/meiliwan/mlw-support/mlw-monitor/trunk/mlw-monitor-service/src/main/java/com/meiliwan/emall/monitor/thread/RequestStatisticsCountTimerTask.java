package com.meiliwan.emall.monitor.thread;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.bean.Ip2city;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.dao.Ip2cityDao;
import com.meiliwan.emall.monitor.redis.ProvinceRedis;
import com.meiliwan.emall.monitor.redis.RequestCountRedis;
import com.meiliwan.emall.monitor.service.MonitorService;
import com.meiliwan.emall.monitor.util.IP2Long;

/**
 * Request各总数缓存
 * @author rubi
 *
 */
@Service
public class RequestStatisticsCountTimerTask extends TimerTask{

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(RequestStatisticsCountTimerTask.class);
	private Timer timer = new Timer();
	private static int hour=60;
	private static int TIMER_TIME=hour*60*1000;//心跳时间60分钟
	@Autowired
	private MonitorService monitorService;
	
	private RequestCountRedis requestCountRedis = RequestCountRedis.getInstance();
	private RequestStatisticsCountTimerTask(){
		init();
	}
	private void init(){
		logger.info("RequestStatisticsCountTimerTask init", null, null);
		try {
			timer.schedule(this, 50000, TIMER_TIME);
		} catch (Exception e) {
			logger.error(e, timer, null);
		}
	}
	@Override
	public void run() {
		//查询,判断，发送
		try {
			List<RequestLog> list = queryRequestLog();
			for (RequestLog requestLog : list) {
			}
		} catch (Exception e) {
			logger.error(e, null, null);
		}
	}
	
	
	private List<RequestLog> queryRequestLog(){
		List<RequestLog> list = monitorService.getRequestLogListByNegativeMinitueAndType(-hour);
		return list;
	}
	
}
