package com.meiliwan.emall.monitor.service;

import java.util.Timer;
import java.util.TimerTask;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class MonitorTimerTask extends TimerTask{

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(MonitorTimerTask.class);
	private Cache cache = Cache.getInstance();
	private Timer timer = new Timer();
	private static int TIMER_TIME=60000;//心跳时间
	
	public MonitorTimerTask(){
		logger.info("MonitorTimerTask init", null, null);
		try {
			//延时1000毫秒后重复的执行task，周期是TIMER_TIME毫秒
			timer.schedule(this, 1000, TIMER_TIME);
		} catch (Exception e) {
			logger.error(e, timer, null);
		}
	}
	//timer run
	@Override
	public void run() {
		cache.addToMonitorBuffer();
	}
}
