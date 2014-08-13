package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;
import com.meiliwan.emall.pms.bean.ProAction;
import com.meiliwan.emall.pms.dao.ProActionDao;
import com.meiliwan.emall.pms.dto.ProductStockItem;
import com.meiliwan.emall.pms.util.ActionOpt;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * 用户对商品的操作行为表 比如点击喜欢等
 * User: wuzixin
 * Date: 13-7-8
 * Time: 下午10:24
 */
@Service
public class ProActionService extends DefaultBaseServiceImpl {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(ProActionService.class);
    @Autowired
    private ProActionDao proActionDao;

    @IceServiceMethod
    public void addProAction(JsonObject resultObj, ProAction action) {
        int actId = 0;
        if (action != null) {
            actId = proActionDao.insert(action);
        }
        addToResult(actId, resultObj);
    }

    /**
     * 修改用户行为相关信息
     *
     * @param resultObj
     * @param action
     */
    public void updateAction(JsonObject resultObj, ProAction action) {
        int count = proActionDao.update(action);
        boolean suc = false;
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(action.getProId());
            suc = true;
        }
        addToResult(suc, resultObj);
    }

    /**
     * 修改商品相关字段，并且相关信息增加1
     *
     * @param resultObj
     * @param proId
     * @param parm
     */
    @IceServiceMethod
    public synchronized void updateActionByOpt(JsonObject resultObj, int proId, ActionOpt parm) {
        int count = proActionDao.updateActionByOpt(proId, parm.getCode());
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(proId);
        }
        addToResult(count > 0 ? count : 0, resultObj);
    }

    /**
     * 通过商品ID获取用户行为信息
     *
     * @param resultObj
     * @param proId
     */
    @IceServiceMethod
    public void getActionByProId(JsonObject resultObj, int proId) {
        addToResult(proActionDao.getEntityById(proId), resultObj);
    }

    /**
     * 通过商品IDS 获取商品用户行为列表
     *
     * @param resultObj
     * @param proIds
     */
    public void getMapByProIds(JsonObject resultObj, String proIds) {
        List<ProAction> list = null;
        if (proIds != null) {
            String whereSql = "pro_id in (" + proIds + ")";
            list = proActionDao.getListByObj(null, whereSql);
        }
        addToResult(list, resultObj);
    }

    /**
     * 增加销量相关，修改单个商品
     *
     * @param resultObj
     * @param id
     * @param num
     */
    @IceServiceMethod
    public synchronized void updateProSale(JsonObject resultObj, int id, int num) {
        int count = proActionDao.updateProSale(id, num);
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(id);
        }
        addToResult(count > 0 ? count : 0, resultObj);
    }

    /**
     * 增加浏览数相关
     *
     * @param resultObj
     * @param id
     * @param num
     */
    @IceServiceMethod
    public void updateProScan(JsonObject resultObj, int id, int num) {
        int count = proActionDao.updateProScan(id, num);
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(id);
        }
        addToResult(count > 0 ? count : 0, resultObj);
    }

    /**
     * 用户确认收货，增加该订单对应商品的销量
     *
     * @param resultObj
     * @param items     订单项商品列表
     */
    public synchronized void updateSaleByOrderConfm(JsonObject resultObj, ProductStockItem[] items) {
        for (ProductStockItem item : items) {
            try {
                proActionDao.updateProSale(item.getProId(), item.getStockNum());
                RedisSearchInfoUtil.addSearchInfo(item.getProId());
            } catch (Exception e) {
                logger.error(e, item, "");
            }
        }
    }

    /**
     * 用户删除评论时候，对应的评论数减一操作
     *
     * @param resultObj
     * @param id
     * @param parm
     */
    public synchronized void updateCommentByDelete(JsonObject resultObj, int id, ActionOpt parm) {
        int count = proActionDao.updateCommentByDelete(id, parm.getCode());
        boolean success = false;
        if (count > 0) {
            RedisSearchInfoUtil.addSearchInfo(id);
            success = true;
        }
        addToResult(success, resultObj);
    }

    /**
     * 修改商品SKU对应的优秀评论
     *
     * @param resultObj
     * @param proId
     * @param commentId
     */
    public void updateCommentIdById(JsonObject resultObj, int proId, int commentId) {
        boolean suc = false;
        int count = proActionDao.updateCommentIdById(proId, commentId);
        if (count > 0) {
            proActionDao.getEntityById(proId);
            suc = true;
        }
        addToResult(suc, resultObj);
    }
}
