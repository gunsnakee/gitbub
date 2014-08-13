package com.meiliwan.emall.exception;

import com.meiliwan.emall.commons.exception.BaseRuntimeException;

/**
 * 
 * @author lsf
 *
 */
public class FileTransException extends BaseRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public FileTransException(String code, String logMsg) {
		super(code, logMsg);
	}

}
