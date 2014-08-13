package com.meiliwan.emall.oms.dao.impl;

import java.util.*;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.dao.OrdDao;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;


@Repository
public class OrdDaoImpl extends BaseDao<String, Ord> implements OrdDao {

	@Override
	public String getMapperNameSpace() {
		return OrdDao.class.getName();
	}

	protected Map<String, Object> getMapParams(Object dto, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != dto) map.put("entity", dto);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    protected Map<String, Object> getMapParams(String name,Object value){
        Map<String, Object> map =null;
        if (null != value){
            map = new HashMap<String, Object>();
            map.put(name, value);
        }
        return map;
    }

	/* (non-Javadoc)
	 * @see com.meiliwan.emall.oms.dao.impl.OrdDao#getCountByObj(com.meiliwan.emall.oms.dto.OrderQueryDTO, java.lang.String)
	 */
	@Override
	public int getCountByObj(OrderQueryDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity,
                    getShardParam(orderQuery != null ? orderQuery.getOrderId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<Ord> getPagerByBean(Ord ord, PageInfo pageInfo, String whereSql, String orderBy) {
        PagerControl<Ord> pagerControl = new PagerControl<Ord>();
        pageInfo.startTime();
        List<Ord> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListByEntityAndPageInfo",
                    getShardParam(ord.getOrderId(), getMapParams(ord, pageInfo, whereSql, orderBy), false));
            total = (Integer)getSqlSession().selectOne(getMapperNameSpace() + ".getTotalByBean",
                    getShardParam(ord != null ? ord.getId() : null, getMapParams(ord, null, whereSql, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".getListByEntityAndPageInfo: {},{}", ord.toString(), e);
        }
        pageInfo.endTime();
        if(total>0){
            pageInfo.setTotalCounts(total);
        }
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
	public void updateOrdStatus(String orderId, String statusCode) {
		try {
			Ord ord = new Ord();
			ord.setOrderId(orderId);
			ord.setOrderStatus(statusCode);
            ord.setUpdateTime(new Date());
			getSqlSession().update(getMapperNameSpace() + ".updateOrdStatus", getShardParam(orderId, ord, true));
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".updateOrdStatus: {},{}", new String[]{orderId, statusCode}, e);
        }
	}

    @Override
    public void updateOrdStatus(String orderId, String statusCode, Date payTime) {
        try {
            Ord ord = new Ord();
            ord.setOrderId(orderId);
            ord.setOrderStatus(statusCode);
            ord.setUpdateTime(payTime);
            ord.setPayTime(payTime);
            getSqlSession().update(getMapperNameSpace() + ".updateOrdStatus", getShardParam(orderId, ord, true));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".updateOrdStatus: {},{}", new String[]{orderId, statusCode}, e);
        }
    }

    @Override
    public void updateOrdStatus(String orderId, String statusCode, String orderType) {
        try {
            Ord ord = new Ord();
            ord.setOrderId(orderId);
            ord.setOrderStatus(statusCode);
            ord.setUpdateTime(new Date());
            ord.setOrderType(orderType);
            getSqlSession().update(getMapperNameSpace() + ".updateOrdStatus", getShardParam(orderId, ord, true));
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".updateOrdStatus: {},{}", new String[]{orderId, statusCode}, e);
        }
    }

    @Override
    public int nextOrdSeq() {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".selectOrdSeq", getShardParam(null, null, true));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".selectOrdSeq:", e);
        }
    }

    @Override
    public List getOrdGroupCountByOrdStatus(OrderQueryDTO orderQuery) {

        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getOrdGroupCountByOrdStatus",
                        getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, null, null, null), false));

        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".getOrdGroupCountByOrdStatus: {}", orderQuery.toString(), e);
        }

    }

    @Override
	public PagerControl<OrdDTO> getOrdListByOrdStatus(OrderQueryDTO orderQuery, PageInfo pageInfo, String orderBy) {

		PagerControl<OrdDTO> pagerControl = new PagerControl<OrdDTO>();
        pageInfo.startTime();
        List<OrdDTO> list = new ArrayList<OrdDTO>();
        int total = 0;
        try {
            total = getCountByObj(orderQuery, null);
            //只有total大于0， 才有必要去走list
            if(total>0){
                pageInfo.setTotalCounts(total);
                list = getSqlSession().selectList(getMapperNameSpace() + ".getOrdListByOrdStatus",
                        getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, pageInfo, null, orderBy), false));
            }
        } catch (Exception e) {
        	throw new ServiceException("service-"+getMapperNameSpace()+".getOrdListByOrdStatus: {}", orderQuery.toString(), e);
        }

        pageInfo.endTime();

        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
	}

    @Override
    public int getCountByObjForFrontUser(OrderQueryDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getTotalByEntityForFrontUser",
                    getShardParam(orderQuery != null ? orderQuery.getOrderId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getTotalByEntityForFrontUser: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<OrdDTO> getOrdListByOrdStatusForFrontUser(OrderQueryDTO orderQuery, PageInfo pageInfo, String whereSql, String orderBy) {

        PagerControl<OrdDTO> pagerControl = new PagerControl<OrdDTO>();
        pageInfo.startTime();
        List<OrdDTO> list = new ArrayList<OrdDTO>();
        int total = 0;
        try {
            total = getCountByObjForFrontUser(orderQuery, whereSql);
            //只有total大于0， 才有必要去走list
            if(total>0){
                pageInfo.setTotalCounts(total);
                list = getSqlSession().selectList(getMapperNameSpace() + ".getOrdListByOrdStatusForFrontUser",
                        getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, pageInfo, whereSql, orderBy), false));
            }
        } catch (Exception e) {
            throw new ServiceException("service-"+getMapperNameSpace()+".getOrdListByOrdStatusForFrontUser: {}", orderQuery.toString(), e);
        }

        pageInfo.endTime();

        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    @Override
    public List<OrdDTO> getOrdDTOListByIds(List<String> orderIdList) {
        if (orderIdList == null || orderIdList.size() == 0) {
            return null;
        }
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getOrdDTOListByIds", getShardParam(orderIdList.get(0), orderIdList,false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdDTOListByIds: {}", orderIdList.toString(), e);
        }
    }

	@Override
	public List<Ord> getOrdAndOrdiByQuery(OrderQueryDTO orderQuery,
			PageInfo pageInfo) {
		if (orderQuery == null || pageInfo == null) {
            return null;
        }
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getOrdAndOrdiByQuery", 
            		getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, pageInfo,null, null),false));
        
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdAndOrdiByQuery: {}", orderQuery.toString(), e);
        }
	}
}