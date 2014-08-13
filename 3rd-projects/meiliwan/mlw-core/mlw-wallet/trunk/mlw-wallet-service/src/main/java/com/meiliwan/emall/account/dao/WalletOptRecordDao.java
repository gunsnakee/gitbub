package com.meiliwan.emall.account.dao;

import com.meiliwan.emall.account.bean.WalletOptRecord;
import com.meiliwan.emall.core.db.IDao;

import java.util.Date;
import java.util.List;


public interface WalletOptRecordDao extends IDao<Integer,WalletOptRecord> {
    Double incomeTotalAmount(Integer uid,Date beginDate,Date endDate);
    Double paymentTotalAmount(Integer uid,Date beginDate,Date endDate);
    List<Integer> getUidsByTime(Date beginDate,Date endDate);
}