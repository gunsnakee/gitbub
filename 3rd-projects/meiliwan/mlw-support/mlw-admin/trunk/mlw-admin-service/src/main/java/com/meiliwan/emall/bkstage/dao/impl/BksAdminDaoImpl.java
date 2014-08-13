package com.meiliwan.emall.bkstage.dao.impl;

import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.dao.BksAdminDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 13-5-24.
 */
@Repository
public class BksAdminDaoImpl extends BaseDao<Integer, BksAdmin> implements BksAdminDao {

    @Override
    public String getMapperNameSpace() {
        return BksAdminDao.class.getName();
    }

    @Override
    public int updateFincePwd(int adminId, String pwd) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateStr();
        map.put("id", adminId);
        map.put("pwd", pwd);
        map.put("updateTime",updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateFincePwd", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateFincePwd: {},{}", adminId+"", e);
        }
    }
}
