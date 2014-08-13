package com.meiliwan.emall.oms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdAddr;
import com.meiliwan.emall.oms.dao.OrdAddrDao;
import org.springframework.stereotype.Repository;

/**
 * Created by guangdetang on 13-10-30.
 */
@Repository
public class OrdAddrDaoImpl extends BaseDao<String, OrdAddr> implements OrdAddrDao {
    @Override
    public String getMapperNameSpace() {
        return OrdAddrDao.class.getName();
    }
}
