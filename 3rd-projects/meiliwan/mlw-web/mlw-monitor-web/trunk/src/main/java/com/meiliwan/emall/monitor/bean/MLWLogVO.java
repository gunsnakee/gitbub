package com.meiliwan.emall.monitor.bean;

import java.util.Date;


public class MLWLogVO extends MLWLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2540945771453470107L;

	//排序
	private Boolean order;
	private Date createTimeStart;
	private Date createTimeEnd;
	public Boolean getOrder() {
		return order;
	}

	public void setOrder(Boolean order) {
		this.order = order;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	

    
}