package com.meiliwan.emall.service;

import com.google.gson.JsonArray;

import Ice.Current;

public interface SecurityInterceptor {

	/**
	 * 
	 * @param paramsObj 客户端传递过来的参数，同handleMsg的第一个参数
	 * @param __current 拥有客户端连接信息的对象
	 * @return 如果允许当前的请求执行handleMsg方法，则返回true；否则返回false
	 */
	boolean allowAccess(JsonArray jsonArr, Current __current);

}
