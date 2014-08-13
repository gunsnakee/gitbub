package com.meiliwan.emall.commons.exception;


public class JedisClientException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2841616768865276154L;

	public JedisClientException(String code, String logMsg, Throwable cause) {
		super(code, logMsg, cause);
	}

    public JedisClientException(String code, String logMsg) {
        super(code, logMsg);
    }

    public JedisClientException(JedisClientExceptionCode code, String logMsg, Throwable cause) {
        super(code.name(), logMsg, cause);
    }

    public JedisClientException(JedisClientExceptionCode code, String logMsg) {
        super(code.name(), logMsg);
    }

    public JedisClientException(JedisClientExceptionCode code) {
        super(code.name());
    }

	
}
