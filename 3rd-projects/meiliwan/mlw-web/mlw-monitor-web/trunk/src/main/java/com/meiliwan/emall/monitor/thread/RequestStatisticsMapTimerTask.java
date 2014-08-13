package com.meiliwan.emall.monitor.thread;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.bean.Ip2city;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.dao.Ip2cityDao;
import com.meiliwan.emall.monitor.redis.ProvinceRedis;
import com.meiliwan.emall.monitor.service.MonitorService;
import com.meiliwan.emall.monitor.util.IP2Long;

/**
 * 地图缓存
 * @author rubi
 *
 */
@Service
public class RequestStatisticsMapTimerTask extends TimerTask{

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(RequestStatisticsMapTimerTask.class);
	private Timer timer = new Timer();
	private static int hour=60;
	private static int TIMER_TIME=hour*60*1000;//心跳时间60分钟
	@Autowired
	private MonitorService monitorService;
	@Autowired
	private Ip2cityDao ip2cityDao;
	
	private ProvinceRedis provinceRedis = ProvinceRedis.getInstance();
	private RequestStatisticsMapTimerTask(){
		init();
	}
	private void init(){
		logger.info("RequestStatisticsMapTimerTask init", null, null);
		try {
			//延时1000毫秒后重复的执行task，周期是TIMER_TIME毫秒
			timer.schedule(this, 10000, TIMER_TIME);
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
				if(StringUtils.isEmpty(requestLog.getClientIp())){
					return ;
				}
				String province = judgeProviceByIP(requestLog.getClientIp());
				provinceRedis.redisIncrement(province);
			}
		} catch (Exception e) {
			logger.error(e, null, null);
		}
	}
	
	
	private String judgeProviceByIP(String ip){
		long longIp=0l;
		longIp = IP2Long.ip2long(ip);
		Ip2city ip2city = new Ip2city();
		ip2city.setStartIp(longIp);
		ip2city.setEndIp(longIp);
		Ip2city result = ip2cityDao.getEntityByObj(ip2city);
		if(result==null){
			return "other";
		}
		return result.getProvince();
	}
	
	private List<RequestLog> queryRequestLog(){
		List<RequestLog> list = monitorService.getRequestLogListByNegativeMinitueAndType(-hour);
		return list;
	}
	
	
	
}
