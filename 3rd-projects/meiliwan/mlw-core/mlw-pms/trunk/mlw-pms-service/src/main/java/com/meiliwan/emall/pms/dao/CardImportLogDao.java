package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.CardImportLog;

import java.util.List;

public interface CardImportLogDao extends IDao<Integer,CardImportLog> {

    /**
     * 批量增加卡的导入记录
     *
     * @param logs
     * @return
     */
    int insertByBatch(List<CardImportLog> logs);
}