package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProStoreCategory;
import com.meiliwan.emall.pms.dao.ProStoreCategoryDao;
import org.springframework.stereotype.Repository;

/**
 * User: yiyou.luo
 * Date: 13-9-23
 */
@Repository
public class ProStoreCategoryDaoImpl extends BaseDao<Integer, ProStoreCategory> implements ProStoreCategoryDao {
    @Override
    public String getMapperNameSpace() {
        return ProStoreCategoryDao.class.getName();
    }
}
