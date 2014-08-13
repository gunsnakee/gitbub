package com.meiliwan.emall.oms.constant;

public enum ReturnReasonType {

	QUALITY("QU","商品质量问题"),
	TRANS("TR","物流问题"),
	USER("USER","个人原因"),
	OTHER("OTHER","其他原因");
	
	private String code;
	private String desc;
	private ReturnReasonType(String code, String desc) {
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
