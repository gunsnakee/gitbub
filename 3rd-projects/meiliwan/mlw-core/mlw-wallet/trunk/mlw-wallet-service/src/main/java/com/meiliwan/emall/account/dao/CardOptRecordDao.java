package com.meiliwan.emall.account.dao;

import com.meiliwan.emall.account.bean.CardOptRecord;
import com.meiliwan.emall.core.db.IDao;

import java.util.Date;
import java.util.List;


public interface CardOptRecordDao extends IDao<Integer,CardOptRecord> {
	/** 查询某个用户在某段时间内的入账金额 */
    Double incomeTotalAmount(Integer uid,Date beginDate,Date endDate);
    /** 查询某个用户在某段时间内的消费金额 */
    Double paymentTotalAmount(Integer uid,Date beginDate,Date endDate);
    /** 通过开始结束记录时间查询用户id */
    List<Integer> getUidsByTime(Date beginDate,Date endDate);
    /** 通过订单号查询消费金额 */
    Double getSubMoneyByOrderId(String orderId, String successFlag);
    /** 通过订单号查询消费金额 */
    Double getAllRefundMoneyByOrderId(String orderId, String successFlag);
    /**通过用户id和时间查询某个时间节点之后的充值次数*/
    int getTodayAddCounts(int uid, Date beginDate);
}