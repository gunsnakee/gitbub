package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ExamResult extends BaseEntity {
    private static final long serialVersionUID = -6565626362153943734L;
    private Integer uid;

    private String userName;

    private Integer rewardLevel;

    private Date updateTime = new Date();

    private Date createTime = new Date();

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getRewardLevel() {
        return rewardLevel;
    }

    public void setRewardLevel(Integer rewardLevel) {
        this.rewardLevel = rewardLevel;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Integer getId() {
        return this.uid;
    }
}