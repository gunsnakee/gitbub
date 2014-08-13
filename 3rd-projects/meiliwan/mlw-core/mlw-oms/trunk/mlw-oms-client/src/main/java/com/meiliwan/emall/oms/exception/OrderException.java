package com.meiliwan.emall.oms.exception;

import com.meiliwan.emall.commons.exception.BaseRuntimeException;


public class OrderException extends BaseRuntimeException{


	private static final long serialVersionUID = 3247671035844921552L;

	public OrderException(String code, String logMsg, Object[] messageArgs,
                          String defaultFriendlyMessage) {
		super(code, logMsg, messageArgs, defaultFriendlyMessage);
	}

	public OrderException(String code, String logMsg, Object[] messageArgs) {
		super(code, logMsg, messageArgs);
	}

	public OrderException(String code, String logMsg,
                          String defaultFriendlyMessage) {
		super(code, logMsg, defaultFriendlyMessage);
	}

	public OrderException(String code, String logMsg, Throwable cause,
                          Object[] messageArgs, String defaultFriendlyMessage) {
		super(code, logMsg, cause, messageArgs, defaultFriendlyMessage);
	}

	public OrderException(String code, String logMsg, Throwable cause,
                          Object[] messageArgs) {
		super(code, logMsg, cause, messageArgs);
	}

	public OrderException(String code, String logMsg, Throwable cause,
                          String defaultFriendlyMessage) {
		super(code, logMsg, cause, defaultFriendlyMessage);
	}

	public OrderException(String code, String logMsg, Throwable cause) {
		super(code, logMsg, cause);
	}

	public OrderException(String code, String[] logMsg, Throwable cause) {
		super(code, logMsg[0], cause);
	}

	public OrderException(String code, String logMsg) {
		super(code, logMsg);
	}

	public OrderException(String code, String[] logMsg) {
		super(code, logMsg[0]);
	}

	public OrderException(String code, Throwable cause, Object[] messageArgs,
                          String defaultFriendlyMessage) {
		super(code, cause, messageArgs, defaultFriendlyMessage);
	}

	public OrderException(String code, Throwable cause, Object[] messageArgs) {
		super(code, cause, messageArgs);;
	}

	public OrderException(String code, Throwable cause,
                          String defaultFriendlyMessage) {
		super(code, cause, defaultFriendlyMessage);
	}

	public OrderException(String code, Throwable cause) {
		super(code, cause);
	}

	public OrderException(String logMsg) {
		super(logMsg);
	}

	public OrderException(Throwable cause) {
		super(cause);
	}

}
