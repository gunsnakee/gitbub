package com.meiliwan.emall.monitor.statistics;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.meiliwan.emall.monitor.bean.OrderStatistics;

public class OrderStatisticsVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1976964533586995759L;
	//截止今天几点
	private Date todayEndTime;
	private Date yesterdayEndTime;
	private OrderStatistics todayCount;
	private OrderStatistics yesterdayCount;
	private OrderStatistics yesterdayEndTimeCount;
	
	public OrderStatistics getYesterdayEndTimeCount() {
		return yesterdayEndTimeCount;
	}
	public void setYesterdayEndTimeCount(OrderStatistics yesterdayEndTimeCount) {
		this.yesterdayEndTimeCount = yesterdayEndTimeCount;
	}
	public Date getYesterdayEndTime() {
		return yesterdayEndTime;
	}
	public void setYesterdayEndTime(Date yesterdayEndTime) {
		this.yesterdayEndTime = yesterdayEndTime;
	}
	public Date getTodayEndTime() {
		return todayEndTime;
	}
	public void setTodayEndTime(Date todayEndTime) {
		this.todayEndTime = todayEndTime;
	}
	public OrderStatistics getTodayCount() {
		return todayCount;
	}
	public void setTodayCount(OrderStatistics todayCount) {
		this.todayCount = todayCount;
	}
	public OrderStatistics getYesterdayCount() {
		return yesterdayCount;
	}
	public void setYesterdayCount(OrderStatistics yesterdayCount) {
		this.yesterdayCount = yesterdayCount;
	}
	@Override
	public String toString() {
		return "OrderStatisticsVO [todayCount=" + todayCount
				+ ", yesterdayCount=" + yesterdayCount + "]";
	}
	
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:m0:00");
		  String a1=sdf.format(new Date());
		  System.out.println(a1);
	}
}
