package com.meiliwan.emall.commons.exception;

import com.meiliwan.emall.commons.bean.ErrorPageCode;

public class WebRuntimeException extends BaseRuntimeException{

	public WebRuntimeException(String logMsg) {
		super(logMsg);
	}
	
	public WebRuntimeException(ErrorPageCode errorPageCode) {
        super(errorPageCode.name());
        this.code = errorPageCode.name();
    }

	/**
	 * 
	 */
	private static final long serialVersionUID = 342893193104702226L;
	
	

}
