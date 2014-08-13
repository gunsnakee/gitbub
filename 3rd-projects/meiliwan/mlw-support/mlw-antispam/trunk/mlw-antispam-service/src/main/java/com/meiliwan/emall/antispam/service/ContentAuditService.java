package com.meiliwan.emall.antispam.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.antispam.bean.ContentObject;


/**
 * 内容审核
 *    前期只对内容进行敏感词检查，后期再考虑用户封禁等功能
 * @author yj
 *  
 */
public interface ContentAuditService {
	
	/**
	 * 检查内容是否违规
	 * @param resultObj
	 * @param contentObject
	 * @return
	 */
	public void checkContent(JsonObject resultObj, ContentObject contentObject);
	
	/**
	 * 重载敏感词库
	 * @param resultObj
	 */
	public void reload(JsonObject resultObj);
}
