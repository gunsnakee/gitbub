package com.meiliwan.emall.oms.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdLogistics;
import com.meiliwan.emall.oms.dao.OrdLogisticsDao;

/**
 * Created by guangdetang on 13-8-26.
 */
@Repository
public class OrdLogisticsDaoImpl extends BaseDao<Integer, OrdLogistics> implements OrdLogisticsDao{
    @Override
    public String getMapperNameSpace() {
        return OrdLogisticsDao.class.getName();
    }

	
}
