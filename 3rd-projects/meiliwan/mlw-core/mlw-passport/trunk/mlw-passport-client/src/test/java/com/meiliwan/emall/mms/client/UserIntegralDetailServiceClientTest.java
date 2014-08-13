package com.meiliwan.emall.mms.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserIntegralDetail;
import com.meiliwan.emall.mms.dto.UserIntegralDetailDto;
import org.testng.annotations.Test;

import java.util.Date;

/**
 * 用户积分明细  client压测类
 * Created by yiyou.luo on 13-09-6.
 */
public class UserIntegralDetailServiceClientTest {

    /**
     * 通过类目积分 实体参数获取对应的实体列表包含物理分页
     */
    @Test(invocationCount = 100, threadPoolSize = 5)
    public void getUserIntegralDetailPaperByObj() {
        UserIntegralDetailDto dto = new UserIntegralDetailDto();
        dto.setUid(12241321);
        UserIntegralDetailServiceClient.getUserIntegralDetailPaperByObj(dto,new PageInfo());
    }

}
