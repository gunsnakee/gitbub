package com.meiliwan.emall.bean.union;


import com.meiliwan.emall.core.bean.BaseEntity;

public class OrderPay extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer Id;
	private String OrderId;
	private int PayStatus;
	private String PayMethod;
	private int OrderStatus;
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return Id;
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
	public void setId(Integer id) {
		Id = id;
	}
	public String getOrderId() {
		return OrderId;
	}
	public void setOrderId(String orderId) {
		OrderId = orderId;
	}
}
