package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicketBatch;
import com.meiliwan.emall.sp2.dao.SpTicketBatchDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:25
 */
@Repository
public class SpTicketBatchDaoImpl extends BaseDao<Integer, SpTicketBatch> implements SpTicketBatchDao {
    @Override
    public String getMapperNameSpace() {
        return SpTicketBatchDao.class.getName();
    }

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.sp$tkBatch;
    }

    @Override
    public int updateNumsByAdd(int batchId, int num, String field) {
        Map<String, Object> map = getMap(String.valueOf(batchId), num, field);
        try {
            deleteCacheByPk(batchId);
            return getSqlSession().update(getMapperNameSpace() + ".updateNumsByAdd", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateNumsByAdd: {},{}", new String[]{String.valueOf(batchId), String.valueOf(num), field}, e);
        }
    }

    @Override
    public int updateStateByOn(int batchId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("batchId", batchId);
        map.put("onTime", DateUtil.getCurrentDateTimeStr());
        try {
            deleteCacheByPk(batchId);
            return getSqlSession().update(getMapperNameSpace() + ".updateStateByOn", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateStateByOn: {},{}", new String[]{String.valueOf(batchId)}, e);
        }
    }

    @Override
    public int updateNumsBySendUser(int batchId, int realNum, int activeNum) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", batchId);
        map.put("realNum", realNum);
        map.put("activeNum", activeNum);
        try {
            deleteCacheByPk(batchId);
            return getSqlSession().update(getMapperNameSpace() + ".updateNumsBySendUser", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateNumsBySendUser: {},{}", new String[]{String.valueOf(batchId)}, e);
        }
    }

    private Map<String, Object> getMap(String batchId, int num, String field) {
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", batchId);
        map.put("field", field);
        map.put("num", num);
        map.put("updateTime", updateTime);
        return map;
    }
}
