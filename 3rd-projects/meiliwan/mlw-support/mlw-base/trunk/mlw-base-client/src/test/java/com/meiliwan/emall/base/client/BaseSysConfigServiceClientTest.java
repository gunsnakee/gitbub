package com.meiliwan.emall.base.client;

import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

import org.testng.annotations.Test;

/**
 * 系统基础配置项管理 ICE Client
 * Created by yiyou.luo on 13-6-7.
 */
public class BaseSysConfigServiceClientTest {


    /**
     * 通过 配置编码 获得配置值
     * @return
     */
   // @Test
    public void getSysValueSysConfigByCode(){
        BaseSysConfigServiceClient.getSysValueSysConfigByCode("transport_price_min");
    }


   // @Test
    public void updateSysConfig(){
        BaseSysConfig config = new BaseSysConfig();
        config.setSysConfigId(3);
        config.setSysConfigValue("88888");
        BaseSysConfigServiceClient.updateSysConfig(config);
    }



   // @Test
   public void getJedisTool() throws  Exception{
        ShardJedisTool tool = ShardJedisTool.getInstance();
        tool.get(JedisKey.base$sys,"2313");
   }

    @Test
    public void getSeoListByEntityAndPageInfo() throws  Exception{
        BaseSysConfig config = new BaseSysConfig();
        BaseSysConfigServiceClient.getSeoListByEntityAndPageInfo(new BaseSysConfig(),new PageInfo()) ;
    }



}
