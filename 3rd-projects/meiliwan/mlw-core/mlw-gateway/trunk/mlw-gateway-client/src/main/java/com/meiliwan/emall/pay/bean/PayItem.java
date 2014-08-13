package com.meiliwan.emall.pay.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class PayItem extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1600496792273185233L;

	private String payId;

	private String orderId;

	private String payCode;

	private BigDecimal payAmount;

	private String thirdTradeNo;

	private String thirdPayUid;

	private String thirdPayEmail;

	private String payStatus;

	private Date createTime;

	private String extraInfo;
	
	private Date successTime;

	public Date getSuccessTime() {
		return successTime;
	}

	public void setSuccessTime(Date successTime) {
		this.successTime = successTime;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId == null ? null : payId.trim();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId == null ? null : orderId.trim();
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode == null ? null : payCode.trim();
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getThirdTradeNo() {
		return thirdTradeNo;
	}

	public void setThirdTradeNo(String thirdTradeNo) {
		this.thirdTradeNo = thirdTradeNo == null ? null : thirdTradeNo.trim();
	}

	public String getThirdPayUid() {
		return thirdPayUid;
	}

	public void setThirdPayUid(String thirdPayUid) {
		this.thirdPayUid = thirdPayUid == null ? null : thirdPayUid.trim();
	}

	public String getThirdPayEmail() {
		return thirdPayEmail;
	}

	public void setThirdPayEmail(String thirdPayEmail) {
		this.thirdPayEmail = thirdPayEmail == null ? null : thirdPayEmail
				.trim();
	}

	public String getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus == null ? null : payStatus.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo == null ? null : extraInfo.trim();
	}

	@SuppressWarnings("unchecked")
	public String getId() {
		return this.payId;
	}

	@Override
	public String toString() {
		return "{payId:" + payId + ",orderId:" + orderId + ",payCode:"
				+ payCode + ",payAmount:" + payAmount + ",thirdTradeNo:"
				+ thirdTradeNo + ",thirdPayUid:" + thirdPayUid
				+ ",thirdPayEmail:" + thirdPayEmail + ",payStatus:" + payStatus
				+ ",createTime:" + createTime + ",extraInfo:" + extraInfo
				+ "}";
	}
}