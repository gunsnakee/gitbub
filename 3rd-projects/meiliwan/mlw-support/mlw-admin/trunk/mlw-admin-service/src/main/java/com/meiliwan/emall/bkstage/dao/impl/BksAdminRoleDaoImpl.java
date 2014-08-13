package com.meiliwan.emall.bkstage.dao.impl;

import com.meiliwan.emall.bkstage.bean.BksAdminRoleKey;
import com.meiliwan.emall.bkstage.dao.BksAdminRoleDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Sean on 13-5-24.
 */
@Repository
public class BksAdminRoleDaoImpl extends BaseDao<BksAdminRoleKey, BksAdminRoleKey> implements BksAdminRoleDao {
    @Override
    public String getMapperNameSpace() {
        return BksAdminRoleDao.class.getName();
    }
}
