package com.meiliwan.emall.oms.bean;


import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class OrdiStatus extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3103018630530735891L;

    private String orderId;

    private String orderItemId;

    private String statusCode;

    private String statusType;

    private short billType;

    private Integer uid;

    private Integer adminId;
    
    private Date createTime = new Date();

    private Date updateTime = new Date();

    private short state;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
	}

	public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId == null ? null : orderItemId.trim();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType == null ? null : statusType.trim();
    }

    public short getBillType() {
        return billType;
    }

    public void setBillType(short billType) {
        this.billType = billType;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public String getId() {
        return orderItemId;
    }
}