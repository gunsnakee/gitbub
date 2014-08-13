package com.meiliwan.search.common;

/**
 * @author liangguoning
 * @version 1.0
 * 
 * 
 *          知道明确的错误原因的异常
 */
public class MlwException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public int errorCode=502;
	public MlwException(String message) {
		super(message);
	}

	public MlwException(String message, Throwable cause) {
		super(message, cause);
	}
	public MlwException(String message,int errcode) {
		super(message);
		this.errorCode=errcode;
	}

	public MlwException(String message, Throwable cause,int errcode) {
		super(message, cause);
	    this.errorCode=errcode;
	}
	
}
