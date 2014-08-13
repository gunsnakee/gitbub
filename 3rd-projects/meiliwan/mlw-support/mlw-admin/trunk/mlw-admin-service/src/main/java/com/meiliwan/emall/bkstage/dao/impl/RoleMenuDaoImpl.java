package com.meiliwan.emall.bkstage.dao.impl;

import com.meiliwan.emall.bkstage.bean.BksRoleMenuKey;
import com.meiliwan.emall.bkstage.dao.BksRoleMenuDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Sean on 13-5-24.
 */
@Repository
public class RoleMenuDaoImpl extends BaseDao<BksRoleMenuKey, BksRoleMenuKey> implements BksRoleMenuDao {
    @Override
    public String getMapperNameSpace() {
        return BksRoleMenuDao.class.getName();
    }
}
