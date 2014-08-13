package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProAction;

public interface ProActionDao extends IDao<Integer, ProAction> {


    /**
     * 根据字段名修改对应的数据
     */
    int updateActionByOpt(int id, String param);

    /**
     * 增加商品的销量，包括实际销量和显示销量
     */
    int updateProSale(int id, int num);

    /**
     * 增加商品的销量，包括实际浏览数和显示浏览数
     */
    int updateProScan(int id, int num);

    /**
     * 用户删除评论时候，对应的评论数减一操作
     *
     * @param id
     * @param param
     * @return
     */
    int updateCommentByDelete(int id, String param);

    /**
     * 修改商品优秀评论
     * @param proId
     * @param commentId
     * @return
     */
    int updateCommentIdById(int proId,int commentId);
}