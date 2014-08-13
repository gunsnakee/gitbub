package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.mms.BaseTest;
import com.meiliwan.emall.mms.bean.UserExtra;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;

/**
 * Created by jiawuwu on 13-8-16.
 */
public class UserExtraServiceTest extends BaseTest {
    @Autowired
    UserExtraService userExtraService;


    @Test
    public void testAdd() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {
        JsonObject resultObj = new JsonObject();
        UserExtra extra = new UserExtra();
        extra.setUid(3);
        extra.setSex((short)2);
        extra.setCountryCode("1000");
        extra.setCountryName("中国");
        extra.setProvinceCode("100022");
        extra.setProvinceName("吉林省");
        extra.setCityCode("10002202");
        extra.setCityName("吉林市");
        extra.setAreaCode("1000220203");
        extra.setCityName("龙潭区");
        extra.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse("1949-10-01"));
        userExtraService.update(resultObj,extra);
    }

    @Test
    public void testGetExtraByUid() throws Exception {
        JsonObject resultObj = new JsonObject();
        Integer uid = 3;
        userExtraService.getExtraByUid(resultObj,uid);
    }
}
