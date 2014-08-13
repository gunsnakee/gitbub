package com.meiliwan.emall.tongji.task.dao.oms.impl;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.Ordi;
import com.meiliwan.emall.tongji.bean.oms.OmsProSales;
import com.meiliwan.emall.tongji.task.dao.oms.OrdiDao;
import com.meiliwan.emall.tongji.dto.oms.OrdQueryDTO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 上午10:01
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class OrdiDaoImpl extends BaseDao<String,Ordi> implements OrdiDao {

    private static final String SHARD_NAME = "OmsShard";

    @Override
    public String getShardName() {
        return SHARD_NAME;
    }

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

    @Override
    public List<OmsProSales> getOmsProSalesBy(OrdQueryDTO ordQueryDTO) {
        if(ordQueryDTO == null) return null;
        List<OmsProSales> list = null;
        try{
            list = getSqlSession().selectList(getMapperNameSpace() +".getOmsProSalesByEntity",getShardParam(null,getMapParams(ordQueryDTO,null,null,null),false));
        }catch (Exception e){
            throw new ServiceException("service-"+getMapperNameSpace()+".getOmsProSalesBy: {},{}", ordQueryDTO.toString(), e);
        }
       return list;
    }
}
