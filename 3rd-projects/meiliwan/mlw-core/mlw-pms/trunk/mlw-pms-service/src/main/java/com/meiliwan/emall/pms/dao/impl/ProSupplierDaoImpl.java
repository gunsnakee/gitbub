package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.pms.dao.ProSupplierDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProSupplierDaoImpl extends BaseDao<Integer,ProSupplier> implements ProSupplierDao{

    @Override
    public String getMapperNameSpace() {
        return ProSupplierDao.class.getName();
    }
}