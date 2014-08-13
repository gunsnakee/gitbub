package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.mms.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-21
 * Time: 下午3:48
 * To change this template use File | Settings | File Templates.
 */
public class UserProExtraIntergralServiceTest extends BaseTest {
    @Autowired
    UserProExtraIntergralService userProExtraIntergralService;

    /**
     * 通过商品id获取商品额外赠分实例
     */
    /*@Test
    public void getUserProExtraIntegralByProId() {
        JsonObject json = new JsonObject();
        userProExtraIntergralService.getUserProExtraIntegralByProId(json,1231313);
    }*/


   @Test
    public void getExtraIntegralByProId() {
        userProExtraIntergralService.getExtraIntegralByProId(1343445);
    }
}
