package com.meiliwan.emall.pms.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.CardImportLog;
import com.meiliwan.emall.pms.dao.CardImportLogDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 13-12-25
 * Time: 下午3:41
 */
@Repository
public class CardImportLogDaoImpl extends BaseDao<Integer, CardImportLog> implements CardImportLogDao {
    @Override
    public String getMapperNameSpace() {
        return CardImportLogDao.class.getName();
    }

    @Override
    public int insertByBatch(List<CardImportLog> logs) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", logs);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertByBatch: {}", logs.toString(), e);
        }    }
}
