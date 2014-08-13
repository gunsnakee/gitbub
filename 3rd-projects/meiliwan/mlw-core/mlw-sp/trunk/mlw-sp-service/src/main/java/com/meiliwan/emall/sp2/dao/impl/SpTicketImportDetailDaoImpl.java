package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicketImportDetail;
import com.meiliwan.emall.sp2.dao.SpTicketImportDetailDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:29
 */
@Repository
public class SpTicketImportDetailDaoImpl extends BaseDao<Integer, SpTicketImportDetail> implements SpTicketImportDetailDao {
    @Override
    public String getMapperNameSpace() {
        return SpTicketImportDetailDao.class.getName();
    }

    @Override
    public int insertTicketImportDtByBatch(List<SpTicketImportDetail> list) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", list);
        try {
            return getSqlSession().insert(getMapperNameSpace() + ".insertTicketImportDtByBatch",
                    getShardParam(null, map, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".insertTicketImportDtByBatch: {}", list.toString(), e);
        }
    }
}
