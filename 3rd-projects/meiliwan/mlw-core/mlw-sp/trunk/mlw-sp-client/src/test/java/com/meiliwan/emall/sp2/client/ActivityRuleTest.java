package com.meiliwan.emall.sp2.client;


import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import com.meiliwan.emall.sp2.bean.base.ActivityRuleBean;
import com.meiliwan.emall.sp2.client.rule.ActivityDiscountRuleClient;
import com.meiliwan.emall.sp2.constant.ActType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动规则 client 测试类
 *
 * @author yiyou.luo
 *         2013-12-25
*/

public class ActivityRuleTest {
    private static MLWLogger logger = MLWLoggerFactory.getLogger(ActivityRuleTest.class);


    /**
     * 根据活动保存活动规则
     * @return
     */
   // @Test
    public void saveSpActivityRules(){
        ActivityBean act = ActivityClient.getSpActivityById(7);
        ActivityDiscountRuleBean dRule = new ActivityDiscountRuleBean();
        dRule.setActId(act.getActId());
        dRule.setRuleName(act.getActName());
        dRule.setAdmin("admin");
        dRule.setDiscount(75);
        dRule.setUid(212121);
        List<ActivityRuleBean>  ruleBeans = new ArrayList<ActivityRuleBean>();
        ruleBeans.add(dRule);
        act.setSpRules(ruleBeans);
        List<Integer> ruleIdList = ActivityRuleClient.saveSpActivityRules(act);
        ruleIdList.size();

    }

    //根据活动 获取该活动的规则bean list
    @Test
    public void  getSpActRules(){
        ActivityBean spActivity =  ActivityClient.getSpActivityById(7);
        List list = ActivityRuleClient.getSpActRules(spActivity);
        list.size();

    }

}




