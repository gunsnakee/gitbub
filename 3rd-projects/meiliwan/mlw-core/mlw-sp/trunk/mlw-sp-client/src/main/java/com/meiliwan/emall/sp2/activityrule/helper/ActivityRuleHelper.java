package com.meiliwan.emall.sp2.activityrule.helper;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRule;
import com.meiliwan.emall.sp2.activityrule.base.ActivityRuleParam;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.client.rule.ActivityDiscountRuleClient;
import com.meiliwan.emall.sp2.constant.ActType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-24
 * Time: 下午6:06
 * 活动规则帮助类
 *
 */
public class ActivityRuleHelper {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(ActivityRuleHelper.class);

    //根据活动 获取该活动的规则list
    public static List<ActivityRule> getActRules(ActivityBean spActivity, ActivityRuleParam actRuleParam){
        List<ActivityRule>  rules = null;
        try {
            ActType actType = ActType.valueOf(spActivity.getActType());
            switch (actType) {
                case DISCOUNT:
                    rules = ActivityDiscountRuleClient.getActRules(spActivity, actRuleParam);
                    break;
            }
        }catch (IllegalArgumentException e) {
            logger.error(e, spActivity, null);
        }
        return rules;
    }


}
