package com.meiliwan.emall.oms.constant;

/**
 * 订单状态枚举
 * @author yuxiong
 *
 */
public enum OrderStatusType {

	//订单状态类型：订单行状态
	IS("IS"),
	//订单状态类型：订单行配送状态
	ID("ID"),
	//订单状态类型：订单行支付状态
	IP("IP"),
	//订单状态类型：订单行冲红状态
	IR("IR"),
	//订单状态类型：订单头状态
	OS("OS"),
	//订单状态类型：订单头支付状态
	OP("OP");
	
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	OrderStatusType(String type){this.type = type;}
}
