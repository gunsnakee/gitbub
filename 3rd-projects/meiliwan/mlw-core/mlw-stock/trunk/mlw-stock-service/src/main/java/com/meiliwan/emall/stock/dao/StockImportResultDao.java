package com.meiliwan.emall.stock.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.stock.bean.StockImportResult;

public interface StockImportResultDao extends IDao<Integer, StockImportResult> {

    /**
     * 通过批次号，获取导入结果
     *
     * @param batchNo
     * @return
     */
    StockImportResult getResultByBatchNo(String batchNo);
}