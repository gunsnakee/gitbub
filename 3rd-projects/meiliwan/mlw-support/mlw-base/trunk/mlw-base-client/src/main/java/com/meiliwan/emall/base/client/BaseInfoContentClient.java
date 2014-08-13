package com.meiliwan.emall.base.client;

//import com.alibaba.fastjson.TypeReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseInfoContent;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import java.util.List;

/**
 * 资讯管理 主要实现资讯内容相关
 * User: wuzixin
 * Date: 13-6-14
 * Time: 下午2:42
 */
public class BaseInfoContentClient {

    public static boolean addBaseInfoContent(BaseInfoContent bict) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/addBaseInfoContent", bict));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateBaseInfoContent(BaseInfoContent bict) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/updateBaseInfoContent", bict));
        return obj.get("resultObj").getAsBoolean();
    }

    public static BaseInfoContent getBaseICTById(int infoId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/getBaseICTById", infoId));
        return new Gson().fromJson(obj.get("resultObj"), BaseInfoContent.class);
    }

    public static BaseInfoContent getBaseICTByObj(BaseInfoContent bict) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/getBaseICTByObj", bict));
        return new Gson().fromJson(obj.get("resultObj"), BaseInfoContent.class);
    }

    public static PagerControl<BaseInfoContent> getBaseICTByPager(BaseInfoContent baseICT, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/getBaseICTByPager", baseICT, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BaseInfoContent>>() {
        }.getType());
    }

    public static List<BaseInfoContent> getFrontTenInfoContent() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseInfoContentService/getFrontTenInfoContent"));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseInfoContent>>() {
        }.getType());
    }
}
