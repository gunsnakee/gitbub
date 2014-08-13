package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.SpTicketImportDetail;

import java.util.List;

public interface SpTicketImportDetailDao extends IDao<Integer, SpTicketImportDetail> {

    /**
     * 批量增加优惠券导入的记录
     *
     * @param list
     * @return
     */
    int insertTicketImportDtByBatch(List<SpTicketImportDetail> list);
}