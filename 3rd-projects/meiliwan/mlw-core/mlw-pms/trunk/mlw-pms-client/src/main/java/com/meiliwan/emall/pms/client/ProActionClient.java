package com.meiliwan.emall.pms.client;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProAction;
import com.meiliwan.emall.pms.dto.ProductStockItem;
import com.meiliwan.emall.pms.util.ActionOpt;

/**
 * User: wuzixin
 * Date: 13-7-9
 * Time: 上午9:30
 */
public class ProActionClient {


    /**
     * 增加用户行为相关
     *
     * @param action
     * @return
     */
    public static int addProAction(ProAction action) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/addProAction", action));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 修改商品用户行为相关信息
     * @param action
     * @return
     */
    public static boolean updateAction(ProAction action){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateAction", action));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 修改用户行为相关的数据，比如喜欢、评论数量等，主要进行加1操作
     *
     * @param proId
     * @param parm
     * @return
     */
    public static int updateActionByOpt(int proId, ActionOpt parm) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateActionByOpt", proId, parm));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 获取商品用户行为数据实体
     *
     * @param proId
     * @return
     */
    public static ProAction getActionByProId(int proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/getActionByProId", proId));
        return new Gson().fromJson(obj.get("resultObj"), ProAction.class);
    }

    /**
     * 通过商品IDS获取对应的喜爱数量
     *
     * @param proIds
     * @return
     */
    public static Map<String, String> getMapByProIds(String proIds) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/getMapByProIds", proIds));
        List<ProAction> list = new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProAction>>() {
        }.getType());
        Map<String, String> map = new HashMap<String, String>();
        if (list != null) {
            for (ProAction action : list) {
                map.put(String.valueOf(action.getProId()), String.valueOf(action.getLove()));
            }
        }
        return map;
    }

    /**
     * 增加销量相关
     *
     * @param id
     * @param num
     */
    public static int updateProSale(int id, int num) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateProSale", id, num));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 增加浏览数相关
     *
     * @param id
     * @param num
     */
    public static int updateProScan(int id, int num) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateProScan", id, num));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 用户确认收货，增加该订单对应商品的销量
     *
     * @param items     订单项商品列表
     */
    public static void updateSaleByOrderConfm(List<ProductStockItem> items) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateSaleByOrderConfm", items));
    }

    /**
     * 用户删除评论时候，对应的评论数减一操作
     * @param id
     * @param parm
     */
    public static void updateCommentByDelete(int id,ActionOpt parm) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateCommentByDelete", id,parm));
    }

    /**
     * 修改商品优秀评论
     *
     * @param proId
     * @param commentId
     * @return
     */
    public static boolean updateCommentIdById(int proId, int commentId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proActionService/updateCommentIdById", proId, commentId));
        return obj.get("resultObj").getAsBoolean();
    }
}
