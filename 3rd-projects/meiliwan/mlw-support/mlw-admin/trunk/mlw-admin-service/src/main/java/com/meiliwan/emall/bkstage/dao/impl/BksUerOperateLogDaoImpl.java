package com.meiliwan.emall.bkstage.dao.impl;

import com.meiliwan.emall.bkstage.bean.BksUserOperateLog;
import com.meiliwan.emall.bkstage.dao.BksAdminDao;
import com.meiliwan.emall.bkstage.dao.BksUserOperateLogDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Sean on 13-5-24.
 */
@Repository
public class BksUerOperateLogDaoImpl extends BaseDao<Integer, BksUserOperateLog> implements  BksUserOperateLogDao {

    @Override
    public String getMapperNameSpace() {
        return BksUserOperateLogDao.class.getName();
    }



}
