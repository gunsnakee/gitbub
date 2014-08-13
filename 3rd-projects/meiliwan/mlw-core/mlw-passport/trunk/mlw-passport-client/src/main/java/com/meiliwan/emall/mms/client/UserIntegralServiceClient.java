package com.meiliwan.emall.mms.client;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserIntegral;
import com.meiliwan.emall.mms.dto.OrderInteralDto;
import com.meiliwan.emall.mms.dto.OrderItemIntegralDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户积分  client
 * Created by yiyou.luo on 13-6-7.
 */
public class UserIntegralServiceClient {
    private static final String SERVICE_NAME = "userIntegralService";

    /**
     * 通过id获取类目规则实例
     *
     * @param id
     */
    public static UserIntegral getUserIntegralById(String id) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserIntegralById", id));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<UserIntegral>() {
        }.getType());
    }

    /**
     * 通过用户id获实例
     *
     * @param uid
     */
    public static List<UserIntegral> getUserIntegralByUid(String uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserIntegralByUid", uid));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserIntegral>>() {
        }.getType());
    }

    /**
     * 添加用户积分
     *
     * @param integral
     */
    public static Integer saveUserIntegral(UserIntegral integral){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/saveUserIntegral", integral));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改用户积分
     *
     * @param integral
     */
    public static boolean updateUserIntegral(UserIntegral integral) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/updateUserIntegral", integral));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }

    /**
     * 通过类目积分 实体参数获取对应的实体列表包含物理分页
     *
     * @param userIntegral
     * @param pageInfo
     */
    public static PagerControl<UserIntegral> getUserIntegralPaperByObj(UserIntegral userIntegral, PageInfo pageInfo) {
        JsonObject obj = null;
        obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserIntegralPaperByObj", userIntegral, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserIntegral>>() {
        }.getType());
    }

    /**
     * 下单 送积积分（添加积分）
     * 包含：商品额外赠分 和 类目积分
     * @param orderInteralDto
     * * @param uid  用户id
     * @return
     */
    public static boolean  addCreateOrderIntegral(OrderInteralDto orderInteralDto,Integer uid){
        JsonObject obj = null;
        obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/addCreateOrderIntegral", orderInteralDto, uid));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }


    public  static void main(String args[]){
       /* //1、插入数据
         UserIntegral integral = new UserIntegral();
         integral.setUid(12313);
         integral.setCreateTime(new Date());
         integral.setIntegralBalance(20);
         integral.setIsDel(GlobalNames.STATE_VALID);
        int id =saveUserIntegral(integral) ;
        System.out.println("1、integral.id="+id);

        //2、通过id查找
        UserIntegral findIntegral = getUserIntegralById(id+"");
        System.out.println("2、通过id查找="+findIntegral.toString());

        //3、通过用户id获取积分
        System.out.println("3、通过用户id获取积分.size="+getUserIntegralByUid("12313").size());
       //4、获取分页
       System.out.println("4、获取分页 条数="+getUserIntegralPaperByObj(new UserIntegral(), new PageInfo()).getEntityList().size());

       //5、更新
        findIntegral.setIntegralBalance(88);
       System.out.println( "5、更新="+updateUserIntegral(findIntegral));*/


       //6、下订单送积分多种方法测试
        OrderInteralDto orderInteralDto =new OrderInteralDto();
        //1、设置订单号
        orderInteralDto.setOrderId("0000000682");

        //2、构建订单行
        OrderItemIntegralDto itemIntegralDto = new OrderItemIntegralDto();
        itemIntegralDto.setProAmount(20);

        itemIntegralDto.setProId(10235398); //      10264568   10235398
        itemIntegralDto.setSaleNum(1);

        itemIntegralDto.setCategoryId(65);

        List<OrderItemIntegralDto> items = new ArrayList<OrderItemIntegralDto>();
        items.add(itemIntegralDto);

        //3、设置订单行
        orderInteralDto.setItem(items);
       System.out.print(addCreateOrderIntegral(orderInteralDto, 30));
    }
}
