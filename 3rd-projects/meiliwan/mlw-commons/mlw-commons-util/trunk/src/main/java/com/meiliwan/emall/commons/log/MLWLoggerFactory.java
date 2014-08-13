package com.meiliwan.emall.commons.log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLWLoggerFactory {

	public static MLWLogger getLogger(String name) {
		Logger logger =   LoggerFactory.getLogger(name);
		return new MLWLogger(logger);
	  }

	  /**
	   * Return a logger named corresponding to the class passed as parameter, using
	   * the statically bound {@link ILoggerFactory} instance.
	   * 
	   * @param clazz
	   *          the returned logger will be named after clazz
	   * @return logger
	   */
	  public static MLWLogger getLogger(Class clazz) {
		  Logger logger =   LoggerFactory.getLogger(clazz.getName());
		  return new MLWLogger(logger);
			
	  }
}
