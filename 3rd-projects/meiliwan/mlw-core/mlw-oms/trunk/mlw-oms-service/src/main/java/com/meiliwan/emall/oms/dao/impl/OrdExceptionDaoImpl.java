package com.meiliwan.emall.oms.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdException;
import com.meiliwan.emall.oms.dao.OrdExceptionDao;
import com.meiliwan.emall.oms.dto.OrdExceptionDTO;

import org.springframework.stereotype.Repository;

@Repository
public class OrdExceptionDaoImpl extends BaseDao<Integer, OrdException> implements OrdExceptionDao {

	@Override
	public String getMapperNameSpace() {
		return OrdExceptionDao.class.getName();
	}

	 private Map<String, Object> getMapParams(OrdExceptionDTO entity, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != entity) map.put("entity", entity);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }
	@Override
	public PagerControl<OrdException> getPagerByDTO(OrdExceptionDTO dto, PageInfo pageInfo, String whereSql,String orderBy) {

		PagerControl<OrdException> pagerControl = new PagerControl<OrdException>();
        pageInfo.startTime();
        List<OrdException> list = null;
        int total = 0;
        try {
			list = getSqlSession().selectList(getMapperNameSpace() + ".getListByEntityAndPageInfo",
	        			getShardParam(dto.getOrderId(), getMapParams(dto, pageInfo, null, orderBy), false));
	        total = getCountByObj(dto, null);
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".getTotalByEntity: {},{}", dto.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}
	
    public int getCountByObj(OrdExceptionDTO entity, String whereSql) {

        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
                    getShardParam(entity != null ? entity.getId() : null, getMapParams(entity, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{entity == null ? "" : entity.toString(), whereSql}, e);
        }
    }
}
