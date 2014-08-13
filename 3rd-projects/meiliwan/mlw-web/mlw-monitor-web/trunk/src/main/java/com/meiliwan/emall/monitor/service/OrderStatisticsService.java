package com.meiliwan.emall.monitor.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.monitor.bean.OrderStatistics;
import com.meiliwan.emall.monitor.dao.OrderStatisticsDao;
import com.meiliwan.emall.monitor.statistics.OrderStatisticsVO;

@Service
public class OrderStatisticsService {

	@Autowired
	private OrderStatisticsDao orderStatisticsDao;
	
	public void add(OrderStatistics orderStatistics){
		if(!orderStatistics.isAllZero()){
			orderStatistics.setCreateTime(new Date());
			OrderStatistics bean = orderStatisticsDao.getEntityByObj(orderStatistics);
			if(bean==null){
				orderStatisticsDao.insert(orderStatistics);
			}
		}
	}
	
	private Date getTodayZeroDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	private Date getYesterdayZeroDate(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(getTodayZeroDate());
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	
	public OrderStatisticsVO getTwoDayCount(){
		
		
		OrderStatistics yesterday = yesterdayStatistics();
		OrderStatistics today = todayStatistics();
		
		OrderStatisticsVO vo = new OrderStatisticsVO();
		
		Calendar cal = Calendar.getInstance();
		
		int min = cal.get(Calendar.MINUTE);
		min = min/10*10;
		cal.set(Calendar.MINUTE, min);
		cal.set(Calendar.SECOND, 0);
		
		Date now = cal.getTime();
		vo.setTodayEndTime(now);
		cal.add(Calendar.DATE, -1);
		vo.setYesterdayEndTime(cal.getTime());
		
		OrderStatistics yesterdayEndTimeCount = yesterdayEndTimeStatistics(cal.getTime());
		vo.setTodayCount(today);
		vo.setYesterdayCount(yesterday);
		vo.setYesterdayEndTimeCount(yesterdayEndTimeCount);
		return vo;
	}

	private OrderStatistics yesterdayEndTimeStatistics(Date yesterDayEndTime) {
		OrderStatistics entity = new OrderStatistics();
		entity.setCreateTimeStart(getYesterdayZeroDate());
		entity.setCreateTimeEnd(yesterDayEndTime);
		
		List<OrderStatistics> list = orderStatisticsDao.getListByObj(entity);
		OrderStatistics yesterday=new OrderStatistics();
		for (OrderStatistics os : list) {
			yesterday.addCreateCod(os.getCreateCod());
			yesterday.addCancel(os.getCancel());
			yesterday.addCreatePay(os.getCreatePay());
			yesterday.addPayFinish(os.getPayFinish());
			yesterday.addCodCancel(os.getCodCancel());
		}
		//设置有效订单数
		yesterday.setEffective();
		return yesterday;
	}
	
	
	private OrderStatistics yesterdayStatistics() {
		OrderStatistics entity = new OrderStatistics();
		entity.setCreateTimeStart(getYesterdayZeroDate());
		entity.setCreateTimeEnd(getTodayZeroDate());
		
		List<OrderStatistics> list = orderStatisticsDao.getListByObj(entity);
		OrderStatistics yesterday=new OrderStatistics();
		for (OrderStatistics os : list) {
			yesterday.addCreateCod(os.getCreateCod());
			yesterday.addCancel(os.getCancel());
			yesterday.addCreatePay(os.getCreatePay());
			yesterday.addPayFinish(os.getPayFinish());
			yesterday.addCodCancel(os.getCodCancel());
		}
		//设置有效订单数
		yesterday.setEffective();
		return yesterday;
	}
	
	private OrderStatistics todayStatistics() {
		OrderStatistics entity = new OrderStatistics();
		entity.setCreateTimeStart(getTodayZeroDate());
		
		List<OrderStatistics> list = orderStatisticsDao.getListByObj(entity);
		OrderStatistics day=new OrderStatistics();
		
		for (OrderStatistics os : list) {
			day.addCreateCod(os.getCreateCod());
			day.addCancel(os.getCancel());
			day.addCreatePay(os.getCreatePay());
			day.addPayFinish(os.getPayFinish());
			day.addCodCancel(os.getCodCancel());
			
		}
		//设置有效订单数
		day.setEffective();
		return day;
	}
	
	public static void main(String[] args) {
		OrderStatisticsService ss = new OrderStatisticsService();
		Date d = ss.getTodayZeroDate();
		System.out.println(d);
	}
}
