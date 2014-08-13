package com.meiliwan.emall.base.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.base.bean.BaseMsgTemplate;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

import java.util.List;

/**
 * User: wuzixin
 * Date: 14-5-28
 * Time: 下午5:55
 */
public class BaseMsgTemplateClient {

    /**
     * 修改发送消息的内容模板
     *
     * @param template
     */
    public static int update(BaseMsgTemplate template) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseMsgTemplateService/update", template));
        return obj.get("resultObj").getAsInt();
    }

    /**
     * 根据模板ID获取消息发送信息
     *
     * @param tmpId
     */
    public static BaseMsgTemplate getTmpById(int tmpId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseMsgTemplateService/getTmpById", tmpId));
        return new Gson().fromJson(obj.get("resultObj"), BaseMsgTemplate.class);
    }

    /**
     * 根据消息模板相关类型获取消息模板列表
     *
     * @param tmpType 模板类型
     */
    public static List<BaseMsgTemplate> getListByType(int tmpType) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseMsgTemplateService/getListByType", tmpType));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BaseMsgTemplate>>() {
        }.getType());
    }

    /**
     * 根据消息模板类型和内容类型获取消息模板
     *
     * @param tmpType  模板类型
     * @param contType 内容类型
     */
    public static BaseMsgTemplate getMsgByType(int tmpType, int contType) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BASE_ICE_SERVICE,
                JSONTool.buildParams("baseMsgTemplateService/getMsgByType", tmpType, contType));
        return new Gson().fromJson(obj.get("resultObj"), BaseMsgTemplate.class);
    }
}
