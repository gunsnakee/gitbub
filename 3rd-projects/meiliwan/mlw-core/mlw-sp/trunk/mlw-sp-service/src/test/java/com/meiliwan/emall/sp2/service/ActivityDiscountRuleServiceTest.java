package com.meiliwan.emall.sp2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.sp2.activityrule.ActivityDiscountRule;
import com.meiliwan.emall.sp2.base.BaseTest;
import com.meiliwan.emall.sp2.bean.ActivityDiscountRuleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-27
 * Time: 下午2:19
 * To change this template use File | Settings | File Templates.
 */
public class ActivityDiscountRuleServiceTest extends BaseTest {
    @Autowired
    ActivityDiscountRuleService activityDiscountRuleService;

    /**
     *
     */
     @Test
    public void testSaveSpActivityProduct(){
         ActivityDiscountRuleBean rule = new ActivityDiscountRuleBean();
         rule.setDiscount(70);
         rule.setAdmin("admin");
         rule.setUid(131244);
         rule.setRuleName("七折活动");
         rule.setActId(3);
         JsonObject json = new JsonObject();
         activityDiscountRuleService.saveSpActivityDiscountRule(json,rule);

    }


}
