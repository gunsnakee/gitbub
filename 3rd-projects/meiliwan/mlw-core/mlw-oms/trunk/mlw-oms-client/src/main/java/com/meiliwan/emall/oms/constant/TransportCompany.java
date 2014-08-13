package com.meiliwan.emall.oms.constant;

public enum TransportCompany {

	SF("SF","顺丰快递","http://www.kiees.cn/sf/"),
	EMS("EMS","EMS","http://www.ems.com.cn/mailtracking/you_jian_cha_xun.html");
	
	private String code;
	private String desc;
	private String url;
	
	private TransportCompany(String code, String desc, String url) {
		this.code = code;
		this.desc = desc;
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
