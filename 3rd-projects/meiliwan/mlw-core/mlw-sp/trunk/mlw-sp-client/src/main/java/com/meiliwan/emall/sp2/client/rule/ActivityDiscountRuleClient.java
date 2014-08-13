package com.meiliwan.emall.sp2.client.rule;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.activityrule.ActivityDiscountRule;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRuleParam;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;

import java.util.ArrayList;
import java.util.List;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-24
 * Time: 下午6:26
 * 折扣规则client接口
 */
public class ActivityDiscountRuleClient {
    /**
     * 根据活动实例获取该活动的规则
     *
     * @param spActivity
     */
    public static List<ActivityRule> getActRules(ActivityBean spActivity, ActivityRuleParam actRuleParam) {
        List<ActivityRule> rules= new ArrayList<ActivityRule>(1);
        ActivityDiscountRule rule = new ActivityDiscountRule();
        rule.setRuleBean(new ActivityDiscountRuleBean());
        rule.setActRuleParam(actRuleParam);
        rules.add(rule);
        return rules;
    }

    /**
     * 保存折扣活动规则
     * @return
     */
    public static Integer saveSpActivityDiscountRule(ActivityDiscountRuleBean rule){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityDiscountRuleService/saveSpActivityDiscountRule", rule));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 根据活动id 获取活动规则bean list
     * @param actId
     */
    public static List<ActivityDiscountRuleBean> getDiscountRuleByActId(Integer actId){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityDiscountRuleService/getDiscountRuleByActId",actId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ActivityDiscountRuleBean>>() {
        }.getType());
    }

}
