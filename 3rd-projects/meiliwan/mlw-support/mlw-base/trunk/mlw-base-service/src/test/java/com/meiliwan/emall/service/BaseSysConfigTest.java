package com.meiliwan.emall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.BaseTest;
import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.base.service.BaseSysConfigService;
import com.meiliwan.emall.commons.PageInfo;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-19
 * Time: 下午2:22
 * To change this template use File | Settings | File Templates.
 */
public class BaseSysConfigTest extends BaseTest {
    @Autowired
    BaseSysConfigService baseSysConfigService;

    /**
     *
     */
   /* @Test
    public void testSaveSysConfig(){
        BaseSysConfig config = new BaseSysConfig();
        config.setSysConfigCode("test");
        config.setSysConfigValue("testValue");
        config.setSysConfigName("测试名");
        config.setDescp("testNg测试");
        JsonObject json = new JsonObject();
        baseSysConfigService.saveSysConfig(json,config);
    }*/

    /**
     * 通过 配置编码 获得配置对象
     */
    //@Test
    public void TestGetSysConfigByCode() {
        JsonObject json = new JsonObject();
        baseSysConfigService.getSysConfigByCode(json,"test");
    }
    /**
     * 通过配置项 实体参数获取对应的实体列表包含物理分页
     *
     */
   // @Test
    public void getSysConfigPaperByObj() {
        JsonObject  json = new JsonObject();
        BaseSysConfig baseSysConfig = new BaseSysConfig();
        baseSysConfig.setSysConfigName("test");
        baseSysConfig.setSysConfigValue("yyy");
        baseSysConfig.setSysConfigCode("cd");
        PageInfo pageInfo = new PageInfo();
        baseSysConfigService.getSysConfigPaperByObj(json, baseSysConfig,pageInfo );
    }

    //@Test
    public void getSysValueSysConfigByCode(){
        JsonObject  json = new JsonObject();
        baseSysConfigService.getSysValueSysConfigByCode(json,"transport_price_min");

    }

    /**
     * 通过配置项 实体参数获取对应的 SEO相关 实体列表包含物理分页
     *
     */
    @Test
    public void getSeoListByEntityAndPageInfo() {
        JsonObject  json = new JsonObject();
        BaseSysConfig baseSysConfig = new BaseSysConfig();
       /* baseSysConfig.setSysConfigName("test");
        baseSysConfig.setSysConfigValue("yyy");
        baseSysConfig.setSysConfigCode("cd");*/
        PageInfo pageInfo = new PageInfo();
        baseSysConfigService.getSeoListByEntityAndPageInfo(json, baseSysConfig,pageInfo );
    }

}
