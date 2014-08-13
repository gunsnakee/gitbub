package com.meiliwan.emall.mms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

/**
 * create by yiyou.luo 2013-7-3
 * 用户积分明细
 */

public class UserIntegralDetail extends BaseEntity {

    private static final long serialVersionUID = -280296493122951253L;

    private Integer id;

    private String detail;

    private Short updateWay;

    private Integer value;

    private Short updateType;

    private Date createTime;

    private Integer uid;

    private Integer integralId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Short getUpdateWay() {
        return updateWay;
    }

    public void setUpdateWay(Short updateWay) {
        this.updateWay = updateWay;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public Short getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Short updateType) {
        this.updateType = updateType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getIntegralId() {
        return integralId;
    }

    public void setIntegralId(Integer integralId) {
        this.integralId = integralId;
    }
}