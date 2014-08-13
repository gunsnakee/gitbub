package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.SpTicketImportResult;

import java.util.List;

public interface SpTicketImportResultDao extends IDao<Integer, SpTicketImportResult> {

    List<SpTicketImportResult> getListImportResultByFileName(String fileName);
}