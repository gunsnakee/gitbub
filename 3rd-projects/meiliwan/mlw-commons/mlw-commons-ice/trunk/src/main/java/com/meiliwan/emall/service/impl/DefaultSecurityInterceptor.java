package com.meiliwan.emall.service.impl;

import Ice.Current;

import com.google.gson.JsonArray;
import com.meiliwan.emall.service.SecurityInterceptor;

public class DefaultSecurityInterceptor implements SecurityInterceptor {

	@Override
	public boolean allowAccess(JsonArray jsonArr, Current __current) {
		// TODO Auto-generated method stub
		return true;
	}


}
