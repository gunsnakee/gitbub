package com.meiliwan.emall.sp2.dto;

import com.meiliwan.emall.sp2.bean.ActivityBean;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-12-30
 * Time: 上午11:45
 *  活动查询条件 dto
 */
public class ActivityDTO extends ActivityBean {
    /**
     * 活动列表页显示状态
     *未上线：0
     *未开始(上线)：10
     *正在进行（(上线）：11
     *已结束（(上线）： 12
     *已取消（下线） ：-1
     */
    private Short actViewState;
    /**
     * 系统当前时间
     */
    private Date currentTime;

    public Short getActViewState() {
        return actViewState;
    }

    public void setActViewState(Short actViewState) {
        this.actViewState = actViewState;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }
}
