package com.meiliwan.emall.oms.util;

public enum InvoiceType {

	//PT 普通发票；ZZ 增值税发票
	PT("PT","普通发票"),
	ZZ("ZZ","增值税发票");
	private String code;
	private String desc;
	private InvoiceType(String code, String desc) {
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
