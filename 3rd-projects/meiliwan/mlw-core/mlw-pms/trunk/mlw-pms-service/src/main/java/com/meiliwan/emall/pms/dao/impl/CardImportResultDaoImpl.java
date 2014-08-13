package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.CardImportResult;
import com.meiliwan.emall.pms.dao.CardImportResultDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午3:43
 */
@Repository
public class CardImportResultDaoImpl extends BaseDao<Integer, CardImportResult> implements CardImportResultDao {
    @Override
    public String getMapperNameSpace() {
        return CardImportResultDao.class.getName();
    }

    @Override
    public int insertByBatch(List<CardImportResult> logs) {
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
