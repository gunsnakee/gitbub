package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProBrandCategoryKey;
import com.meiliwan.emall.pms.dao.ProBrandCategoryDao;
import org.springframework.stereotype.Repository;

@Repository
public class ProBrandCategoryDaoImpl extends BaseDao<ProBrandCategoryKey,ProBrandCategoryKey> implements ProBrandCategoryDao{
    @Override
    public String getMapperNameSpace() {
        return ProBrandCategoryDao.class.getName();
    }
}