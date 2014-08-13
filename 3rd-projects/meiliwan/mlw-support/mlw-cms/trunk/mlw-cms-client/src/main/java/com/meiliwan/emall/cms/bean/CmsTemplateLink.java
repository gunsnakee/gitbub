package com.meiliwan.emall.cms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CmsTemplateLink extends BaseEntity {
    private Integer id;

    private Integer positionId;

    private Integer linkId;

    private String linkUrl;

    private String linkName;

    private String linkDesc;

    private String picUrl;

    private Timestamp beginTime;

    private Timestamp endTime;

    private Timestamp updateTime;

    private String updateUser;

    private Integer state;

    private Integer openFlag;

    private Integer onlineFlag;

    private BigDecimal mlwPrice;

    private BigDecimal marketPrice;

    private Integer saleNum;

    private Integer comNum;

    private Float score;

    private Integer discount;

    private Integer hideFlag;

    private Integer templateId;

    private Integer proId;

    private String mark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc == null ? null : linkDesc.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Integer openFlag) {
        this.openFlag = openFlag;
    }

    public BigDecimal getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(BigDecimal mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Integer getSaleNum() {
        return saleNum;
    }

    public void setSaleNum(Integer saleNum) {
        this.saleNum = saleNum;
    }

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getHideFlag() {
        return hideFlag;
    }

    public void setHideFlag(Integer hideFlag) {
        this.hideFlag = hideFlag;
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}