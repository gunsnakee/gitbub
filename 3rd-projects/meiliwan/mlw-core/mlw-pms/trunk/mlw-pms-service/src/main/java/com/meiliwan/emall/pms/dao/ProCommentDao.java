package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.ProComment;
import com.meiliwan.emall.pms.bean.ProCommentVo;
import com.meiliwan.emall.pms.dto.CommentDTO;
import com.meiliwan.emall.pms.dto.ProCommentPage;
import com.meiliwan.emall.pms.dto.QueryCountsDTO;

import java.util.List;

public interface ProCommentDao extends IDao<Integer, ProComment> {

    PagerControl<ProComment> getPagerByCommentView(CommentDTO commentView, PageInfo pageInfo);

    PagerControl<ProCommentPage> getPagerForBkstage(CommentDTO commentView, PageInfo pageInfo);
    /**
     * 根据字段名修改对应的数据
     */
    int updateComByParam(Integer id,String param);

    /**
     * (应用场景：地方馆展示)
     * 通过商品IDs获取评论列表，每个商品至多查三个好评
     * @param ids
     */
    List<ProCommentVo> getListByProIds(Integer[] ids);

    /**
     * (应用场景：商品详情页展示各评论分段的条数，好评‘中评‘差评)
     * @param comment
     * @param orderBy
     * @return
     */
    List<QueryCountsDTO> getCountsGroupByScore(ProComment comment, String orderBy);

    /**
     * 根据订单IDS获取未评价的订单号列表
     * 使用场景：个人中心订单列表，查询未评价的订单
     * @param orderIds
     * @return
     */
    List<String> getOrderIsCommentListByOrderCenter(String[] orderIds);

    /**
     * 根据订单IDS获取已评价的订单号列表及其已评价数量
     * 使用场景：个人中心订单列表，查询已评价的订单
     * @param orderIds
     * @return
     */
    List getOrderAllCommentListByOrderCenter(String[] orderIds);
}