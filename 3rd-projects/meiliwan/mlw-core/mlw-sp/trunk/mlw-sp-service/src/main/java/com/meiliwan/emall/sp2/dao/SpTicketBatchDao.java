package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.SpTicketBatch;

public interface SpTicketBatchDao extends IDao<Integer, SpTicketBatch> {

    /**
     * 增加相关状态的数量，例如券实际数量，激活，销售等
     *
     * @param batchId
     * @param num
     * @return
     */
    int updateNumsByAdd(int batchId, int num, String field);

    /**
     * 修改优惠券批次，对批次上线操作
     *
     * @param batchId
     * @return
     */
    int updateStateByOn(int batchId);

    int updateNumsBySendUser(int batchId,int realNum,int activeNum);
}