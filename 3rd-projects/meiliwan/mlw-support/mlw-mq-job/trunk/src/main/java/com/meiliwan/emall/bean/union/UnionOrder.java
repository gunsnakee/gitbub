package com.meiliwan.emall.bean.union;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class UnionOrder extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private int Id;
	private String sourceidId;
	private String SourceType;
	private String OrderId;
	private int UId;
	private double TotalPrice;
	private Date CreateTime;
	private String QueryInfo;
	private String mlwQueryInfo;
	private int PayStatus;
	private String PayMethod;
	private int OrderStatus;
	
	@SuppressWarnings("unchecked")
	public Integer getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getSourceidId() {
		return sourceidId;
	}
	public void setSourceidId(String sourceidId) {
		this.sourceidId = sourceidId;
	}
	public String getSourceType() {
		return SourceType;
	}
	public void setSourceType(String sourceType) {
		SourceType = sourceType;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
	public int getUId() {
		return UId;
	}
	public void setUId(int uId) {
		UId = uId;
	}
	public double getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		TotalPrice = totalPrice;
	}
	public Date getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}
	public String getQueryInfo() {
		return QueryInfo;
	}
	public void setQueryInfo(String queryInfo) {
		QueryInfo = queryInfo;
	}
	public int getPayStatus() {
		return PayStatus;
	}
	public void setPayStatus(int payStatus) {
		PayStatus = payStatus;
	}
	public String getPayMethod() {
		return PayMethod;
	}
	public void setPayMethod(String payMethod) {
		PayMethod = payMethod;
	}
	public int getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getMlwQueryInfo() {
		return mlwQueryInfo;
	}
	public void setMlwQueryInfo(String mlwQueryInfo) {
		this.mlwQueryInfo = mlwQueryInfo;
	}
	
}
