package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.bean.UserCollect;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-19
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class UserCollectServiceTest extends BaseTest {
    @Autowired
    UserCollectService userCollectService;

    /**
     * 通过 用户id获取 用户收藏 list
     */
   /* @Test
    public void getUserCollectByUId() {
        JsonObject json = new JsonObject();
        userCollectService.getUserCollectByUId(json,213131415);
    }*/

    /**
     * 通过 用户收藏 实体参数获取对应的实体列表包含物理分页
     */
   /* @Test
    public void getUserCollectPaperByObj() {
        JsonObject json = new JsonObject();
        UserCollect userCollect = new UserCollect();
        userCollect.setUid(123141414);
        userCollect.setProName("商品名");
        PageInfo  pageInfo = new PageInfo();
        userCollectService.getUserCollectPaperByObj(json,userCollect,pageInfo);
    }*/

    /**
     * 通过商品id 更改商品名称
     */
  /* @Test
    public void updateProNameByProId(){
        JsonObject json = new JsonObject();
        userCollectService.updateProNameByProId(json,"修改testng",8034247);
    }*/



    /**
     * 通过商品id 和 用户id获取用户收藏
     */
 //   @Test
    @Test(invocationCount = 100, threadPoolSize = 50)
    @Parameters({"text"})

    public void getUserCollectByUIdAndProId(){
        JsonObject json = new JsonObject();
        userCollectService.getUserCollectByUIdAndProId(json, 123134435,342881320);
        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa------------------>");
    }


    /**
     * 通过用户id获取前num条收藏
     */
   // @Test
    public void getListByUidAndNun(){
        JsonObject json = new JsonObject();
        userCollectService.getListByUidAndNun(json,12313,2);
    }


}
