package com.meiliwan.emall.commons.exception.model;

/**
 * 框架用得到的一些常量
 * 
 * @author matrixgan 
 */
public interface ConfigConstant {
	/**
	 * 异常处理配置对应的Spring bean 的名字，
	 */
	public static final String EXCEPTION_HANDLE_SETTING_BEAN_NAME = "exceptionHandleSetting";
	/**
	 * JmsTemplate对应的Spring bean 的名字，
	 */
	public static final String EXCEPTION_HANDLE_JMSTEMPLATE_BEAN_NAME = "exJmsTemplate";
	/**
	 * 换行符
	 */
	public static final String LINE_SEPARATOR=System.getProperty("line.separator");
}
