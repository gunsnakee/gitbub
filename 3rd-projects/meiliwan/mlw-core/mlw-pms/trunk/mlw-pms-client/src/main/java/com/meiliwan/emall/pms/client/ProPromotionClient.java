package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.bean.ProPromotion;
import com.meiliwan.emall.pms.bean.ProSupplier;
import com.meiliwan.emall.service.BaseService;

import java.util.List;


/**
 * User: guangdetang
 * Date: 13-6-18
 * Time: 下午4:13
 */
public class ProPromotionClient {

    private static final String SERVICE_NAME = "proPromotionService";

    /**
     * 添加推荐促销的商品
     * @param promotion
     */
    public static boolean addPromotion(ProPromotion promotion) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addPromotion", promotion));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改推荐促销的商品
     * @param promotion
     */
    public static boolean updatePromotion(ProPromotion promotion) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updatePromotion", promotion));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 删除推荐促销的商品
     * @param ids
     */
    public static boolean updateDelPromotion(int[] ids, short state) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateDelPromotion", ids, state));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    public static boolean getPromotionIsExistByProId(int proId, int thirdId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getPromotionIsExistByProId", proId, thirdId));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得促销推荐商品分页列表
     * @param promotion
     * @param pageInfo
     */
    public static PagerControl<ProPromotion> getPromotionPager(ProPromotion promotion, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getPromotionPager", promotion, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProPromotion>>() {
        }.getType());
    }

    /**
     * 获得未删除的促销推荐列表
     * @param promotion
     * @param pageInfo
     */
    public static List<ProPromotion> getPromotionList(ProPromotion promotion, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getPromotionList", promotion, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProPromotion>>() {
        }.getType());
    }

    /**
     * 获得一条促销对象
     * @param id
     * @return
     */
    public static ProPromotion getPromotion(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getPromotion", id));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProPromotion.class);
    }

    /**
     * 删除所有推荐位商品
     * @return
     */
    public static boolean deletePromotion() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/deletePromotionTable"));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 更新某三级类目下的推荐商品
     * @param thirdId
     * @return
     */
    public static boolean updatePromotionByThirdCategoryId(int thirdId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updatePromotionByThirdCategoryId", thirdId));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 更新全站推荐位商品
     * @return
     */
    public static boolean updateAllPromotion(ProCategory thirdCategory) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateAllPromotion", thirdCategory));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得三级类目下推荐位商品列表
     * @param thirdCategoryId
     * @return
     */
    public static List<ProPromotion> getThirdCategoryPromotion(int thirdCategoryId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getThirdCategoryPromotion", thirdCategoryId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProPromotion>>() {
        }.getType());
    }

}
