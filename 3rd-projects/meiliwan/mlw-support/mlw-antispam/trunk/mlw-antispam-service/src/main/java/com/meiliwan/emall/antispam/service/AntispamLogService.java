package com.meiliwan.emall.antispam.service;

import java.util.Date;

import com.google.gson.JsonObject;
import com.meiliwan.emall.antispam.bean.AntispamLog;
import com.meiliwan.emall.commons.PageInfo;

/**
 * 客户端获取内容审核结果
 * @author yj
 *
 */
public interface AntispamLogService {
	
	/**
	 * 根据条件查询审核结果，分页显示
	 * @param resultObj
	 * @param antispamLog			包含查询参数的实例
	 * @param startTime				开始时间
	 * @param endTime				结束时间
	 * @param pageInfo				页面信息
	 */
	public void getPageList(JsonObject resultObj, AntispamLog antispamLog, Date startTime, Date endTime, PageInfo pageInfo);
}
