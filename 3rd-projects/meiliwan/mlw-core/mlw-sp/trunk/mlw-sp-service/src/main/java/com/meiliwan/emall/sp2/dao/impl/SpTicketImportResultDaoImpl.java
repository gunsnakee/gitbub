package com.meiliwan.emall.sp2.dao.impl;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;
import com.meiliwan.emall.sp2.bean.SpTicketImportResult;
import com.meiliwan.emall.sp2.dao.SpTicketImportResultDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午4:31
 */
@Repository
public class SpTicketImportResultDaoImpl extends BaseDao<Integer, SpTicketImportResult> implements SpTicketImportResultDao {
    @Override
    public String getMapperNameSpace() {
        return SpTicketImportResultDao.class.getName();
    }

    @Override
    public List<SpTicketImportResult> getListImportResultByFileName(String fileName) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("fileName", fileName);
        List<SpTicketImportResult> list = null;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + ".getListImportResultByFileName", getShardParam(null, map, false));
            return list;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getListImportResultByFileName: fileName: " + fileName, e);
        }
    }
}
