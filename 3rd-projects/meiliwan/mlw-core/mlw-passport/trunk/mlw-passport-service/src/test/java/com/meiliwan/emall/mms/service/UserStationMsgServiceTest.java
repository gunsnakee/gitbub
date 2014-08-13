package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.bean.UserStationMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-19
 * Time: 下午4:50
 * To change this template use File | Settings | File Templates.
 */
public class UserStationMsgServiceTest extends BaseTest {
    @Autowired
    UserStationMsgService userStationMsgService;


    /**
     * 通过 用户id获取 站内信 list
     */
    @Test
    public void getUserStationMsgByUId() {
        JsonObject json = new JsonObject();
        userStationMsgService.getUserStationMsgByUId(json,12314141);
    }



    /**
     * 获取某个用户的某个公告
     */
    @Test
    public void getUserPublicMsg() {
        JsonObject json = new JsonObject();
        userStationMsgService.getUserPublicMsg(json, 12313, 1234567);
    }

    /**
     * 通过  /**
     * 通过 站内信 实体参数获取对应的实体列表包含物理分页
     */
    @Test
    public void getUserStationMsgPaperByObj() {
        JsonObject json = new JsonObject();
        UserStationMsg userStationMsg = new  UserStationMsg();
        userStationMsg.setUid(3123133);
        userStationMsg.setNickName("老五");
        userStationMsg.setAdminName("郭德纲");
        PageInfo pageInfo = new PageInfo();
        userStationMsgService.getUserStationMsgPaperByObj(json, userStationMsg, pageInfo);
    }

    /**
     * 通过  /**
     * 通过 获取用户站内信数量
     */
    @Test
    public void getUserStationMsgNumByObj() {
        JsonObject  json = new JsonObject();
        UserStationMsg userStationMsg = new UserStationMsg();
        userStationMsg.setUid(121512413);
        userStationMsgService.getUserStationMsgNumByObj(json,userStationMsg);
    }



}
