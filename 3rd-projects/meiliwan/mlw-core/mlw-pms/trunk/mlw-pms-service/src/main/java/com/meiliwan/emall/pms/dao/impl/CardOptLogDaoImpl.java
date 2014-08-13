package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.CardOptLog;
import com.meiliwan.emall.pms.dao.CardOptLogDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午3:44
 */
@Repository
public class CardOptLogDaoImpl extends BaseDao<Integer, CardOptLog> implements CardOptLogDao {
    @Override
    public String getMapperNameSpace() {
        return CardOptLogDao.class.getName();
    }

    @Override
    public int insertByBatch(List<CardOptLog> logs) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", logs);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", logs.toString(), e);
        }
    }
}
