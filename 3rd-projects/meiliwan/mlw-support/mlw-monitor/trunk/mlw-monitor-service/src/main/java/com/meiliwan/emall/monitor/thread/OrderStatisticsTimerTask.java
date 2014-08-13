package com.meiliwan.emall.monitor.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.bean.OrderStatistics;
import com.meiliwan.emall.monitor.service.OrderService;
import com.meiliwan.emall.monitor.service.OrderStatisticsService;

@Service
public class OrderStatisticsTimerTask extends TimerTask{

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(OrderStatisticsTimerTask.class);
	private static int TIMER_MINUTE=10;//心跳时间十分钟
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	private OrderStatisticsTimerTask(){
		logger.info("OrderStatisticsTimerTask init", null, null);
	}
	@Override
	public void run() {
		//查询,判断，发送
		synchronized(this){
			Date minuteAgo=getMinute();
			try {
				logger.info("OrderStatisticsTimerTask run=======", minuteAgo, null);
				int createCod = totalCreateOrderCOD(minuteAgo);
				int createPay = totalCreateOrderPayOnLine(minuteAgo);
				int cancel = totalCancelOrder(minuteAgo);
				int payFinish  = totalOrderPayFinish(minuteAgo);
				int codCancel = totalCODCancel(minuteAgo);
				OrderStatistics orderStatistics = new OrderStatistics();
				orderStatistics.setCancel(cancel);
				orderStatistics.setCreateCod(createCod);
				orderStatistics.setCreatePay(createPay);
				orderStatistics.setPayFinish(payFinish);
				orderStatistics.setCodCancel(codCancel);
				orderStatisticsService.add(orderStatistics);
			} catch (Exception e) {
				logger.error(e, minuteAgo, null);
			}
		}
	}
	
	private Date getMinute(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -TIMER_MINUTE);
		return cal.getTime();
	}
	
	private int totalCODCancel(Date minuteAgo){
		int count = orderService.getCODCancelCount(minuteAgo);
		return count;
	}
	
	
	private int totalCreateOrderCOD(Date minuteAgo){
		int count = orderService.getCreateCODCount(minuteAgo);
		return count;
	}
	
	private int totalCreateOrderPayOnLine(Date minuteAgo){
		int count = orderService.getCreatePayCount(minuteAgo);
		return count;
	}
	
	private int totalCancelOrder(Date minuteAgo){
		int count = orderService.getCancelCount(minuteAgo);
		return count;
	}
	
	private int totalOrderPayFinish(Date minuteAgo){
		int count = orderService.getPayFinishCount(minuteAgo);
		return count;
	}
}
