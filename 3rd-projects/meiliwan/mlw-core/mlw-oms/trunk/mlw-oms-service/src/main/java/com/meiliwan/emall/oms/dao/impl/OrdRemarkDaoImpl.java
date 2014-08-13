package com.meiliwan.emall.oms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdRemark;
import com.meiliwan.emall.oms.dao.OrdRemarkDao;
import org.springframework.stereotype.Repository;

/**
 * Created by guangdetang on 13-10-30.
 */
@Repository
public class OrdRemarkDaoImpl extends BaseDao<Integer, OrdRemark> implements OrdRemarkDao{
    @Override
    public String getMapperNameSpace() {
        return OrdRemarkDao.class.getName();
    }
}
