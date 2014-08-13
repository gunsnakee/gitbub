package com.meiliwan.emall.oms.dto;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class OrdExceptionDTO extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8687566861972049671L;

    private Integer id;

    private String bizCode;

    private String orderId;
    private String orderItemId;
    private String statusType;
    private String statusCode;
    private String ordLastStatus;

    private String errorCode;

    private String errorMsg;

    private Date updateTime;

    private Date createTimeMin;
    
    private Date createTimeMax;

    private Short state;

    private Short billType;
    private int uid;
    private String adminId;
    
    @SuppressWarnings("unchecked")
	@Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }


    public String getOrdLastStatus() {
        return ordLastStatus;
    }

    public void setOrdLastStatus(String ordLastStatus) {
        this.ordLastStatus = ordLastStatus == null ? null : ordLastStatus.trim();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode == null ? null : errorCode.trim();
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTimeMin() {
		return createTimeMin;
	}

	public void setCreateTimeMin(Date createTimeMin) {
		this.createTimeMin = createTimeMin;
	}

	public Date getCreateTimeMax() {
		return createTimeMax;
	}

	public void setCreateTimeMax(Date createTimeMax) {
		this.createTimeMax = createTimeMax;
	}

	public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getBillType() {
        return billType;
    }

    public void setBillType(Short billType) {
        this.billType = billType;
    }

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
    
}