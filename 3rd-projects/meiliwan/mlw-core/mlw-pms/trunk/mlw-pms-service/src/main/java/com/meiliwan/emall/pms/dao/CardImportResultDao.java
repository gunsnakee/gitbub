package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.CardImportResult;

import java.util.List;

public interface CardImportResultDao extends IDao<Integer, CardImportResult> {
    /**
     * 批量增加卡的导入记录
     *
     * @param logs
     * @return
     */
    int insertByBatch(List<CardImportResult> logs);
}