package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.Card;

import java.util.List;

public interface CardDao extends IDao<String, Card> {
    /**
     * 通过礼品卡密码查询礼品卡
     *
     * @param pwd
     * @return
     */
    Card getCardByPwd(String pwd);

    /**
     * 批量增加礼品卡
     *
     * @param cards
     * @return
     */
    int insertByBatch(List<Card> cards);

    /**
     * 激活礼品卡
     *
     * @param cardId
     * @return
     */
    int updateActiveCard(String cardId, int userId, String userName);

    /**
     * 冻结礼品卡
     *
     * @param cardId
     * @return
     */
    int updateFreezeCard(String cardId, int state);

    /**
     * 批量作废礼品卡
     *
     * @param cardIds
     * @return
     */
    int updateDeleteCard(String[] cardIds);

    /**
     * 根据批量礼品卡号获取礼品列表
     *
     * @param cardIds
     * @return
     */
    List<Card> getCardsByIds(String[] cardIds);

    /**
     * 创建礼品卡出现异常，需要批量作废礼品卡
     *
     * @param batchId
     * @return
     */
    int updateFreezeByBatchId(String batchId);

    /**
     * 导入或者发送邮箱或者手机，需要指定销售状态，更新数据
     *
     * @param cardId
     * @param field
     * @param data
     * @return
     */
    int updateSellCard(String cardId, String field, String data);

    /**
     * 导入购买者信息，需要指定销售状态，更新数据，更新销售人
     *
     * @param cardId
     * @param field
     * @param data
     * @param sellerName
     * @return
     */
    int updateSellByImport(String cardId, String field, String data, String sellerName);

    /**
     * 根据批量礼品卡号获取礼品列表并且包括相关的礼品卡批次信息
     *
     * @param ids
     * @return
     */
    List<Card> getCardWithBatchByIds(String[] ids);

    /**
     * 根据用户ID获取对应用户激活礼品卡的数量，主要提供活动礼品卡激活业务
     *
     * @param userId
     * @return
     */
    int getCountByUserId(int userId,String batchId);
}