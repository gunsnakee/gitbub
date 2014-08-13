package com.meiliwan.emall.pojo;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public class MethodInfo {

	private Method method;
	private Type[] paramTypes;
	private Class[] paramClzs;
	
	public MethodInfo(Method method) {
		this.method = method;
	}
	
	public MethodInfo(Method method, Type[] paramTypes, Class[] paramClzs) {
		this(method);
		this.paramTypes = paramTypes;
		this.paramClzs = paramClzs;
	}

	public Method getMethod() {
		return method;
	}

	public Type[] getParamTypes() {
		return paramTypes;
	}

	public Class[] getParamClzs() {
		return paramClzs;
	}
	
}
