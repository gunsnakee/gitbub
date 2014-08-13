package com.meiliwan.emall.stock.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.stock.bean.ProStockLog;

import java.util.List;

public interface ProStockLogDao extends IDao<Integer,ProStockLog>{

    /**
     * 批量增加库存日志
     * @return
     */
    int addStockLogOnBatch(List<ProStockLog> list);
}