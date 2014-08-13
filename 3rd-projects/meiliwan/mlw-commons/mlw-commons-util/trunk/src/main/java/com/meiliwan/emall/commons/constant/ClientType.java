package com.meiliwan.emall.commons.constant;

/**
 * 客户端类型， 用来标记用户以什么样的客户端来请求
 */
public enum ClientType {

	PC("PC","PC"),
	IOS("IOS","IOS"),
    ANDRIOD("ANDRIOD","Andriod"),
    WP("WP","Windows Phone");

	private String type;
	private String desc;
	public String getType() {
		return type;
	}
	public String getDesc() {
		return desc;
	}
	private ClientType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	
}
