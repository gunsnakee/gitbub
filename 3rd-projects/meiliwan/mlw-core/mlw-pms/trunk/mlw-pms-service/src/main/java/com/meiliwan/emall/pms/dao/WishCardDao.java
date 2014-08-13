package com.meiliwan.emall.pms.dao;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.core.db.IDao;
import com.meiliwan.emall.pms.bean.WishCard;

/**
 * Created by Administrator on 13-12-23.
 */
public interface WishCardDao extends IDao<Integer,WishCard> {

    PagerControl<WishCard> getWishCardListByObj(WishCard entity, PageInfo pageInfo, String whereSql, String orderBySql);
}
