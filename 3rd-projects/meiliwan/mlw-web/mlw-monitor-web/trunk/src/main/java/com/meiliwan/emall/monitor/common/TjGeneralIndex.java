package com.meiliwan.emall.monitor.common;

/**
 * tjGeneral 指标枚举类
 * @author yyluo
 *
 */
public enum TjGeneralIndex {

	PV("pv"),    //pv
    UV("uv"),   //uv
    LOGIN("login"), //登录数
    REGISTER("register"),  //注册数
    ORD("ord");      //订单数
    private String desc;
    private String code;

	private TjGeneralIndex(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

    
     
}
