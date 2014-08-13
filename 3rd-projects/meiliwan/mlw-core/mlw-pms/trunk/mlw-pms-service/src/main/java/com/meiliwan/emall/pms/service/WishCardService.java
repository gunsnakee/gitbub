package com.meiliwan.emall.pms.service;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.pms.bean.WishCard;
import com.meiliwan.emall.pms.dao.WishCardDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * Created by Administrator on 13-12-23.
 */
@Service
public class WishCardService extends DefaultBaseServiceImpl {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(WishCardService.class);

    @Autowired
    private WishCardDao wishCardDao;

    /**
     * 新增心愿卡，成功返回id，否则-1
     */
    @IceServiceMethod
    public void addWishCard(JsonObject result, WishCard wishCard) {
        int id = -1;
        try {
            if (wishCard != null) {
                wishCardDao.insert(wishCard);
            }
            id = wishCard.getId();
        } catch (Exception e) {
            logger.error(e, (wishCard != null) ? wishCard.toString() : null, null);
        }
        addToResult(id, result);
    }

    @IceServiceMethod
    public void getWishCardById(JsonObject result, int id) {
        addToResult(wishCardDao.getEntityById(id), result);
    }

    @IceServiceMethod
    public void getWishCardsByObj(JsonObject result, WishCard wishCard, PageInfo pageInfo, String whereSql, boolean asc, Boolean isRemark) {
        whereSql = initWhereSql(whereSql, isRemark);
        PagerControl<WishCard> pc = wishCardDao.getWishCardListByObj(wishCard, pageInfo, whereSql, "create_time " + (asc ? "asc" : "desc"));
        addToResult(pc, result);
    }

    private String initWhereSql(String whereSql, Boolean isRemark) {
        //是否有备注
        if (isRemark != null) {
            if (isRemark) {
                whereSql = (Strings.isNullOrEmpty(whereSql)) ? "remark is not null" : whereSql + "and remark is not null";
            } else {
                whereSql = ((Strings.isNullOrEmpty(whereSql)) ? "remark is null" : whereSql + "and remark is null");
            }
        }
        return whereSql;
    }

    public void updateWishCardById(JsonObject result, WishCard wishCard) {
        int id = -1;
        if (wishCard == null || wishCard.getId() == null) {
            addToResult(id, result);
            return;
        }

        try {
            id = wishCardDao.update(wishCard);
            addToResult(id, result);
            return;
        } catch (Exception e) {
            logger.error(e, (wishCard != null) ? wishCard.toString() : null, null);
            addToResult(-1, result);
            return;
        }
    }
}
