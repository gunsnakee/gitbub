package com.meiliwan.emall.oms.constant;

public enum RetFlag {

    NONE((short)0,"没有退换货"),
    APPLY((short)1,"申请退换货");

	private short flag;
	private String desc;
	private RetFlag(short flag, String desc) {
		this.flag = flag;
		this.desc = desc;
	}
	public short getFlag() {
		return flag;
	}
	public String getDesc() {
		return desc;
	}
}
