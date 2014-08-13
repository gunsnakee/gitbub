package com.meiliwan.emall.tongji.task.dao.pms.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.SimpleProduct;
import com.meiliwan.emall.tongji.bean.oms.OmsProSales;
import com.meiliwan.emall.tongji.task.dao.pms.ProProductDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ProProductDaoImpl extends BaseDao<Integer, SimpleProduct> implements ProProductDao {

    private static final String SHARD_NAME = "PmsShard";

    @Override
    public String getShardName() {
        return SHARD_NAME;
    }

    @Override
    public String getMapperNameSpace() {
        return ProProductDao.class.getName();
    }

    @Override
    public List<OmsProSales> getOmsProSalesBy(List<Integer> list) {
        if(list == null || list.isEmpty()) return null;
        List<OmsProSales> result = null;
        try {
           result = getSqlSession().selectList(getMapperNameSpace()+".getOmsProSalesBy",getShardParam(list.get(0),list,false));
        }catch (Exception e){
            throw new ServiceException("service-"+getMapperNameSpace()+".getOmsProSalesBy: {},{}", list.toString(), e);
        }

        return result;
    }
}
