package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.ProStore;
import com.meiliwan.emall.pms.dao.ProStoreDao;
import org.springframework.stereotype.Repository;

/**
 * User: yiyou.luo
 * Date: 13-9-23
 */
@Repository
public class ProStoreDaoImpl extends BaseDao<Integer, ProStore> implements ProStoreDao {
    @Override
    public String getMapperNameSpace() {
        return ProStoreDao.class.getName();
    }


}
