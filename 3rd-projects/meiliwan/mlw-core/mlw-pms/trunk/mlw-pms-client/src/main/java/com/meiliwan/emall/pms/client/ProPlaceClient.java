package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProPlace;
import com.meiliwan.emall.service.BaseService;

import java.util.List;

/**
 * User: guangdetang
 * Date: 13-6-4
 * Time: 下午4:58
 */
public class ProPlaceClient {

    private static final String SERVICE_NAME = "proPlaceService";
    /**
     * 添加产地
     * @param proPlace
     * @return
     */
    public static boolean addPropPlace(ProPlace proPlace) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/saveProPlace", proPlace));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 修改产地、删除
     * @param proPlace
     * @return
     */
    public static boolean updateProPlace(ProPlace proPlace) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateProPlace", proPlace));
        return obj.get(BaseService.RESULT_OBJ) == null ? false : obj.get(BaseService.RESULT_OBJ).getAsBoolean();
    }

    /**
     * 获得一条产地
     * @param placeId
     * @return
     */
    public static ProPlace getProPlaceById(int placeId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getProPlaceById", placeId));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ProPlace.class);
    }

    /**
     * 获得产地分页列表
     * @param proPlace
     * @param pageInfo
     * @return
     */
    public static PagerControl<ProPlace> getAllProPlacePager(ProPlace proPlace, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getAllProPlacePager", proPlace, pageInfo));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<PagerControl<ProPlace>>() {
        }.getType());
    }

    /**
     * 获得商品产地下拉列表。（前台使用）
     * @param propPlace
     * @return
     */
    public static List<ProPlace> getAllProPlaceList(ProPlace propPlace) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME+"/getAllProPlaceList", propPlace));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ProPlace>>() {
        }.getType());
    }

}
