package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.WishCard;
import com.meiliwan.emall.service.BaseService;

import java.util.List;

/**
 * Created by Administrator on 13-12-23.
 */
public class WishCardClient {

    public static int addWishCard(WishCard wishCard) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("wishCardService/addWishCard", wishCard));
        return obj.get(BaseService.RESULT_OBJ).getAsInt();
    }

    public static PagerControl<WishCard> getWishCardsByObj(WishCard wishCard, PageInfo pageInfo, String whereSql, boolean asc, Boolean isRemark) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("wishCardService/getWishCardsByObj", wishCard, pageInfo, whereSql, asc, isRemark));

        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ).getAsJsonObject(), new TypeToken<PagerControl<WishCard>>() {
        }.getType());
    }

    public static WishCard getWishCardById(int id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("wishCardService/getWishCardById", id));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), WishCard.class);
    }

    public static int updateWishCardByObj(WishCard wishCard){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("wishCardService/updateWishCardById", wishCard));
        return obj.get(BaseService.RESULT_OBJ).getAsInt();
    }
}
