package com.meiliwan.emall.stock.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.stock.bean.StockImportLog;

public interface StockImportLogDao extends IDao<Integer,StockImportLog>{

    /**
     * 批量导入库存日志
     * @return
     */
    int addStockImportLogOnBatch(StockImportLog[] logs);
}