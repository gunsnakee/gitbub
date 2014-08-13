package com.meiliwan.emall.pms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProProperty;
import com.meiliwan.emall.pms.bean.ProPropertyValue;
import com.meiliwan.emall.pms.dto.PropertyValueList;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-6-3
 * Time: 下午7:08
 */
public class ProPropertyClient {

    public static boolean addProProperty(ProProperty proProperty) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/addProProperty", proProperty));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateProProperty(ProProperty proProperty) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/updateProProperty", proProperty));
        return obj.get("resultObj").getAsBoolean();
    }

    public static ProProperty getProPropertyById(Integer proPropId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getProPropertyById", proPropId));
        return new Gson().fromJson(obj.get("resultObj"), ProProperty.class);
    }

    public static List<ProProperty> getProPropertyList(ProProperty proProperty) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getProPropertyList", proProperty));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProperty>>() {
        }.getType());
    }

    public static PagerControl<ProProperty> getProPropertyPager(ProProperty proProperty, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getProPropertyPager", proProperty, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<ProProperty>>() {
        }.getType());
    }

    public static boolean resetPropertyBystus(ProProperty proProperty) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/resetPropertyBystus", proProperty));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 查找属性列表，包括对应三级类目包含的属性
     *
     * @param third 三级类目
     */
    public static List<ProProperty> getPropertyListByCategoryId(int third) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getPropertyListByCategoryId", third));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProperty>>() {
        }.getType());
    }

    /**
     * 用于对比商品属性和发布商品已有属性，用于做商品编辑功能
     *
     * @param third 三级类目
     */
    public static List<ProProperty> getPropByProProp(int third, int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getPropByProProp", third, spuId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProperty>>() {
        }.getType());
    }

    /**
     * 通过商品ID获取商品属性列表
     *
     * @param spuId
     */
    public static List<ProProperty> getPropListByProId(int spuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getPropListByProId", spuId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProperty>>() {
        }.getType());
    }

    public static PropertyValueList getAllPPVList() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getAllPPVList"));
        return new Gson().fromJson(obj.get("resultObj"), PropertyValueList.class);
    }

    /**
     * 通过属性值ID获取属性值
     *
     * @param valueId
     * @return
     */
    public static ProPropertyValue getPropertyValueById(int valueId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getPropertyValueById",valueId));
        return new Gson().fromJson(obj.get("resultObj"), ProPropertyValue.class);
    }

    /**
     * 根据SPU ID 获取spu对应的规格属性信息
     *
     * @param spuId
     * @return
     */
    public static List<ProProperty> getSpuWithSkuPropsById(int spuId){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getSpuWithSkuPropsById", spuId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProProperty>>() {
        }.getType());
    }

    /**
     * 根据商品ID获取商品规格属性名称
     *
     * @param proId
     */
    public static List<ProPropertyValue> getSkuProVsByProId(int proId){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proPropertyService/getSkuProVsByProId", proId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProPropertyValue>>() {
        }.getType());
    }
}
