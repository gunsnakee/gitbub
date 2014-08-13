package com.meiliwan.emall.sp2.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.sp2.bean.SpTicketBatchProd;

import java.util.List;

public interface SpTicketBatchProdDao extends IDao<Integer, SpTicketBatchProd> {

    int insertByBatchProd(List<SpTicketBatchProd> list);

    /**
     * 更加ids获取商品参加的优惠券
     *
     * @param ids
     * @return
     */
    List<SpTicketBatchProd> getTicketProdsByProIds(int[] ids);
    

    /**
     * 根据优惠券批次号和商品ids获取优惠券和批次之间的关系列表
     *
     * @param ids
     * @return
     */
    List<SpTicketBatchProd> getTicketProdsByProIdsAndBatchIds(int[] proIds, int[] batchIds);

    /**
     * 更加商品ID和批次ID删除商品
     *
     * @param proId
     * @param batchId
     * @return
     */
    int deleteProdById(int proId, int batchId);
}