package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.ExamResult;
import com.meiliwan.emall.sp2.dao.ExamResultDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-4-24
 * Time: 下午5:07
 */
@Repository
public class ExamResultDaoImpl extends BaseDao<Integer, ExamResult> implements ExamResultDao {
    @Override
    public String getMapperNameSpace() {
        return ExamResultDao.class.getName();
    }

    @Override
    public int updateLevelToAddOne(int uid) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("uid", uid);
        map.put("updateTime", updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateLevelToAddOne", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateLevelToAddOne: {},{}", new String[]{uid+""}, e);
        }
    }

    @Override
    public int updateLevelToZero() {
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateLevelToZero", getShardParam(null, null, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateLevelToZero: {},{}", "", e);
        }
    }
}
