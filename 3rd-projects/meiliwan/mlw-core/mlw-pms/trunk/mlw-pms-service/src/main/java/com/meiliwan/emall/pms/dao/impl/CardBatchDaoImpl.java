package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.CardBatch;
import com.meiliwan.emall.pms.dao.CardBatchDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * 礼品卡批次数据Dao层实现
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午3:38
 */
@Repository
public class CardBatchDaoImpl extends BaseDao<String, CardBatch> implements CardBatchDao {
    @Override
    public String getMapperNameSpace() {
        return CardBatchDao.class.getName();
    }

    @Override
    public int updateWarnDate(String batchId,int preDate, String warnTime) {
        deleteCacheByPk(batchId);
        Map<String, Object> map = new HashMap<String, Object>();
        String updateTime = DateUtil.getCurrentDateTimeStr();
        map.put("id", batchId);
        map.put("prdDay",preDate);
        map.put("warnTime", warnTime);
        map.put("updateTime", updateTime);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateWarnDate", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateWarnDate: {},{}", new String[]{batchId, warnTime}, e);
        }
    }

    @Override
    public int updateNumsByAdd(String batchId, int num, String field) {
        deleteCacheByPk(batchId);
        Map<String, Object> map = getMap(batchId, num, field);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateNumsByAdd", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateNumsByAdd: {},{}", new String[]{batchId, String.valueOf(num), field}, e);
        }
    }

    @Override
    public int updateNumsBySub(String batchId, int num, String field) {
        deleteCacheByPk(batchId);
        Map<String, Object> map = getMap(batchId, num, field);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateNumsBySub", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateNumsBySub: {},{}", new String[]{batchId, String.valueOf(num), field}, e);
        }
    }

    @Override
    public int updateNumsAndStock(String batchId, int num, String field) {
        deleteCacheByPk(batchId);
        Map<String, Object> map = getMap(batchId, num, field);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateNumsAndStock", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateNumsAndStock: {},{}", new String[]{batchId, String.valueOf(num), field}, e);
        }
    }

    @Override
    public int updateCardNumByDel(String batchId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", batchId);
        try {
            return getSqlSession().update(getMapperNameSpace() + ".updateCardNumByDel", getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".updateCardNumByDel: {},{}", new String[]{batchId}, e);
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
