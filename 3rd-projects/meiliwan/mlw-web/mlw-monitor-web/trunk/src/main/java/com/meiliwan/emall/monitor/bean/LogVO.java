package com.meiliwan.emall.monitor.bean;

public class LogVO extends Log{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6166264863644666586L;
	private Long startTimeConsume;
	private Long endTimeConsume;
	/** timeConsume 排序 */
	private Boolean timeConsumeDesc;
	
	public Long getStartTimeConsume() {
		return startTimeConsume;
	}
	public void setStartTimeConsume(Long startTimeConsume) {
		this.startTimeConsume = startTimeConsume;
	}
	public Long getEndTimeConsume() {
		return endTimeConsume;
	}
	public void setEndTimeConsume(Long endTimeConsume) {
		this.endTimeConsume = endTimeConsume;
	}
	public Boolean getTimeConsumeDesc() {
		return timeConsumeDesc;
	}
	public void setTimeConsumeDesc(Boolean timeConsumeDesc) {
		this.timeConsumeDesc = timeConsumeDesc;
	}
	
	
	
}
