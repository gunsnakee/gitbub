package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.dao.RequestLogDao;
import com.meiliwan.emall.monitor.statistics.RequestCount;

@Repository
public class RequestLogDaoImpl  extends BaseDao<Integer, RequestLog> implements RequestLogDao {

	@Override
	public String getMapperNameSpace() {
		return RequestLogDao.class.getName();
	}

//	private void redis(List<RequestLog> list){
//		CountRedis redis = CountRedis.getInstance();
//		redis.setRequestLogDao(this);
//		for (RequestLog requestLog : list) {
//			if(requestLog.isDAOType()){
//				redis.setRequestDaoCount(value);
//			}
//		}
//	}
	/**
	 * 批量插入
	 */
	public int insertBatch(List<RequestLog> list){
		
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
	public PagerControl<RequestLog> getPageByDTO(RequestLogVO dto, PageInfo pageInfo,
			String orderBySql) {
		PagerControl<RequestLog> pagerControl = new PagerControl<RequestLog>();
        pageInfo.startTime();
        List<RequestLog> list = null;
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
	
	 public int getCountByObj(RequestLogVO orderQuery, String whereSql) {
	        try {
	            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
	                    getShardParam(orderQuery != null ? orderQuery.getId() : null, getMapParams(orderQuery, null, whereSql, null), false));
	            return (Integer) selectOne;
	        } catch (Exception e) {
	            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
	        }
	    }

//	 @Override
//		public int getRequestCountByType(String type) {
//			// TODO Auto-generated method stub
//			RequestLog requestLog = new RequestLog();
//			requestLog.setType(type);
//			return this.getCountByObj(requestLog);
//			
//		}
	 
	@Override
	public List<RequestCount> getRequestCountGroupByType() {
		// TODO Auto-generated method stub
		 List<RequestCount> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getRequestCountGroupByType",
                    getShardParam(null, getMapParams(null, null, null, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getRequestCountGroupByType: {}",  e);
        }
		return list;
	}

	@Override
	public List<RequestCount> getRequestCountGroupByHour(RequestLogVO dto) {
		// TODO Auto-generated method stub
		 List<RequestCount> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getRequestCountGroupByHour",
            		getShardParam(dto.getId(), getMapParams(dto, null, null, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getRequestCountGroupByType: {}",  e);
        }
		return list;
	}
	
	@Override
	public List<RequestLog> getListByDTO(RequestLogVO dto) {
        List<RequestLog> list = new ArrayList<RequestLog>();
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(dto.getId(), getMapParams(dto, null, null, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListByDTO: {},{}", dto.toString(), e);
        }
        return list;
	}
	
	
}
