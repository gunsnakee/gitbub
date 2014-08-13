package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;
import com.meiliwan.emall.monitor.dao.MLWLogDao;

@Repository
public class MLWLogDaoImpl extends BaseDao<Integer, MLWLog> implements MLWLogDao{

	@Override
	public String getMapperNameSpace() {
		return MLWLogDao.class.getName();
	}

	@Override
	public PagerControl<MLWLog> getPage(MLWLogVO dto,
			PageInfo pageInfo, String orderBySql) {
		PagerControl<MLWLog> pagerControl = new PagerControl<MLWLog>();
        pageInfo.startTime();
        List<MLWLog> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(dto.getId(), getMapParams(dto, pageInfo, null, orderBySql), false));
            total = getCountByObj(dto, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPage: {},{}", dto.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}
    
	
	
	 public int getCountByObj(MLWLogVO query, String whereSql) {
	        try {
	            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
	                    getShardParam(query != null ? query.getId() : null, getMapParams(query, null, whereSql, null), false));
	            return (Integer) selectOne;
	        } catch (Exception e) {
	            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{query == null ? "" : query.toString(), whereSql}, e);
	        }
	    }
	 
	 @Override
	public List<MLWLog> getListByMLWLogVO(MLWLogVO dto) {
        List<MLWLog> list = new ArrayList<MLWLog>();
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(dto.getId(), getMapParams(dto, null, null, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPage: {},{}", dto.toString(), e);
        }

        return list;
	}

	@Override
	public List<String> getAllApplication() {
		 List<String> list = new ArrayList<String>();
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getAllApplication",
                    getShardParam(null, getMapParams(null, null, null, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getAllApplication: {}",e);
        }
        for (int i = 0; i < list.size(); i++) {
	        	if(StringUtil.checkNull(list.get(i))){
	        		list.remove(i);
	        	}
		}
        return list;
	}
	 
}