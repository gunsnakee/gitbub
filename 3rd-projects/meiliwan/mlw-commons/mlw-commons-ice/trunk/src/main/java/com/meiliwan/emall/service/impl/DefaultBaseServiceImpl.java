package com.meiliwan.emall.service.impl;

import com.meiliwan.emall.service.BaseService;

abstract public class DefaultBaseServiceImpl implements BaseService {

	/**
	 * 
	 * @return 返回本处理器的消息类型，同spring中的bean的id值
	 */
	public String getMsgType() {
		return null;
	}
	
}
