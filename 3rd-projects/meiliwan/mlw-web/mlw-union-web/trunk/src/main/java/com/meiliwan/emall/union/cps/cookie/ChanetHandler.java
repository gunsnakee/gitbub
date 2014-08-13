package com.meiliwan.emall.union.cps.cookie;

import javax.servlet.http.HttpServletRequest;

import com.meiliwan.emall.union.cps.CPSHandler;


public class ChanetHandler extends CPSHandler {

	@Override
	public String getMcps(HttpServletRequest request) {
		return request.getParameter("id");
	}

	
	/**
	 * 获取推广公司的名称
	 * */
	@Override
	public String getCPSType(){
		return "channet";
	}
}
