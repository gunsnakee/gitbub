package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.dao.ActivityProductDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * @author : yiyou.luo
 * Date: 13-12-24
 * Time: 上午11:00
 */
@Repository
public class ActivityProductDaoImpl extends BaseDao<Integer, ActivityProductBean> implements ActivityProductDao {
    @Override
    public String getMapperNameSpace() {
        // TODO Auto-generated method stub
        return ActivityProductDao.class.getName();
    }


    @Override
    public List<ActivityProductBean> getAllByProIdForUpAct(int proId) {
        Map map = new HashMap();
        map.put("proId", proId);
        map.put("currDate", new Date());
        return getSqlSession().selectList(getMapperNameSpace() + ".getAllByProIdForUpAct", getShardParam(null, map, false));
    }

    @Override
     public List<ActivityProductBean> getAllByProIdForUpAct(int[] proIds) {
        Map map = new HashMap();
        map.put("proIds", proIds);
        map.put("currDate", new Date());
        return getSqlSession().selectList(getMapperNameSpace() + ".getAllByProIdsForUpAct", getShardParam(null, map, false));
    }

    @Override
    public List<ActivityProductBean> getAllByProIdActIsVaild(int[] proIds) {
        Map map = new HashMap();
        map.put("proIds", proIds);
        map.put("currDate", new Date());
        return getSqlSession().selectList(getMapperNameSpace() + ".getAllByProIdActIsVaild", getShardParam(null, map, false));
    }

    @Override
    public List<ActivityProductBean> getAPByProIdAndTimes(int proId, Date newStartTime, Date newEndTime) {
        Map map = new HashMap();
        map.put("proId", proId);
        map.put("newStartTime", newStartTime);
        map.put("newEndTime", newEndTime);
        return getSqlSession().selectList(getMapperNameSpace() + ".getAPByProIdAndTimes", getShardParam(null, map, false));
    }

    @Override
    public Integer deleteByActId(Integer actId) {
        Map map = new HashMap();
        map.put("actId", actId);
        return   getSqlSession().delete(getMapperNameSpace() + ".deleteByActId",getShardParam(null, map, false));
    }
}
 