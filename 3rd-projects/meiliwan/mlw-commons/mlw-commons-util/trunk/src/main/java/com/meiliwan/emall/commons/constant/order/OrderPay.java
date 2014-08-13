package com.meiliwan.emall.commons.constant.order;

/**
 * 配送管理支付方式
 * @author yinggao.zhuo
 * @date 2013-6-16
 */
public enum OrderPay {

	//CASH001
	PAY_CASH("CAS","现金"),
	//010
	PAY_POS("POS","POS"),
	//100
	PAY_CHECK("CHK","支票");
	
	private String code;
	private String desc;
	private OrderPay(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
}
