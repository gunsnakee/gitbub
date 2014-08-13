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

import java.util.Date;

/**
 * 用户积分  client
 * Created by yiyou.luo on 13-07-03.
 */
public class UserIntegralDetailServiceClient {
    private static final String SERVICE_NAME = "userIntegralDetailService";

    /**
     * 通过类目积分 实体参数获取对应的实体列表包含物理分页
     * @param dto
     * @param pageInfo
     * @return
     */
    public static PagerControl<UserIntegralDetail> getUserIntegralDetailPaperByObj(UserIntegralDetailDto dto, PageInfo pageInfo) {
        JsonObject obj = null;
        obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserIntegralDetailPaperByObj", dto, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserIntegralDetail>>() {
        }.getType());
    }

    public  static void main(String args[]){
       //4、获取分页
        UserIntegralDetailDto dto=new UserIntegralDetailDto();
        dto.setEndTime(new Date());
       System.out.println("4、获取分页 条数="+getUserIntegralDetailPaperByObj(dto, new PageInfo()).getEntityList().size());

    }
}
