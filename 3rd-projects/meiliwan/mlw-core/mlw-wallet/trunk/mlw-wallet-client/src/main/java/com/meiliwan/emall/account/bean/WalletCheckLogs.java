package com.meiliwan.emall.account.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class WalletCheckLogs extends BaseEntity implements Cloneable{
    private Integer uid;

    private Date updateTime = new Date();

    private Date startTime;

    private Date endTime;

    private Integer checkStatus;

    private String checkInfo;

    private BigDecimal endMlwCoin;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo == null ? null : checkInfo.trim();
    }

    public BigDecimal getEndMlwCoin() {
        return endMlwCoin;
    }

    public void setEndMlwCoin(BigDecimal endMlwCoin) {
        this.endMlwCoin = endMlwCoin;
    }

    @Override
    public Integer getId() {
        return uid;
    }
}