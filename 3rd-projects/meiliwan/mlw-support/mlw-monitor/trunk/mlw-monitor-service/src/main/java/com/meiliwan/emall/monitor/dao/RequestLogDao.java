package com.meiliwan.emall.monitor.dao;

import java.util.List;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.statistics.RequestCount;

public interface RequestLogDao extends IDao<Integer, RequestLog> {
	
	int insertBatch(List<RequestLog> list);

	PagerControl<RequestLog> getPageByDTO(RequestLogVO dto, PageInfo pageInfo, String string);
	
	List<RequestCount> getRequestCountGroupByType();

	List<RequestCount> getRequestCountGroupByHour(RequestLogVO dto);

	List<RequestLog> getListByDTO(RequestLogVO dto);

	//int getRequestCountByType(String type);
}