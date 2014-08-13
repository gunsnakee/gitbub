package com.meiliwan.emall.stock.dao.impl;


import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.stock.bean.ProStockLog;
import com.meiliwan.emall.stock.dao.ProStockLogDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProStockLogDaoImpl extends BaseDao<Integer, ProStockLog> implements ProStockLogDao {

    @Override
    public String getMapperNameSpace() {
        return ProStockLogDao.class.getName();
    }

    @Override
    public int addStockLogOnBatch(List<ProStockLog> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".addStockLogOnBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".addStockLogOnBatch: {}", list.toString(), e);
        }
    }
}
