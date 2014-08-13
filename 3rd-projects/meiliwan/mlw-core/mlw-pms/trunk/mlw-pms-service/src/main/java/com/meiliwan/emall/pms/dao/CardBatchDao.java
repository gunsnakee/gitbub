package com.meiliwan.emall.pms.dao;


import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.CardBatch;

public interface CardBatchDao extends IDao<String, CardBatch> {

    /**
     * 修改提前到期提醒时间
     *
     * @param batchId
     * @param warnDate
     * @return
     */
    int updateWarnDate(String batchId, int preDate, String warnDate);

    /**
     * 增加相关状态的数量，例如冻结，作废，销售等
     *
     * @param batchId
     * @param num
     * @return
     */
    int updateNumsByAdd(String batchId, int num, String field);

    /**
     * 減少相关状态的数量，例如冻结，作废，销售等
     *
     * @param batchId
     * @param num
     * @return
     */
    int updateNumsBySub(String batchId, int num, String field);

    /**
     * 增加对应字段的数量，同时减少对应的库存
     *
     * @param batchId
     * @param num
     * @param field
     * @return
     */
    int updateNumsAndStock(String batchId, int num, String field);

    /**
     * 批量作废，创建礼品卡出现异常
     *
     * @param batchId
     * @return
     */
    int updateCardNumByDel(String batchId);
}