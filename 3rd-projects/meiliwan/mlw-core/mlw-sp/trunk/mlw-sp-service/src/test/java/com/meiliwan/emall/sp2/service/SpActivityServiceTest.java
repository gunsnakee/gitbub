package com.meiliwan.emall.sp2.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.sp2.base.BaseTest;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.constant.ActType;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-24
 * Time: 上午11：32
 * To change this template use File | Settings | File Templates.
 */
public class SpActivityServiceTest extends BaseTest {
    @Autowired
    ActivityService spActivityService;

    /**
     *
     */
     @Test
    public void testSaveSpActivity(){
        ActivityBean act = new ActivityBean();
        act.setActName("每日折扣001");
        act.setActDesc("美丽湾最牛逼折扣");
        act.setAdmin("admin");
        act.setActType(ActType.DISCOUNT.toString());

        JsonObject json = new JsonObject();
        spActivityService.saveSpActivity(json,act);
    }

    /**
     *
     */
    // @Test
    public void testgetAndUpdateSpActivity(){
        JsonObject json = new JsonObject();
        spActivityService.getSpActivityById(json,3);

        ActivityBean act = new ActivityBean();
        act.setActId(3);
        act.setActName("每日折扣");
        act.setActDesc("美丽湾最牛逼折扣，史上最牛逼的哦！");
        act.setAdmin("admin");

        JsonObject jso1 = new JsonObject();
        spActivityService.updateSpActivity(jso1,act);
    }


    /**
     * 通过活动 实体参数获取对应的实体列表包含物理分页
     *
     */
  //  @Test
    public void getSpActivityPaperByObj() {
        JsonObject  json = new JsonObject();
        ActivityBean act = new ActivityBean();
        PageInfo pageInfo = new PageInfo();
        spActivityService.getSpActivityPaperByObj(json, act,pageInfo );
    }

}