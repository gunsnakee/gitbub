package com.meiliwan.emall.bkstage.dao.impl;

import com.meiliwan.emall.bkstage.bean.BksRole;
import com.meiliwan.emall.bkstage.dao.BksRoleDao;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Sean on 13-5-24.
 */
@Repository
public class BksRoleDaoImpl extends BaseDao<Integer, BksRole> implements BksRoleDao {
    @Override
    public String getMapperNameSpace() {
        return BksRoleDao.class.getName();
    }
}
