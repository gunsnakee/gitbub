package com.meiliwan.emall.oms.dao.impl;

import java.util.*;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import  com.meiliwan.emall.oms.constant.RetFlag;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.oms.dao.OrdiDao;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;
import com.meiliwan.emall.oms.dto.OrdiDTO;

import com.meiliwan.emall.oms.dto.SaleProOrdDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;


/**
 * Created by xiong.yu on 13-5-23.
 */
@Repository
public class OrdiDaoImpl extends BaseDao<String, Ordi> implements OrdiDao {

    @Override
    public String getMapperNameSpace() {
        return OrdiDao.class.getName();
    }

    protected Map<String, Object> getMapParams(Object dto, PageInfo pageInfo, String whereSql, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != dto) map.put("entity", dto);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != whereSql) map.put("whereSql", whereSql);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    public int getCountByObj(OrderQueryDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getTotalByEntityJoinPay",
                    getShardParam(orderQuery != null ? orderQuery.getOrderId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByObj: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public void updateOrdIStatus(String orderItemId, String statusCode) {
        try {
            Ordi ordi = new Ordi();
            ordi.setOrderItemId(orderItemId);
            ordi.setOrderItemStatus(statusCode);
            ordi.setUpdateTime(new Date());
            getSqlSession().update(getMapperNameSpace() + ".updateOrdIStatus", getShardParam(orderItemId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateOrdIStatus: {},{}", new String[]{orderItemId, statusCode}, e);
        }
    }

    @Override
    public void updateOrdIStatus(String orderItemId, String statusCode, String orderType) {
        try {
            Ordi ordi = new Ordi();
            ordi.setOrderItemId(orderItemId);
            ordi.setOrderItemStatus(statusCode);
            ordi.setUpdateTime(new Date());
            ordi.setOrderType(orderType);
            getSqlSession().update(getMapperNameSpace() + ".updateOrdIStatus", getShardParam(orderItemId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateOrdIStatus: {},{}", new String[]{orderItemId, statusCode}, e);
        }
    }

    @Override
    public void updateAllOrdIStatus(String orderId, String statusCode) {
        try {
            Ordi ordi = new Ordi();
            ordi.setOrderId(orderId);
            ordi.setOrderItemStatus(statusCode);
            ordi.setUpdateTime(new Date());
            getSqlSession().update(getMapperNameSpace() + ".updateAllOrdIStatus", getShardParam(orderId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateAllOrdIStatus: {},{}", new String[]{orderId, statusCode}, e);
        }
    }

    @Override
    public void updateAllOrdIStatus(String orderId, String statusCode, Date payTime) {
        try {
            Ordi ordi = new Ordi();
            ordi.setOrderId(orderId);
            ordi.setOrderItemStatus(statusCode);
            ordi.setUpdateTime(payTime);
            ordi.setPayTime(payTime);
            getSqlSession().update(getMapperNameSpace() + ".updateAllOrdIStatus", getShardParam(orderId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateAllOrdIStatus: {},{}", new String[]{orderId, statusCode}, e);
        }
    }

    @Override
    public PagerControl<OrdiDTO> getOrdIListByOrdIStatus(OrderQueryDTO orderQuery, PageInfo pageInfo, String orderBy) {

        PagerControl<OrdiDTO> pagerControl = new PagerControl<OrdiDTO>();
        pageInfo.startTime();
        List<OrdiDTO> list = null;
        int total = 0;
        try {
            if (!StringUtils.isEmpty(orderQuery.getOrderId())) {
                list = getSqlSession().selectList(getMapperNameSpace() + ".getDTOByOrderQueryDTO",
                        getShardParam(orderQuery.getOrderId(), orderQuery, false));
                //对于主线状态的订单列表功能来说，依据订单ID做查询，如果有值，则返回的total应该永远=1    modify by yuxiong 2013-10-28
                if(list!=null && list.size() > 0){
                    total = 1;
                }
            } else {
                list = getSqlSession().selectList(getMapperNameSpace() + ".getOrdIListByOrdIStatus",
                        getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, pageInfo, null, orderBy), false));
                total = getCountByObj(orderQuery, null);
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIListByOrdIStatus: {},{}", orderQuery.toString(), e);
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
    public Ordi getOrdIOneByOrdId(String orderId) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace() + ".getOneByOrderId",
                    getShardParam(orderId, orderId, false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIOneByOrdId: {}", orderId, e);
        }
    }

    @Override
    public Ordi getOrdIOneByOrdId(String orderId, boolean isMainDS) {
        try {
            return getSqlSession().selectOne(getMapperNameSpace() + ".getOneByOrderId",
                    getShardParam(orderId, orderId, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIOneByOrdId: {}", orderId, e);
        }
    }

    @Override
    public List<Ordi> getOrdIListByOrdId(String orderId) {
        return getOrdIListByOrdId(orderId, false);
    }

    @Override
    public List<Ordi> getOrdIListByOrdId(String orderId, boolean isMainDS) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getByOrderId",
                    getShardParam(orderId, orderId, isMainDS));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIListByOrdId: {}", orderId, e);
        }
    }

    @Override
    public List<OrdiDTO> getOrdIDTOListByOrdId(String orderId) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getDTOByOrderId",
                    getShardParam(orderId, orderId, false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIDTOListByOrdId: {},{}", orderId, e);
        }
    }

    @Override
    public List<OrdiDTO> getOrdIDTOListByOrdIds(List<String> orderIds) {
        try {
            return getSqlSession().selectList(getMapperNameSpace() + ".getDTOByOrderIds", getShardParam(orderIds.get(0), orderIds, false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIDTOListByOrdIds: {},{}", orderIds.toString(), e);
        }
    }

    public int getCountByStatusType(OrderQueryDTO orderQuery, String whereSql) {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getTotalByStatusType",
                    getShardParam(orderQuery != null ? orderQuery.getOrderId() : null, getMapParams(orderQuery, null, whereSql, null), false));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountByStatusType: {},{}", new String[]{orderQuery == null ? "" : orderQuery.toString(), whereSql}, e);
        }
    }

    @Override
    public PagerControl<OrdiDTO> getOrdIListByStatusType(OrderQueryDTO orderQuery, PageInfo pageInfo, String orderBy) {

        PagerControl<OrdiDTO> pagerControl = new PagerControl<OrdiDTO>();
        pageInfo.startTime();
        List<OrdiDTO> list = new ArrayList<OrdiDTO>();
        int total = 0;
        try {
            if (!StringUtils.isEmpty(orderQuery.getOrderId())) {
                list = getSqlSession().selectList(getMapperNameSpace() + ".getDTOByOrderIdAndOISState",
                        getShardParam(orderQuery.getOrderId(), orderQuery, false));
                total = list.size();
            } else {
                total = getCountByStatusType(orderQuery, null);
                //只有total大于0， 才有必要去走list
                if(total>0){
                    list = getSqlSession().selectList(getMapperNameSpace() + ".getOrdIListByStatusType",
                            getShardParam(orderQuery.getOrderId(), getMapParams(orderQuery, pageInfo, null, orderBy), false));
                }
            }
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getOrdIListByStatusType: {},{}", orderQuery.toString(), e);
        }

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    //获得已卖出商品列表------------------------------------------------------------------
    @Override
    public PagerControl<SaleProOrdDTO> getSaleProductPager(SaleProOrdDTO saleProOrdDTO, PageInfo pageInfo, String orderBy) {
        PagerControl<SaleProOrdDTO> pagerControl = new PagerControl<SaleProOrdDTO>();
        pageInfo.startTime();
        List<SaleProOrdDTO> list = null;
        int total = 0;
        list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo + "SaleProduct",
                getShardParam(null, getMapParamsSaleProduct(saleProOrdDTO, pageInfo, orderBy), false));
        total = getCountBySaleProduct(saleProOrdDTO);

        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }

    private Map<String, Object> getMapParamsSaleProduct(SaleProOrdDTO saleProOrdDTO, PageInfo pageInfo, String orderBySql) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null != saleProOrdDTO) map.put("entity", saleProOrdDTO);
        if (null != pageInfo) map.put("pageInfo", pageInfo);
        if (null != orderBySql) map.put("orderBy", orderBySql);
        return map;
    }

    private int getCountBySaleProduct(SaleProOrdDTO saleProOrdDTO) {
        Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + getTotalByEntity + "SaleProduct",
                getShardParam(null, getMapParamsSaleProduct(saleProOrdDTO, null, null), false));
        return (Integer) selectOne;
    }
    //-------------------------------------------------------------------------------------------


    @Override
    public void updateOrdIRetFlag(String orderItemId, short retFlag) {
        try {
            Ordi ordi = new Ordi();
            ordi.setOrderItemId(orderItemId);
            ordi.setRetFlag(retFlag);
            ordi.setUpdateTime(new Date());
            getSqlSession().update(getMapperNameSpace() + ".updateOrdIRetFlag", getShardParam(orderItemId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateOrdIRetFlag: {},{}", new String[]{orderItemId, Short.toString(retFlag)}, e);
        }
    }

    /**
     * 退换货拒绝时调用,
     */
	@Override
	public void updateOrdIRetFlag(String orderItemId, String statusCode, RetFlag retFlag) {
		// TODO Auto-generated method stub
		try {
            Ordi ordi = new Ordi();
            ordi.setOrderItemId(orderItemId);
            ordi.setOrderItemStatus(statusCode);
            ordi.setRetFlag(retFlag.getFlag());
            ordi.setUpdateTime(new Date());
            getSqlSession().update(getMapperNameSpace() + ".updateOrdIRetFlag", getShardParam(orderItemId, ordi, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateOrdIRetFlag: {},{}", new String[]{orderItemId,retFlag.name()}, e);
        }
	}

}
