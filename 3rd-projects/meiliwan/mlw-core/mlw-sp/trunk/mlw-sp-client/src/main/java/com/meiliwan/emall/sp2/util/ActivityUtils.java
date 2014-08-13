package com.meiliwan.emall.sp2.util;

import com.meiliwan.emall.sp2.bean.ActivityBean;
import com.meiliwan.emall.sp2.constant.ActState;
import java.util.Date;

/**
 * 活动工具类
 * Created by xiongyu on 13-12-27.
 */
public class ActivityUtils {

    /**
     * 校验一个活动是否上架以及开始/结束时间是否在当前时间范围内
     * @param activityBean
     * @return
     */
    public static boolean isOnliningActivity(ActivityBean activityBean){
        if(activityBean == null) return false;
        if(activityBean.getState() < ActState.UP.getState()) return false;
        Date now = new Date();
        if(activityBean.getStartTime().after(now)) return false;
        if(activityBean.getEndTime().before(now)) return false;
        return true;
    }

    /**
     * 校验一个活动是否 正在进行时 或 上架但未开始
     * 1、正在进行时：上架以及开始/结束时间是否在当前时间范围内
     * 2、上架但未开始：上架及开始时间比当前时间晚
     * 实际上就是上架但并未过期。
     * @param activityBean
     * @return
     */
    public static boolean isUnstartOrOnliningActivity(ActivityBean activityBean){
        if(activityBean == null) return false;
        if(activityBean.getState() < ActState.UP.getState()) return false;
        Date now = new Date();
        if(activityBean.getEndTime().after(now)) return true;
       // if(activityBean.getEndTime().before(now)) return false;
        return false;
    }

}
