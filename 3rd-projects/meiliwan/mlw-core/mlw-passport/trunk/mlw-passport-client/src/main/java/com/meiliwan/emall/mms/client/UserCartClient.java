package com.meiliwan.emall.mms.client;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserCart;
import com.meiliwan.emall.service.BaseService;

import java.util.List;

/**
 * Created by Sean on 13-6-14.
 */
public class UserCartClient {

    public static enum Operator {
        /**
         * 同步操作
         */
        Merge,
        /**
         * 添加操作
         */
        Add,
        /**
         * 添加操作
         */
        Set,

        /**
         * 获取操作
         */
        Get,
        /**
         * 删除操作
         */
        Del,
        /**
         * 下订单成功
         */
        AddToOrderSuccess
    }

    /**
     * 用户操作购物车客户端 接口
     *
     * @param operator       操作类型
     * @param cartIds        操作购物车的Ids
     * @param countArgs      对应商品的数量
     * @param userId         操作用户的Id
     * @param isReturnResult 是否要返回操作后的购物车结果
     * @return
     */
    public static List<UserCart> operatorCart(Operator operator, List<String> cartIds, List<String> countArgs,List<Long> addTime,
                                              int userId, boolean isReturnResult) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams("userCartService/updateUserCart", operator, cartIds, countArgs,addTime, userId, isReturnResult));
        return new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<UserCart>>() {
        }.getType());
    }


    public static void main(String args[]) {
//        List<UserCart> list = operatorCart(Operator.Del, new int[]{4}, new int[]{1}, 1, true);
//        if (list != null) {
//            for (UserCart userCart : list) {
//                System.out.println(userCart);
//            }
//        }
    }
}
