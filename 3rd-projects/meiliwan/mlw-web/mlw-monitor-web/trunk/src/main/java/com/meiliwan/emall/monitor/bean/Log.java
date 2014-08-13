package com.meiliwan.emall.monitor.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 *   type(name)(start_time)(end_time)(otherInfo)
 *    1、interceptor日志：intr(/product/getTimelyInfo)(2013-08-19 17:43:22.123)(2013-08-19 17:43:22.524)
   2、ice的client日志： iceC(productServiceClient/getProduct)(2013-08-19 17:43:22.123)(2013-08-19 17:43:22.524)
   3、ice的service日志：iceS(productServiceClient/getProduct)(2013-08-19 17:43:22.123)(2013-08-19 17:43:22.524)
   4、dao日志：dao(getProduct)(2013-08-19 17:43:22.123)(2013-08-19 17:43:22.524)(sql:select * from product where proId=?, param:12)
 * @author rubi
 *
 */
public class Log extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5972767497935418469L;
	private Integer id;
	private String type;
	private String name;
	private Integer hour;
	private Date startTime;
	private Date endTime;
	private Long timeConsume;
	private String otherInfo;
	
	@SuppressWarnings("unchecked")
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getHour() {
		return hour;
	}
	public void setHour(Integer hour) {
		this.hour = hour;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getTimeConsume() {
		return timeConsume;
	}
	public void setTimeConsume(Long timeConsume) {
		this.timeConsume = timeConsume;
	}
	public String getOtherInfo() {
		return otherInfo;
	}

	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
}
