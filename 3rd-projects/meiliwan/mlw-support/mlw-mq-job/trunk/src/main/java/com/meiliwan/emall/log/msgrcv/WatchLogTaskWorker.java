package com.meiliwan.emall.log.msgrcv;

import java.util.Calendar;
import java.util.Date;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.service.Cache;
import com.meiliwan.emall.monitor.service.EmailMobileCache;

/**
 * 监控入口类
 * 
 * @author rubi
 * 
 */
public class WatchLogTaskWorker implements MsgTaskWorker {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(WatchLogTaskWorker.class);
	public static final int DAO_NOT_INSERT=5;
	public static final int OTHER_NOT_INSERT=10;
	
	// 缓冲区1
	private Cache cache = Cache.getInstance();
	// 缓存待发送log
	private EmailMobileCache emailMobileCache = EmailMobileCache.getInstance();

	/**
	 * 构造器只会进入一次
	 */
	public WatchLogTaskWorker() {

	}

	/**
	 * 日志的格式如下： type(name)(start_time)(end_time)(otherInfo)
	 * 
	 * 举例： 1、interceptor日志：intr(/product/getTimelyInfo)(2013-08-19
	 * 17:43:22.123)(2013-08-19 17:43:22.524) 2、ice的client日志：
	 * iceC(productServiceClient/getProduct)(2013-08-19 17:43:22.123)(2013-08-19
	 * 17:43:22.524)
	 * 3、ice的service日志：iceS(productServiceClient/getProduct)(2013-08-19
	 * 17:43:22.123)(2013-08-19 17:43:22.524) 4、dao日志：dao(getProduct)(2013-08-19
	 * 17:43:22.123)(2013-08-19 17:43:22.524)(sql:select * from product where
	 * proId=?, param:12)
	 */
	@Override
	public void handleMsg(String msg) {

		logger.debug("WatchLogTaskWorker handleMsg {}", msg);
		try {
			String strNotHasRight = msg.replaceAll("\\)", "");
			String[] items = strNotHasRight.split("\\(");
			if (items.length < 4) {
				logger.warn("WatchLogTaskWorker-handleMsg leng less 4 {}", msg,
						null);
				return;
			}
			
			RequestLog log = stringToLog(items);
			cache.add(log);
			emailMobileCache.add(log);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, msg, null);
		}

	}

	private RequestLog stringToLog(String[] items) {
		RequestLog log = new RequestLog();
		String type=items[0];
		log.setType(asStringTruncatedTo10(type));
		log.setName(items[1]);
		long startLong = Long.parseLong(items[2]);
		long endLong = Long.parseLong(items[3]);

		log.setStartTime(getStartTime(startLong));
		log.setEndTime(getEndTime(endLong));
		log.setTimeConsume(endLong - startLong);
		log.setHour(getHour(startLong));

		if (items.length > 4) {
			String otherInfo =items[4];
			log.setOtherInfo(otherInfo);
			if(otherInfo.startsWith("clientIP")&&RequestLog.INTR.equals(type)){
				log.setClientIp(getClientIp(otherInfo));
			}
		}
		return log;
	}

	private String getClientIp(String  otherInfo) {
		if(!StringUtil.checkNull(otherInfo)&&otherInfo.length()>21){
			String ip = otherInfo.substring(9, otherInfo.indexOf(","));
			return ip;
		}
		return "";
	}

	private Date getStartTime(long startLong) {
		Date startTime = new Date();
		startTime.setTime(startLong);
		return startTime;
	}

	private Date getEndTime(long endLong) {
		Date endTime = new Date();
		endTime.setTime(endLong);
		return endTime;
	}

	private int getHour(long startLong) {

		Date startTime = getStartTime(startLong);
		Calendar c = Calendar.getInstance();
		c.setTime(startTime);
		return c.get(Calendar.HOUR_OF_DAY);

	}

	String asStringTruncatedTo10(Object o) {
		String s = null;
		if (o != null) {
			s = o.toString();
		}

		if (s == null) {
			return null;
		}
		if (s.length() <= 10) {
			return s;
		} else {
			return s.substring(0, 10);
		}
	}

	public static void main(String[] args) {
		JsonObject json = new JsonObject();
		json.addProperty("name", "/product/getTimelyInfo");
		json.addProperty("start_time", "2013-08-19 17:43:22.123");
		json.addProperty("end_time", "2013-08-19 17:43:22.524");
		json.addProperty("otherInfo",
				"select * from product where proId=?, param:12");

		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			json.get("name");
			json.get("start_time");
			json.get("end_time");
			json.get("otherInfo");
		}
		long end1 = System.currentTimeMillis();
		System.out.println(end1 - start1);

		String str = "intr(/product/getTimelyInfo)()(2013-08-19 17:43:22.524)";
		String str2 = str.replaceAll("\\)", "");
		String[] strs = str2.split("\\(");
		System.out.println(strs.length);
		long start2 = System.currentTimeMillis();
		for (int i = 0; i < 10000000; i++) {
			for (String string : strs) {
				String s = string;
			}
		}
		long end2 = System.currentTimeMillis();
		System.out.println(end2 - start2);
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);

		System.out.println(hour);
	}

}
