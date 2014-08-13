package com.meiliwan.emall.sp2.client;



import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.bean.ActivityProductBean;
import com.meiliwan.emall.sp2.bean.view.SimpleActVO;
import com.meiliwan.emall.sp2.cache.ActCacheTool;
import com.meiliwan.emall.sp2.constant.ActType;
import com.meiliwan.emall.sp2.constant.PrivilegeType;
import com.meiliwan.emall.sp2.activityrule.helper.ActivityRuleHelper;
import com.meiliwan.emall.sp2.util.ActivityUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 活动通用的对外接口
 * Created by xiongyu on 13-12-24.
 */
public class ActivityInterfaceClient {

    private static final MLWLogger logger = MLWLoggerFactory.getLogger(ActivityInterfaceClient.class);

    /**
     * 根据商品id得到活动(使用场景：商品详情页, 购物车)
     * 单品优惠: 直降 单品商品送（积分、赠品、券）
     * 活动优惠：满减、满送（积分、赠品、券）
     * @param proId
     * @return

    public static Map<PrivilegeType,List<SimpleActVO>> getActivitiesByProId(int proId) {

        List<ActivityProductBean> list = getActivityProductsByProId(proId);

        //要返回的活动Map对象
        Map<PrivilegeType,List<SimpleActVO>> activitiesMap = null;
        if(list!=null && !list.isEmpty()){
            activitiesMap = new HashMap<PrivilegeType,List<SimpleActVO>>();
            for(ActivityProductBean sap : list){
                ActVO actVO = getActVOByActId(sap.getActId());
                SimpleActVO simpleActVO = new SimpleActVO();
                simpleActVO.setActId(actVO.getActId());
                simpleActVO.setActDesc(actVO.getActDesc());
                simpleActVO.setActName(actVO.getActName());
                simpleActVO.setActType(actVO.getActType());

                List<SimpleActVO> simpleActVOs = activitiesMap.get(actVO.getPrivilegeType());
                if(simpleActVOs == null){
                    simpleActVOs = new ArrayList<SimpleActVO>();
                    activitiesMap.put(actVO.getPrivilegeType(), simpleActVOs);
                }
                simpleActVOs.add(simpleActVO);
            }
        }
        return activitiesMap;
    }*/


    /**
     * 根据商品id得到商品活动关系表数据(使用场景：商品详情页)
     * 单品优惠: 直降 单品商品送（积分、赠品、券）
     * 活动优惠：满减、满送（积分、赠品、券）
     * @param proId
     * @return
     */
    public static List<ActivityProductBean> getActivityProductsByProId(int proId) {

        Map<Integer,List<ActivityProductBean>> map = ActCacheTool.getActInfoByProIds(proId);
        if(map != null && !map.isEmpty()) {
            List<ActivityProductBean> apbs = map.get(proId);
            if(apbs != null && !apbs.isEmpty()){
                return apbs;
            }
        }

        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("activityProductService/getAllByProIdForUpAct", proId));

