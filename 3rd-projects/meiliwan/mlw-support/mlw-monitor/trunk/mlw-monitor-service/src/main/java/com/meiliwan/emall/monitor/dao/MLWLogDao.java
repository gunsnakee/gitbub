package com.meiliwan.emall.monitor.dao;

import java.util.List;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;

public interface MLWLogDao extends IDao<Integer, MLWLog>{

	PagerControl<MLWLog> getPage(MLWLogVO dto,
			PageInfo pageInfo, String order);

	List<MLWLog> getListByMLWLogVO(MLWLogVO dto);
    
	List<String> getAllApplication();
}