
package com.meiliwan.emall.sp2.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 活动商品 client
 *
 * @author yiyou.luo
 *         2013-12-25
*/

public class ActivityProductClient {
    private static MLWLogger logger = MLWLoggerFactory.getLogger(ActivityProductClient.class);
    /**
     * 根据活动商品ID获得活动商品对象
     *
     * @param actId
     */
    public static ActivityProductBean getSpActivityProductById(int actId) {
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/getSpActivityProductById", actId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<ActivityProductBean>() {
        }.getType());
    }

    /**
     * 添加活动商品
     *
     * @param spActivityProduct
     */
    public static Integer saveSpActivityProduct(ActivityProductBean spActivityProduct) {
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/saveSpActivityProduct", spActivityProduct));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }

    /**
     * 修改活动商品
     *
     * @param spActivityProduct
     */
    public static boolean updateSpActivityProduct(ActivityProductBean spActivityProduct) {
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/updateSpActivityProduct", spActivityProduct));
        return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();
    }


    /**
     * 通过活动商品 实体参数获取对应的实体列表包含物理分页
     *
     * @param spActivityProduct
     * @param pageInfo
     */
    public static PagerControl<ActivityProductBean> getSpActivityProductPaperByObj(ActivityProductBean spActivityProduct, PageInfo pageInfo) {
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/getSpActivityProductPaperByObj",spActivityProduct,pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<ActivityProductBean>>() {
        }.getType());
    }

    /**
     * 根据商品ids检查商品是否处于有效（创建、上架）活动的内
     * @param startTime
     * @param endTime
     * @param proIds
     * @return  返回被占用的商品id List
     */
    public static List<Integer> checkActUsingProId(Date startTime, Date endTime,List<Integer> proIds){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/checkActUsingProId",startTime,endTime,proIds));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<Integer>>() {
        }.getType());
    }

    /**
     * 批量保存活动商品
     * @param actProList   活动商品list
     * @return  返回添加不成功的List<ActivityProductBean>
     */

    public static List<ActivityProductBean> saveSpActivityProducts(List<ActivityProductBean> actProList, Date startTime, Date endTime){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/saveSpActivityProducts",actProList,startTime,endTime));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<ActivityProductBean>>() {
        }.getType());
    }

    /**
     * 根据活动ids 获取每个活动的商品数量
     * @param actIds
     * @return
     */
    public static Map<Integer,Integer> getActProsByActIds(List<Integer> actIds){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/getActProsByActIds",actIds));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<Map<Integer,Integer>>() {
        }.getType());
    }

    /**
     * 根据id删除活动商品
     * @param actProId
     */
    public static Integer  deleteActProById(Integer actProId){
        JsonObject obj=null;
        obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/deleteActProById", actProId));
        return Integer.parseInt(obj.get("resultObj").getAsString());
    }



}




