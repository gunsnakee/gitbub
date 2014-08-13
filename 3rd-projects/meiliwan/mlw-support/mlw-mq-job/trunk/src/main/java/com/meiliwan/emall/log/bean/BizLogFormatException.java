package com.meiliwan.emall.log.bean;

import com.meiliwan.emall.commons.exception.BaseException;

public class BizLogFormatException extends BaseException {
	
	private static final long serialVersionUID = -3562104828126524066L;

	public BizLogFormatException(String code, String logMsg, Throwable cause) {
		super(code, logMsg, cause);
	}
	
}
