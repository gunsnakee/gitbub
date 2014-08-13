package com.meiliwan.emall.union.cps;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CPSQueryHandler {
	
	boolean validation(HttpServletRequest request);
	
	String query(HttpServletRequest request,HttpServletResponse response);
}
