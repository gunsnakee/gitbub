package com.meiliwan.emall.pms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.pms.BaseTest;
import com.meiliwan.emall.pms.bean.ProEnterprisePurchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-21
 * Time: 下午4:05
 * To change this template use File | Settings | File Templates.
 */
public class ProEnterprisePurchaseServiceTest extends BaseTest {
    @Autowired
    ProEnterprisePurchaseService proEnterprisePurchaseService;

    /**
     * 通过  /**
     * 通过 企业采购 实体参数获取对应的实体列表包含物理分页
     *
     */
    @Test
    public void getProEnterprisePurchasePaperByObj() {
        JsonObject json = new JsonObject();
        ProEnterprisePurchase proEnterprisePurchase = new ProEnterprisePurchase();
        proEnterprisePurchase.setEnterprise("旁氏集团");
        proEnterprisePurchase.setContact("李小姐");
        proEnterprisePurchase.setContactPhone("07713219874");
        PageInfo pageInfo = new PageInfo();
        proEnterprisePurchaseService.getProEnterprisePurchasePaperByObj(json,proEnterprisePurchase,pageInfo);
    }
}