        List<ActivityProductBean> apList = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ActivityProductBean>>() {
        }.getType());
        if(apList!=null && apList.size()>0){
            ActCacheTool.addActPro(apList);
        }
        return apList;
    }

    /**
     * 根据商品ids 获取商品的直降价格
     * 用于全站非搜索商品的显示业务
     * @param proIds
     * @return   resultMap<商品id,直降价格>
     */
    public static  Map<Integer,BigDecimal> getActProByProIdsForShowPrice(int... proIds) {
        Map<Integer,BigDecimal> resultMap = new HashMap<Integer, BigDecimal>();
        try{
            Map<Integer,List<ActivityProductBean>> map = ActCacheTool.getActInfoByProIds(proIds);
            if(map != null && map.size()>0 && proIds.length>0) {
                    for(int proId : proIds){
                    List<ActivityProductBean> actPros = map.get(proId);
                    if(actPros!=null && actPros.size()>0){
                        ActivityProductBean apb = actPros.get(0);
                        if(apb!=null && apb.getIsPriceDown().intValue() ==0){ //必须是直降类
                            resultMap.put(proId,apb.getActPrice());
                        }
                    }
                }
                return resultMap;
            }

            if( proIds.length>0) {
                for(int proId : proIds){
                    JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                            JSONTool.buildParams("activityProductService/getAllByProIdForUpAct", proId));
                    List<ActivityProductBean> apList = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<List<ActivityProductBean>>() {
                    }.getType());
                    if(apList!=null && apList.size()>0){
                        ActivityProductBean apb = apList.get(0);
                        if(apb!=null && apb.getIsPriceDown().intValue() ==0){ //必须是直降类
                            resultMap.put(proId,apb.getActPrice());
                        }
                    }
                }
            }
        }catch (Exception e){
           logger.error(e,proIds,null);
        }
        return resultMap;
    }


    /**
     * 根据商品id集合得到活动(使用场景：商品详情页, 购物车)
     * 单品优惠: 直降 单品商品送（积分、赠品、券）
     * 活动优惠：满减、满送（积分、赠品、券）
     * 返回的数据结果：
     * Map<商品ID, Map<优惠类型,List<SimpleActVO>>>
     * @param proIds
     * @return
     */
    public static Map<Integer, Map<PrivilegeType,List<SimpleActVO>>> getSimpleActivitiesByProIds(int[] proIds) {
        if(proIds == null) return null;

        //1、第一轮先去缓存里拿已经缓存过的商品ID-活动商品数据
        Map<Integer, List<ActivityProductBean>> sapMap = new HashMap<Integer, List<ActivityProductBean>>();
        //缓存里不存在，需要另外查库的商品ID集合
        List<Integer> proIdList = new ArrayList<Integer>();
        for(int proId : proIds){
            Map<Integer,List<ActivityProductBean>> map = ActCacheTool.getActInfoByProIds(proId);
            if(map != null && !map.isEmpty()){
                //如果该商品ID没有参与任何活动，则该商品ID对应的值为一个空的List对象；
                List<ActivityProductBean> apbs = map.get(proId);
                if(apbs != null && !apbs.isEmpty()){
                    sapMap.put(proId, apbs);
                }else{
                    sapMap.put(proId, null);
                    proIdList.add(proId);
                }
            }else{
                sapMap.put(proId, null);
                proIdList.add(proId);
            }
        }
        //2、第二轮去库里查询商品ID-活动商品数据
        if(proIdList != null && !proIdList.isEmpty()){
            int[] proIds2 = new int[proIdList.size()];
            int i=0;
            for(int proId : proIdList){
                proIds2[i] = proId;
                i++;
            }

            //1、 走Service端的商品-活动批量查询接口，获取每个商品与该商品参加活动集合的Map数据结果。Map<Integer, List<ActivityProductBean>>
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                    JSONTool.buildParams("activityProductService/getAllByProIdsForUpAct", proIds2));

            Map<Integer, List<ActivityProductBean>> sapMap2 = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<Map<Integer, List<ActivityProductBean>>>() {
            }.getType());
            //将缓存和库里的商品ID-活动商品数据合并
            if(sapMap2 != null){
                Collection<List<ActivityProductBean>> coll = sapMap2.values();
                for(List<ActivityProductBean> list : coll){
                    ActCacheTool.addActPro(list);
                }
                sapMap.putAll(sapMap2);
            }
        }


        //要返回的商品ID集合-活动集合的结果Map
        Map<Integer, Map<PrivilegeType,List<SimpleActVO>>> activitiesMap = null;
        if(sapMap!=null && !sapMap.isEmpty()){

            activitiesMap = new HashMap<Integer, Map<PrivilegeType,List<SimpleActVO>>>();

            //2、 for循环商品ID-活动List的Map，将每个活动List里的活动对象转化做ActVO
            Set<Map.Entry<Integer, List<ActivityProductBean>>> sapEntrySet = sapMap.entrySet();
            for (Map.Entry<Integer, List<ActivityProductBean>> me : sapEntrySet) {
                //每个商品ID的活动Map
                Map<PrivilegeType,List<SimpleActVO>> proActivitiesMap = new HashMap<PrivilegeType,List<SimpleActVO>>();
                //3、循环遍历活动的集合，将每个活动转化做ActVO
                List<ActivityProductBean> sapList = me.getValue();
                if(sapList != null && !sapList.isEmpty()){
                    for(ActivityProductBean sap : sapList){
                        ActivityBean activityBean = getActivityBeanByActId(sap.getActId());
                        if(activityBean!=null){
                            SimpleActVO simpleActVO = new SimpleActVO();
                            simpleActVO.setActId(activityBean.getActId());
                            simpleActVO.setActDesc(activityBean.getActDesc());
                            simpleActVO.setActName(activityBean.getActName());
                            simpleActVO.setActWenan(activityBean.getActWenan());
                            simpleActVO.setActType(ActType.valueOf(activityBean.getActType()));
                            //设置活动规则
                            simpleActVO.setActRule(ActivityRuleHelper.getActRules(activityBean, sap));
                            //优惠类型
                            PrivilegeType pt = PrivilegeType.valueOf(activityBean.getPrivilegeType());
                            List<SimpleActVO> simpleActVOs = proActivitiesMap.get(pt);
                            if(simpleActVOs == null){
                                simpleActVOs = new ArrayList<SimpleActVO>();
                                proActivitiesMap.put(pt, simpleActVOs);
                            }
                            simpleActVOs.add(simpleActVO);
                        }
                    }
                }
                activitiesMap.put(me.getKey(), proActivitiesMap);
            }
        }
        return activitiesMap;
    }


    /**
     * 根据商品id集合得到未开始or正在进行的活动(使用场景：后台优惠券列表)
     * 单品优惠: 直降 单品商品送（积分、赠品、券）
     * 活动优惠：满减、满送（积分、赠品、券）
     * 返回的数据结果：
     * Map<商品ID, Map<优惠类型,List<SimpleActVO>>>
     * @param proIds
     * @return
     */
    public static Map<Integer, Map<PrivilegeType,List<SimpleActVO>>> getSimpleActivitiesByProIdsForSpTicket(int[] proIds) {
        if(proIds == null) return null;

        //1、第一轮先去缓存里拿已经缓存过的商品ID-活动商品数据
        Map<Integer, List<ActivityProductBean>> sapMap = new HashMap<Integer, List<ActivityProductBean>>();
        //缓存里不存在，需要另外查库的商品ID集合
        List<Integer> proIdList = new ArrayList<Integer>();
        for(int proId : proIds){
            Map<Integer,List<ActivityProductBean>> map = ActCacheTool.getVaildActInfoByProIds(proId);
            if(map != null && !map.isEmpty()){
                //如果该商品ID没有参与任何活动，则该商品ID对应的值为一个空的List对象；
                List<ActivityProductBean> apbs = map.get(proId);
                if(apbs != null && !apbs.isEmpty()){
                    sapMap.put(proId, apbs);
                }else{
                    sapMap.put(proId, null);
                    proIdList.add(proId);
                }
            }else{
                sapMap.put(proId, null);
                proIdList.add(proId);
            }
        }
        //2、第二轮去库里查询商品ID-活动商品数据
        if(proIdList != null && !proIdList.isEmpty()){
            int[] proIds2 = new int[proIdList.size()];
            int i=0;
            for(int proId : proIdList){
                proIds2[i] = proId;
                i++;
            }

            //1、 走Service端的商品-活动批量查询接口，获取每个商品与该商品参加活动集合的Map数据结果。Map<Integer, List<ActivityProductBean>>
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                    JSONTool.buildParams("activityProductService/getAllByProIdActIsVaild", proIds2));

            Map<Integer, List<ActivityProductBean>> sapMap2 = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<Map<Integer, List<ActivityProductBean>>>() {
            }.getType());
            //将缓存和库里的商品ID-活动商品数据合并
            if(sapMap2 != null){
                Collection<List<ActivityProductBean>> coll = sapMap2.values();
                for(List<ActivityProductBean> list : coll){
                    ActCacheTool.addActPro(list);
                }
                sapMap.putAll(sapMap2);
            }
        }


        //要返回的商品ID集合-活动集合的结果Map
        Map<Integer, Map<PrivilegeType,List<SimpleActVO>>> activitiesMap = null;
        if(sapMap!=null && !sapMap.isEmpty()){

            activitiesMap = new HashMap<Integer, Map<PrivilegeType,List<SimpleActVO>>>();

            //2、 for循环商品ID-活动List的Map，将每个活动List里的活动对象转化做ActVO
            Set<Map.Entry<Integer, List<ActivityProductBean>>> sapEntrySet = sapMap.entrySet();
            for (Map.Entry<Integer, List<ActivityProductBean>> me : sapEntrySet) {
                //每个商品ID的活动Map
                Map<PrivilegeType,List<SimpleActVO>> proActivitiesMap = new HashMap<PrivilegeType,List<SimpleActVO>>();
                //3、循环遍历活动的集合，将每个活动转化做ActVO
                List<ActivityProductBean> sapList = me.getValue();
                if(sapList != null && !sapList.isEmpty()){
                    for(ActivityProductBean sap : sapList){
                        ActivityBean activityBean = getValidActivityBeanByActId(sap.getActId());
                        if(activityBean!=null){
                            SimpleActVO simpleActVO = new SimpleActVO();
                            simpleActVO.setActId(activityBean.getActId());
                            simpleActVO.setActDesc(activityBean.getActDesc());
                            simpleActVO.setActName(activityBean.getActName());
                            simpleActVO.setActWenan(activityBean.getActWenan());
                            simpleActVO.setActType(ActType.valueOf(activityBean.getActType()));
                            simpleActVO.setStartTime(activityBean.getStartTime().getTime());  //开始时间
                            simpleActVO.setEndTime(activityBean.getEndTime().getTime());   //结束时间
                            //设置活动规则
                            simpleActVO.setActRule(ActivityRuleHelper.getActRules(activityBean, sap));
                            //优惠类型
                            PrivilegeType pt = PrivilegeType.valueOf(activityBean.getPrivilegeType());
                            List<SimpleActVO> simpleActVOs = proActivitiesMap.get(pt);
                            if(simpleActVOs == null){
                                simpleActVOs = new ArrayList<SimpleActVO>();
                                proActivitiesMap.put(pt, simpleActVOs);
                            }
                            simpleActVOs.add(simpleActVO);
                        }
                    }
                }
                activitiesMap.put(me.getKey(), proActivitiesMap);
            }
        }
        return activitiesMap;
    }

    /**
     * 根据商品id集合得到活动(使用场景：商品详情页)
     * 单品优惠: 直降 单品商品送（积分、赠品、券）
     * 活动优惠：满减、满送（积分、赠品、券）
     * @param proIds
     * @return

    public static Map<Integer, Map<PrivilegeType,List<ActVO>>> getActivitiesByProIds(int[] proIds) {

        //1、 走Service端的商品-活动批量查询接口，获取每个商品与该商品参加活动集合的Map数据结果。Map<Integer, List<ActivityProductBean>>
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spActivityProductService/getAllByProIdsForUpAct", proIds));

        Map<Integer, List<ActivityProductBean>> sapMap = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), new TypeToken<Map<Integer, List<ActivityProductBean>>>() {
        }.getType());

        //要返回的商品ID集合-活动集合的结果Map
        Map<Integer, Map<PrivilegeType,List<ActVO>>> activitiesMap = null;
        if(sapMap!=null && !sapMap.isEmpty()){

            activitiesMap = new HashMap<Integer, Map<PrivilegeType,List<ActVO>>>();

            //2、 for循环商品ID-活动List的Map，将每个活动List里的活动对象转化做ActVO
            Set<Map.Entry<Integer, List<ActivityProductBean>>> sapEntrySet = sapMap.entrySet();
            for (Map.Entry<Integer, List<ActivityProductBean>> me : sapEntrySet) {
                //每个商品ID的活动Map
                Map<PrivilegeType,List<ActVO>> proActivitiesMap = new HashMap<PrivilegeType,List<ActVO>>();
                //3、循环遍历活动的集合，将每个活动转化做ActVO
                List<ActivityProductBean> sapList = me.getValue();
                for(ActivityProductBean sap : sapList){
                    ActVO actVO = getActVOByActId(sap.getActId());
                    List<ActVO> actVOs = proActivitiesMap.get(actVO.getPrivilegeType());
                    if(actVOs == null){
                        actVOs = new ArrayList<ActVO>();
                        proActivitiesMap.put(actVO.getPrivilegeType(), actVOs);
                    }
                    actVOs.add(actVO);
                }
                activitiesMap.put(me.getKey(), proActivitiesMap);
            }
        }
        return activitiesMap;
    }*/

    /**
     * 根据活动id得到活动ActVO对象(只返回满足 上架以及开始/结束时间是否在当前时间范围内的活动对象)
     * @param actId
     * @return
    */
    public static ActivityBean getActivityBeanByActId(int actId) {

        ActivityBean activityBean = ActCacheTool.getActivityBean(actId);
        if(activityBean == null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                    JSONTool.buildParams("activityService/getSpActivityById", actId));
            activityBean = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ActivityBean.class);
            ActCacheTool.addAct(activityBean);
        }

        //校验活动是否上架以及开始/结束时间是否在当前时间范围内
        if(ActivityUtils.isOnliningActivity(activityBean)) {
            return activityBean;
        }
        return null;
    }


    /**
     * 根据活动id得到有效活动ActVO对象(只返回满足 1、正在进行。2、上架单未开始)
     * @param actId
     * @return
     */
    public static ActivityBean getValidActivityBeanByActId(int actId) {

        ActivityBean activityBean = ActCacheTool.getActivityBean(actId);
        if(activityBean == null) {
            JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                    JSONTool.buildParams("activityService/getSpActivityById", actId));
            activityBean = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ActivityBean.class);
            ActCacheTool.addAct(activityBean);
        }

        //校验活动是否 正在进行时 或 上架但未开始
        if(ActivityUtils.isUnstartOrOnliningActivity(activityBean)) {
            return activityBean;
        }
        return null;
    }

    public static void main(String[] args){


        List<ActivityProductBean> apList = getActivityProductsByProId(10237943);http://www.meiliwan.com/10235398.html
        for(ActivityProductBean ap : apList){
            logger.debug("ap == "+ap);
        }
/*
        Map<Integer, Map<PrivilegeType,List<SimpleActVO>>> map = getSimpleActivitiesByProIds(new int[]{10235393,10235358,10235357});
        Set<Map.Entry<Integer, Map<PrivilegeType,List<SimpleActVO>>>> sapEntrySet = map.entrySet();

        for (Map.Entry<Integer, Map<PrivilegeType,List<SimpleActVO>>> me : sapEntrySet) {
            System.out.print("proId = " + me.getKey());
            Map<PrivilegeType,List<SimpleActVO>> privilegeTypeListMap = me.getValue();

            Set<Map.Entry<PrivilegeType,List<SimpleActVO>>> innerEntrySet = privilegeTypeListMap.entrySet();

            for (Map.Entry<PrivilegeType,List<SimpleActVO>> innerMe : innerEntrySet) {
                System.out.print(", 优惠类型 = " +innerMe.getKey());

                List<SimpleActVO> simpleActVOList = innerMe.getValue();
                for(SimpleActVO simpleActVO : simpleActVOList){
                    System.out.println(", simpleActVO = " +simpleActVO);
                }
            } 
        }*/
    }

    /**
     * 根据活动id得到活动ActVO对象
     * @param actId
     * @return

    public static ActVO getActVOByActId(int actId) {

        JsonObject obj = IceClientTool.sendMsg(IceClientTool.SP_ICE_SERVICE,
                JSONTool.buildParams("spActivityService/getSpActivityById", actId));

        ActivityBean activity = new Gson().fromJson(obj.get(BaseService.RESULT_OBJ), ActivityBean.class);

        ActVO actVO = new ActVO();
        actVO.setActName(activity.getActName());
        actVO.setActDesc(activity.getActDesc());
        actVO.setActId(activity.getActId());
        try{
            actVO.setPrivilegeType(PrivilegeType.valueOf(activity.getPrivilegeType()));
        }catch (Exception e){
            logger.error(e, activity, null);
        }
        //--------------------------此处需设置活动规则------------------------//
        //Rule rule = ActivityRuleFactory.createRuleClient(activity);
        //actVO.setActrule(ActivityRuleHelper.getActRules(activity));
        return actVO;
    }*/

    /**
     * 根据活动id集合得到活动ActVO的Map对象
     * @param actIds
     * @return

    public static Map<Integer, ActVO> getActVOByActId(int[] actIds) {
        if(actIds == null) return null;
        Map<Integer, ActVO> actVOs = new HashMap<Integer, ActVO>();
        for(int actId : actIds){
            ActVO actVO = getActVOByActId(actId);
            if(actVO != null){
                actVOs.put(actVO.getActId(), actVO);
            }
        }
        return actVOs;
    }*/

    /**
     * 根据商品ids 获取商品参与直降活动的情况
     * @param proIds
     * @return
     */
    public static Map<Integer,Map> getProductPriceDownInfo(int... proIds){
        Map<Integer,Map> result = new HashMap<Integer,Map>();
        try{
            if(proIds!=null && proIds.length>0) {
                Map<Integer,List<ActivityProductBean>> apbMap = ActCacheTool.getActInfoByProIds(proIds);
                if (apbMap!=null && apbMap.size()>0){
                    for(int proId : proIds){
                        Map proInfo = new HashMap();  //直降商品相关活动信息map
                        List<ActivityProductBean> actPros = apbMap.get(proId);
                        if(actPros!=null && actPros.size()>0){
                            ActivityProductBean apb = actPros.get(0);
                            if(apb!=null && apb.getIsPriceDown().intValue() ==0){ //必须是直降类
                                if(apb.getActId()>0){
                                    ActivityBean act = getActivityBeanByActId(apb.getActId());
                                    if(act!=null){
                                        proInfo.put("startTime",act.getStartTime());
                                        proInfo.put("endTime",act.getEndTime());
                                        proInfo.put("actName",act.getActName());
                                        proInfo.put("actWenan",act.getActWenan());
                                        proInfo.put("actPrice",apb.getActPrice());
                                        proInfo.put("discount",apb.getDiscount());
                                        result.put(proId,proInfo);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }catch(Exception e){
            logger.error(e,"根据商品ids 获取商品参与直降活动的情况"+proIds,null);
        }
        return  result;
    }


    /**
     * 根据商品ids 获取商品参与直降活动的情况
     * 包括的活动情况如下：1、正在进行的活动
     *                     2、完成上架但未开始的活动
     * @param proIds
     * @return
     */
    public static Map<Integer,Map> getProductPriceDownInfoForSpTicket(int... proIds){
        Map<Integer,Map> result = new HashMap<Integer,Map>();
        try{
            if(proIds!=null && proIds.length>0) {
                Map<Integer,List<ActivityProductBean>> apbMap = ActCacheTool.getActInfoByProIds(proIds);
                if (apbMap!=null && apbMap.size()>0){
                    for(int proId : proIds){
                        Map proInfo = new HashMap();  //直降商品相关活动信息map
                        List<ActivityProductBean> actPros = apbMap.get(proId);
                        if(actPros!=null && actPros.size()>0){
                            ActivityProductBean apb = actPros.get(0);
                            if(apb!=null && apb.getIsPriceDown().intValue() ==0){ //必须是直降类
                                if(apb.getActId()>0){
                                    //完成上架但未开始 or 正在进行的活动
                                    ActivityBean act = getValidActivityBeanByActId(apb.getActId());
                                    if(act!=null){
                                        proInfo.put("startTime",act.getStartTime());
                                        proInfo.put("endTime",act.getEndTime());
                                        proInfo.put("actName",act.getActName());
                                        proInfo.put("actWenan",act.getActWenan());
                                        proInfo.put("actPrice",apb.getActPrice());
                                        proInfo.put("discount",apb.getDiscount());
                                        result.put(proId,proInfo);
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }catch(Exception e){
            logger.error(e,"根据商品ids 获取商品参与直降活动的情况"+proIds,null);
        }
        return  result;
    }

}
