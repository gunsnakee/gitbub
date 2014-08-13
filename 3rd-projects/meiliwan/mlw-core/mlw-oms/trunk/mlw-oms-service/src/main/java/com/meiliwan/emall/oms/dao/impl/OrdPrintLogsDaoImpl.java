package com.meiliwan.emall.oms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdPrintLogs;
import com.meiliwan.emall.oms.dao.OrdPrintLogsDao;
import org.springframework.stereotype.Repository;

/**
 * Created by guangdetang on 13-10-30.
 */
@Repository
public class OrdPrintLogsDaoImpl extends BaseDao<Integer, OrdPrintLogs> implements OrdPrintLogsDao{
    @Override
    public String getMapperNameSpace() {
        return OrdPrintLogsDao.class.getName();
    }
}
