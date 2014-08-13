package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.ExamLog;
import com.meiliwan.emall.sp2.dao.ExamLogDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-4-24
 * Time: 下午5:05
 */
@Repository
public class ExamLogDaoImpl extends BaseDao<Integer, ExamLog> implements ExamLogDao {
    @Override
    public String getMapperNameSpace() {
        return ExamLogDao.class.getName();
    }

    @Override
    public int getCountAnswerGroupNum(int uid) {
        Map<String, Object> map = new HashMap<String, Object>();
        String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        map.put("uid", uid);
        map.put("date", date);

        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".getCountAnswerGroupNum", getShardParam(null, map, true));
            return (Integer) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getCountAnswerGroupNum: {},{}", new String[]{uid + "", date}, e);
        }
    }

    @Override
    public int updateCountAddOne(int groupId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", groupId);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateCountAddOne", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateCountAddOne: {},{}", new String[]{groupId + ""}, e);
        }
    }

    @Override
    public int updateGroupState(int groupId, int uid) {
        Map<String, Object> map = new HashMap<String, Object>();
        String date = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
        map.put("id", groupId);
        map.put("uid", uid);
        map.put("date", date);

        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateGroupState", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateGroupState: {},{}", new String[]{uid + "", date}, e);
        }
    }
}
