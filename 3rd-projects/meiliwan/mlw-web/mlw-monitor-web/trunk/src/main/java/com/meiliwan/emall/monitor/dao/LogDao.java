package com.meiliwan.emall.monitor.dao;

import java.util.List;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.monitor.bean.Log;
import com.meiliwan.emall.monitor.bean.LogVO;

public interface LogDao extends IDao<Integer, Log> {

	int insertBatch(List<Log> list);

	PagerControl<Log> getPageByDTO(LogVO dto, PageInfo pageInfo, String string);
	
}