package com.meiliwan.emall.oms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdDesc;
import com.meiliwan.emall.oms.dao.OrdDescDao;
import org.springframework.stereotype.Repository;

/**
 * Created by guangdetang on 13-12-30.
 */
@Repository
public class OrdDescDaoImpl extends BaseDao<String, OrdDesc> implements OrdDescDao {
    @Override
    public String getMapperNameSpace() {
        return OrdDescDao.class.getName();
    }
}
