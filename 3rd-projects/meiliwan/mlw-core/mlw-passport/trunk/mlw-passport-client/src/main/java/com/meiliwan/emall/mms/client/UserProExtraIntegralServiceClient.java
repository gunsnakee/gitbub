package com.meiliwan.emall.mms.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserProExtraIntegral;

import java.util.List;


/**
 * 商品额外赠分  client
 * Created by yiyou.luo on 13-6-7.
 */
public class UserProExtraIntegralServiceClient {
    private static final String SERVICE_NAME = "userProExtraIntergralService";


    public static void main(String args[]) {
        //1、创建数据
       /* UserProExtraIntegral pti = new UserProExtraIntegral();
        pti.setProId(12313213);
        pti.setCreateTime(new Date());
        pti.setEndTime(new Date());
        pti.setIsDel(0);
        pti.setValue(88);*/
       /* Integer newId = saveUserProExtraIntegral(pti);
       */ //System.out.println("额外积分的主键id="+newId);

      //  12313213
        //2、根据pro_id拿到数据
         //List<UserProExtraIntegral> ptiList = getUserProExtraIntegralByProId(12313213) ;
         // System.out.println("getbyProId.size=" + ptiList.size());

        //3、修改地域
        //   area2.setAreaName(area2.getAreaName() + "修改地域");
        //  updateArea(area2);

        //4、根据上级编码获取地域
        // System.out.println("getAreasByParentCode=" + getAreasByParentCode("0").size());

        //5、根据地域级别获取编码
        // System.out.println("getAreasByGrade=" + getAreasByGrade("1").size());
        UserProExtraIntegral userProExtraIntegral = new UserProExtraIntegral();
        userProExtraIntegral.setId(1);
        userProExtraIntegral.setValue(10);
        updateUserProExtraIntegral(userProExtraIntegral);


    }

    /**
     * 通过id获取商品额外赠分实例
     * @param id
     */
    public static UserProExtraIntegral getUserProExtraIntegralById( String id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME+"/getProExtraIntegraeById", id));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<UserProExtraIntegral>() {
        }.getType());
    }

    /**
     * 通过商品id获取商品额外赠分实例
     * @param proId
     */
    public static UserProExtraIntegral getUserProExtraIntegralByProId(Integer proId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME+"/getUserProExtraIntegralByProId", proId));
        List<UserProExtraIntegral> list = new Gson().fromJson(obj.get("resultObj"),
                new TypeToken<List<UserProExtraIntegral>>() {
                }.getType());
        if (list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 添加商品额外赠分
     *
     * @param userProExtraIntegral
     */
    public static Integer saveUserProExtraIntegral(UserProExtraIntegral userProExtraIntegral) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME+"/saveUserProExtraIntegral", userProExtraIntegral));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改商品额外赠分
     *
     * @param userProExtraIntegral
     *
     */
    public static boolean updateUserProExtraIntegral(UserProExtraIntegral userProExtraIntegral) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME+"/updateUserProExtraIntegral", userProExtraIntegral));
                boolean result = false;
                if(obj.get("resultObj")!=null && "1".equals(obj.get("resultObj").getAsString())) {
                    result = true;
                }
        return result;
    }
}
