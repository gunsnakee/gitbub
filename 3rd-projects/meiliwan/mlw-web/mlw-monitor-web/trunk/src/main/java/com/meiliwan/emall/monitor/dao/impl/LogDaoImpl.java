package com.meiliwan.emall.monitor.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.Log;
import com.meiliwan.emall.monitor.bean.LogVO;
import com.meiliwan.emall.monitor.dao.LogDao;

@Repository
public class LogDaoImpl  extends BaseDao<Integer, Log> implements LogDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return LogDao.class.getName();
	}

	/**
	 * 批量插入
	 */
	public int insertBatch(List<Log> list){
		SqlSession session = null;
		
		try {
			session = getSqlSession();
            return session.insert(getMapperNameSpace() + ".insertBatch",
                    getShardParam( null, list, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insert: {}", list.toString(), e);
        }
	}
 
	@Override
	public PagerControl<Log> getPageByDTO(LogVO dto, PageInfo pageInfo,
			String orderBySql) {
		PagerControl<Log> pagerControl = new PagerControl<Log>();
        pageInfo.startTime();
        List<Log> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(dto.getId(), getMapParams(dto, pageInfo, null, orderBySql), false));
            total = getCountByObj(dto, null);
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getPageByDTO: {},{}", dto.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}
	
	 public int getCountByObj(LogVO orderQuery, String whereSql) {
	        try {
	            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
	                    getShardParam(orderQuery != null ? orderQuery.getId() : null, getMapParams(orderQuery, null, whereSql, null), false));
	            return (Integer) selectOne;
	        } catch (Exception e) {
	            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
	        }
	    }
}
