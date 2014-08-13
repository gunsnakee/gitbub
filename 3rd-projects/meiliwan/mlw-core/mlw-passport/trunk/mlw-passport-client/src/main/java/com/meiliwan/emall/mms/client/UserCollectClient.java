package com.meiliwan.emall.mms.client;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.util.TypeUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.mms.bean.UserCollect;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 *  用户收藏 client
 * Created by yiyou.luo on 13-6-16.
 */
public class UserCollectClient {
    private static final String SERVICE_NAME = "userCollectService";

    //灌数据到商品收藏表中
    /*
        商品id：10235100  ----- 15247058
        用户id：100000000 -- 104839350
         */
    private static void batchAddCollect(){
        UserCollect collect = new UserCollect();
        collect.setNickName("Jeck");
        for(int i=0; i<5000000; i++){
            collect.setUid(new Random().nextInt(4839350)+100000000);
            collect.setProId(new Random().nextInt(5011958)+10235100);
            collect.setProName("测试商品5000000条第"+i);
           System.out.println(saveUserCollect(collect));
        }


    }

    public static void main(String args[]){

        batchAddCollect();

        // 1、通过id获取 用户收藏 实例

        /*UserCollect collect = new UserCollect();
        collect.setUid(new Random().nextInt(4839350)+100000000);
        collect.setNickName("Jeck");
        collect.setProId(new Random().nextInt(5011958)+10235100);
        collect.setProName("测试商品5000000条")   ;
        int id = saveUserCollect(collect);*/

        /*// 2。通过 id获取 用户收藏 list
        UserCollect gCollect = getUserCollectById(id);
        System.out.println(gCollect.getNickName() + "于：" + gCollect.getCreateTime() + " 收藏： " + gCollect.getProName());

        //3。修改 用户收藏
        gCollect.setProName("泰国金枕榴莲No.2");
        System.out.println("修改结果：" + updateUserCollect(gCollect));

        //4.通过 用户id获取 用户收藏 list
        List<UserCollect> list = getUserCollectByUId(gCollect.getUid());
        System.out.println("用户收藏的条数：list.size=" + list.size());
        //5.
        PageInfo pageInfo  = new PageInfo();
        UserCollect pcollect = new UserCollect();
        System.out.println("分页查到数据条数=" + getUserCollectPaperByObj(pcollect, pageInfo).getEntityList().size());
        //6.通过商品id 更改商品名称
        System.out.println( "update result="+updateProNameByProId("泰国金枕榴莲No3",12313213));*/

      //  List  list = getListByUidAndNun(12313,4);
      //  System.out.print(list.size());
       // getUserCollectByUIdAndProId(3, 10235104);
    }
    /**
     * 通过id获取 用户收藏 实例
     * @param id
     */
    public static UserCollect getUserCollectById(int id) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getUserCollectById", id));
            return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<UserCollect>() {
            }.getType());
    }

    /**
     * 通过 用户id获取 用户收藏 list
     * @param uid
     */
    @IceServiceMethod
    public static List<UserCollect> getUserCollectByUId(int uid) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                JSONTool.buildParams(SERVICE_NAME + "/getUserCollectByUId", uid));
        return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserCollect>>() {
        }.getType());
    }

    /**
     * 添加 用户收藏
     *
     * @param UserCollect
     */
    @IceServiceMethod
    public static int  saveUserCollect(UserCollect UserCollect) {
        if (UserCollect !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/saveUserCollect", UserCollect));
            return Integer.parseInt(obj.get("resultObj").getAsString());
        } else{
            return -1;
        }
    }

    /**
     * 修改 用户收藏
     *
     * @param UserCollect
     */
    @IceServiceMethod
    public static boolean updateUserCollect(UserCollect UserCollect) {
        if (UserCollect !=null){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/updateUserCollect", UserCollect));
            return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();

        } else {
            return false;
        }
    }
    /**
     * 通过id删除收藏
     * @param id
     */
    public static Integer delCollect(Integer id){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/delCollect", id));
            return obj.get("resultObj").getAsInt();
    }

    /**
     * 通过  /**
     * 通过 用户收藏 实体参数获取对应的实体列表包含物理分页
     *
     * @param pageInfo
     */
    @IceServiceMethod
    public static PagerControl getUserCollectPaperByObj(UserCollect UserCollect, PageInfo pageInfo) {
        if (UserCollect != null && pageInfo != null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getUserCollectPaperByObj", UserCollect, pageInfo));
            return  new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<UserCollect>>() {
            }.getType());
        } else{
            return null;
        }
    }

    /**
     * 通过商品id 更改商品名称
     * @param proName
     * @param proId
     */
    @IceServiceMethod
    public static boolean updateProNameByProId(String proName, Integer proId){

        if (StringUtils.isNotEmpty(proName) && proId>0){
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/updateProNameByProId", proName, proId));
           return obj.get("resultObj") == null ? false : obj.get("resultObj").getAsBoolean();

        } else {
            return false;
        }
    }

    /**
     * 通过商品id 和 用户id获取用户收藏
     * @param proId
     * @param uid
     */
    public static UserCollect getUserCollectByUIdAndProId( Integer uid, Integer proId){
        if (uid>0 && proId>0) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getUserCollectByUIdAndProId",uid, proId));
           List<UserCollect> list = new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserCollect>>() {}.getType());
           if (list!=null && list.size()>0){
               return list.get(0);
           }
        }
        return null;
    }

    /**
     * 通过用户id获取最近前num条收藏
     * @param uid
     * @param num
     */
    public static List<UserCollect> getListByUidAndNun(Integer uid, Integer num){
        List<UserCollect> list =  new ArrayList<UserCollect>();
        if (uid>0 && num>0) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.MMS_ICE_SERVICE,
                    JSONTool.buildParams(SERVICE_NAME + "/getListByUidAndNun",uid, num));
           list = new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<UserCollect>>() {}.getType());
        }
        return list;
    }
}
