package com.meiliwan.emall.commons.log;

/**
 * @author leo
 * 
 * log子模块规范接口，所有的bizLog的 subModel都是实现这个接口的枚举
 */
public interface BizLogSubModel {
	
	/**
	 * @return 获取子模块名称
	 */
	String getSubModelName();
	
}
