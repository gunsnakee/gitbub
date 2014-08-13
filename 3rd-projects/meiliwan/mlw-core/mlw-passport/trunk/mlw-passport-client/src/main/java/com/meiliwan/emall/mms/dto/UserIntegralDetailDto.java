package com.meiliwan.emall.mms.dto;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-17
 * Time: 下午4:36
 * To change this template use File | Settings | File Templates.
 */
public class UserIntegralDetailDto {
    private Integer id;

    private String detail;

    private Short updateWay;

    private Integer value;

    private Short updateType;

    private Date createTime;

    private Integer uid;

    private Integer integralId;

    private Date startTime; //开始时间
    private Date endTime; //结束时间

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
        this.detail = detail;
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
}
