package com.meiliwan.emall.oms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.RetApply;
import com.meiliwan.emall.oms.dao.RetApplyDao;
import com.meiliwan.emall.oms.dto.RetApplyDTO;

import org.springframework.stereotype.Repository;

/**
 * Created by nibin on 13-5-23.
 */
@Repository
public class RetApplyDaoImpl extends BaseDao<String, RetApply> implements RetApplyDao {

	@Override
	public String getMapperNameSpace() {
		return RetApplyDao.class.getName();
	}

	protected Map<String, Object> getMapParams(Object dto, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != dto) map.put("entity", dto);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }
	public int getCountByObj(RetApplyDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
                    getShardParam(orderQuery != null ? orderQuery.getRetordItemId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }
	
	@Override
	public PagerControl<RetApply> getPagerByDTO(RetApplyDTO entity,
			PageInfo pageInfo, Object object, String orderBySql) {
		PagerControl<RetApply> pagerControl = new PagerControl<RetApply>();
        pageInfo.startTime();
        List<RetApply> list = null;
        int total = 0;
        try {
			list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
	        			getShardParam(entity.getRetordItemId(), getMapParams(entity, pageInfo, null, orderBySql), false));
	        total = getCountByObj(entity, null);
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".getPagerByDTO: {},{}", entity.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}

	@Override
	public List<RetApply> getApplyListByOrdItemIds(List<String> oItemIds) {
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("ids", oItemIds);
			List<RetApply> list = getSqlSession().selectList(getMapperNameSpace() + ".getApplyListByOrdItemIds",getShardParam(null, map, false));
			return list;
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".getApplyListByOrdItemIds: {}", oItemIds.toString(), e);
        }
	}
	
}
