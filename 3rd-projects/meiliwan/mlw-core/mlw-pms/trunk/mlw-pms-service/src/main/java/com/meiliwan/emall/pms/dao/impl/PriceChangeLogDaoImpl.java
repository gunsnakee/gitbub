package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.PriceChangeLog;
import com.meiliwan.emall.pms.dao.PriceChangeLogDao;
import org.springframework.stereotype.Repository;

/**
 * User: wuzixin
 * Date: 14-2-20
 * Time: 下午5:25
 */
@Repository
public class PriceChangeLogDaoImpl extends BaseDao<Integer,PriceChangeLog> implements PriceChangeLogDao{
    @Override
    public String getMapperNameSpace() {
        return PriceChangeLogDao.class.getName();
    }
}
