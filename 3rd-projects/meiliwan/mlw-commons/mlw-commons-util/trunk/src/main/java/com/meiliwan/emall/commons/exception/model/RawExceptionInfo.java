package com.meiliwan.emall.commons.exception.model;

import java.io.Serializable;

/**
 * 原始异常信息类
 * 
 * 封装了没有提取前的原始异常类Throwable
 * 
 * @author matrixgan
 *
 */
public class RawExceptionInfo implements Serializable {
	private static final long serialVersionUID = 1498351624078388956L;
	String interceptedClass;
	String interceptedInterface;
	Object[] parameterValue;
	private Throwable throwable;
	
	public RawExceptionInfo(String interceptedClass,String interceptedInterface,Object[] parameterValue, Throwable throwable) {		
		this.interceptedClass = interceptedClass;
		this.interceptedInterface = interceptedInterface;
		this.parameterValue = parameterValue;
		this.throwable = throwable;
	}
	
	public RawExceptionInfo(Throwable throwable){
		this.throwable = throwable;
	}
		
	public String getInterceptedClass() {
		return interceptedClass;
	}

	public void setInterceptedClass(String interceptedClass) {
		this.interceptedClass = interceptedClass;
	}

	public String getInterceptedInterface() {
		return interceptedInterface;
	}

	public void setInterceptedInterface(String interceptedInterface) {
		this.interceptedInterface = interceptedInterface;
	}

	public Object[] getParameterValue() {
		return parameterValue;
	}

	public void setParameterValue(Object[] parameterValue) {
		this.parameterValue = parameterValue;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}		
}
