package com.meiliwan.emall.commons.constant.order;

/**
 * 退换货是用户操作，还是管理员操作
 * @author rubi
 *
 */
public enum OpteratorType {

	USER("US","用户操作"),
	ADMIN("AD","管理员操作"),
	SYSTEM("SY","系统");
	private String code;
	private String desc;
	private OpteratorType(String code, String desc) {
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
