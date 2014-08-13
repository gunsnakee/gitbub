package com.meiliwan.emall.oms.dao;

import com.meiliwan.emall.annotation.Param;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.oms.bean.Ord;
import com.meiliwan.emall.oms.dto.OrdDTO;
import com.meiliwan.emall.oms.dto.OrderQueryDTO;

import java.util.Date;
import java.util.List;

public interface OrdDao extends IDao<String, Ord> {

    /**
     * 根据总线状态查询订单数据
     * @param orderQuery
     * @return
     */
    PagerControl<OrdDTO> getOrdListByOrdStatus(OrderQueryDTO orderQuery, PageInfo pageInfo, String orderBy);

    void updateOrdStatus(String orderId, String statusCode);

    void updateOrdStatus(String orderId, String statusCode, Date payTime);

    void updateOrdStatus(String orderId, String statusCode, String orderType);

    int nextOrdSeq();

    int getCountByObj(OrderQueryDTO orderQuery, String whereSql);

    PagerControl<Ord> getPagerByBean(Ord ord, PageInfo pageInfo, String whereSql, String orderBy);

    List getOrdGroupCountByOrdStatus(OrderQueryDTO orderQuery);

    int getCountByObjForFrontUser(OrderQueryDTO orderQuery, String whereSql);

    /**
     * 前台订单个人中心/申请退换货列表
     * @param orderQuery
     * @param pageInfo
     * @param whereSql
     * @param orderBy
     * @return
     */
    PagerControl<OrdDTO> getOrdListByOrdStatusForFrontUser(OrderQueryDTO orderQuery, PageInfo pageInfo, String whereSql, String orderBy);

    /**
     * 根据OrderId list查询OrdDTO list
     * @return
     */
    List<OrdDTO> getOrdDTOListByIds(List<String> orderIdList);

	List<Ord> getOrdAndOrdiByQuery(OrderQueryDTO orderQuery, PageInfo pageInfo);
}