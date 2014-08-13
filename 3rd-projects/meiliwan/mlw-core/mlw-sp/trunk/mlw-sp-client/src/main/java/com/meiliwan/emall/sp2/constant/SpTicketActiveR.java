package com.meiliwan.emall.sp2.constant;

public enum SpTicketActiveR {
	
	SUCCESS("成功"),
	
	FAIL_UNSELL("未销售"),
	FAIL_USED("已激活或已使用"),
	FAIL_DATED("已过期"),
	
	FAIL_NOT_EXIST("不存在"),
	FAIL_PARAM_ILLEGAL("参数不合法"),
	FAIL_USER_ILLEGAL("用户不合法"),
	FAIL_ILLEGAL("不合法"),
	FAIL_EXP("");
	
	
	private String desc;
	
	private SpTicketActiveR(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
	public static void main(String[] args) {
		for ( SpTicketActiveR sar : SpTicketActiveR.values()) {
			System.out.println(sar.name());
		}
	}
}
