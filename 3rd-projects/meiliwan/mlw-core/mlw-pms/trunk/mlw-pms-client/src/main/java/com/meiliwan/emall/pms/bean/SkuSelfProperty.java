package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class SkuSelfProperty extends BaseEntity {
    private static final long serialVersionUID = 3766071840721894676L;
    private Integer id;

    private Integer proId;

    private String selfPropName;

    private String selfPropValue;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getSelfPropName() {
        return selfPropName;
    }

    public void setSelfPropName(String selfPropName) {
        this.selfPropName = selfPropName == null ? null : selfPropName.trim();
    }

    public String getSelfPropValue() {
        return selfPropValue;
    }

    public void setSelfPropValue(String selfPropValue) {
        this.selfPropValue = selfPropValue == null ? null : selfPropValue.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}