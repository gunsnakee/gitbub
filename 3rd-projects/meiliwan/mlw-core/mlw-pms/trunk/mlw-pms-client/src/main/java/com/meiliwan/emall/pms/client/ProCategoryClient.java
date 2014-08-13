package com.meiliwan.emall.pms.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pms.bean.ProCategory;

import java.util.List;

/**
 * User: wuzixin
 * Date: 13-6-3
 * Time: 下午7:08
 */
public class ProCategoryClient {

    public static boolean addProCategory(ProCategory proCategory) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/addProCategory", proCategory));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateProCategory(ProCategory proCategory) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/updateProCategory", proCategory));
        return obj.get("resultObj").getAsBoolean();
    }

    public static ProCategory getProCategoryById(Integer categoryId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getProCategoryById", categoryId));
        return new Gson().fromJson(obj.get("resultObj"), ProCategory.class);
    }

    public static List<ProCategory> getAllCategory() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getAllCategory"));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProCategory>>() {
        }.getType());
    }

    public static List<ProCategory> getTreeCategory() {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getTreeCategory"));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProCategory>>() {
        }.getType());
    }

    public static List<ProCategory> getListByPId(ProCategory proCategory) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getListByPId", proCategory));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProCategory>>() {
        }.getType());
    }
    public static List<ProCategory> getListByCategoryId(int categoryId){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getListByCategoryId", categoryId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ProCategory>>() {
        }.getType());
    }
    public static boolean deleteProCategory(Integer categoryId, Integer level) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/deleteProCategory", categoryId, level));
        return obj.get("resultObj").getAsBoolean();
    }

    public static ProCategory getCategoryByLevelId(int first, int second, int third) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getCategoryByLevelId", first, second, third));
        return new Gson().fromJson(obj.get("resultObj"), ProCategory.class);
    }
    public static ProCategory getTreeSingleNoteCategoryById(int categoryId){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getTreeSingleNoteCategoryById", categoryId));
        return new Gson().fromJson(obj.get("resultObj"), ProCategory.class);
    }
    public static ProCategory getCategoruByProId(int proId){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.PMS_ICE_SERVICE,
                JSONTool.buildParams("proCategoryService/getCategoruByProId", proId));
        return new Gson().fromJson(obj.get("resultObj"), ProCategory.class);
    }
}
