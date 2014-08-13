package com.meiliwan.emall.pms.dao.impl;

import com.google.common.base.Strings;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pms.bean.WishCard;
import com.meiliwan.emall.pms.dao.WishCardDao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 13-12-23.
 */
@Repository
public class WishCardDaoImpl extends BaseDao<Integer, WishCard> implements WishCardDao {
    @Override
    public String getMapperNameSpace() {
        return WishCardDao.class.getName();
    }

    @Override
    public PagerControl<WishCard> getWishCardListByObj(WishCard wishCard, PageInfo pageInfo, String whereSql, String orderBySql) {
        PagerControl<WishCard> pagerControl = new PagerControl<WishCard>();
        pageInfo.startTime();
        List<WishCard> list = null;
        int total = 0;
        try {
            list = getSqlSession().selectList(getMapperNameSpace() + getListByEntityAndPageInfo,
                    getShardParam(wishCard.getId(), getMapParams(wishCard, pageInfo, whereSql, orderBySql), false));
            total = (Integer) getSqlSession().selectOne(getMapperNameSpace() + ".getTotalByBean",
                    getShardParam(wishCard != null ? wishCard.getId() : null, getMapParams(wishCard, null, whereSql, null), false));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".getWishCardListByObj: {},{}", wishCard != null ? wishCard.toString() :
                    null, e);
        }
        pageInfo.endTime();
        pageInfo.setTotalCounts(total);
        if (list != null) {
            pagerControl.setEntityList(list);
        }
        pagerControl.setPageInfo(pageInfo);
        return pagerControl;
    }
}
