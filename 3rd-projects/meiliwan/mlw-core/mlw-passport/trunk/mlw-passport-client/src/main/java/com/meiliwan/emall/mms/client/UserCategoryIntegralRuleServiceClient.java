package com.meiliwan.emall.mms.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserCategoryIntegralRule;

import java.util.List;

/**
 * 类目积分规则  client
 * Created by yiyou.luo on 13-6-7.
 */
public class UserCategoryIntegralRuleServiceClient {
    private static final String SERVICE_NAME = "userCategoryIntegralRuleService";

    /**
     * 通过id获取类目规则实例
     *
     * @param id
     */
    public static UserCategoryIntegralRule getCategoryIntegralRuleById(String id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCategoryIntegralRuleById", id));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<UserCategoryIntegralRule>() {
        }.getType());
    }

    /**
     * 通过类目id获取类目规则实例list
     *
     * @param categoryId
     */
    public static List<UserCategoryIntegralRule> getCategoryIntegralRuleByCategoryId(String categoryId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getCategoryIntegralRuleByCategoryId", categoryId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserCategoryIntegralRule>>() {
        }.getType());
    }

    /**
     * 添加类目积分规则
     *
     * @param userCategoryIntegralRule
     */
    public static Integer saveCategoryIntegralRule(UserCategoryIntegralRule userCategoryIntegralRule) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/saveCategoryIntegralRule", userCategoryIntegralRule));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改类目积分规则
     *
     * @param userCategoryIntegralRule
     */
    public static boolean updateUserCategoryIntegralRule(UserCategoryIntegralRule userCategoryIntegralRule) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateUserCategoryIntegralRule", userCategoryIntegralRule));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }

    /**
     * 通过类目积分 实体参数获取对应的实体列表包含物理分页
     *
     * @param userCategoryIntegralRule
     * @param pageInfo
     */
    public static PagerControl getUserCategoryIntegralRulePaperByObj(UserCategoryIntegralRule userCategoryIntegralRule, PageInfo pageInfo) {
        JsonObject obj = null;
        obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserCategoryIntegralRulePaperByObj", userCategoryIntegralRule, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserCategoryIntegralRule>>() {
        }.getType());
    }

    public  static void main(String args[]){
          System.out.print("dsafdsaf");

        getUserCategoryIntegralRulePaperByObj(new UserCategoryIntegralRule(), new PageInfo());
    }
}
