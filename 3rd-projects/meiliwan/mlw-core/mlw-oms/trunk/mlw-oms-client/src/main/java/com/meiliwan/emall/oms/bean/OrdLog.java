package com.meiliwan.emall.oms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class OrdLog extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5713838533313745424L;

	private Integer id;

    private String bizCode;

    private String orderId;

    private String orderItemId;

    private String orderType;

    private String statusCode;

    private String content;

    private Short state;

    private Short billType;

    private Integer opteratorId;

    private String opteratorName;

    private String opteratorType;

    private Date createTime;

    @SuppressWarnings("unchecked")
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
        this.bizCode = bizCode == null ? null : bizCode.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId == null ? null : orderItemId.trim();
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode == null ? null : statusCode.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }


	public Integer getOpteratorId() {
        return opteratorId;
    }

    public void setOpteratorId(Integer opteratorId) {
        this.opteratorId = opteratorId;
    }
    
    public String getOpteratorName() {
		return opteratorName;
	}

	public void setOpteratorName(String opteratorName) {
		this.opteratorName = opteratorName;
	}

	public String getOpteratorType() {
        return opteratorType;
    }

    public void setOpteratorType(String opteratorType) {
        this.opteratorType = opteratorType == null ? null : opteratorType.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	@Override
	public String toString() {
		return "OmsOrdLog [id=" + id + ", bizCode=" + bizCode + ", orderId="
				+ orderId + ", orderItemId=" + orderItemId + ", orderType="
				+ orderType + ", statusCode=" + statusCode + ", content="
				+ content + ", state=" + state + ", billType=" + billType
				+ ", opteratorId=" + opteratorId + ", opteratorName="
				+ opteratorName + ", opteratorType=" + opteratorType
				+ ", createTime=" + createTime + "]";
	}
}