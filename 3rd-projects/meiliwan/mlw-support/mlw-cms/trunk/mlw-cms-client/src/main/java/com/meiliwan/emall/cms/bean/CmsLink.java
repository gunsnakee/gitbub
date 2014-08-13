package com.meiliwan.emall.cms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class CmsLink extends BaseEntity {
    private Integer linkId;

    private Integer positionId;

    private String linkUrl;

    private String linkName;

    private String linkNameExt;

    private String picUrl;

    private Timestamp beginTime;

    private Timestamp endTime;

    private Timestamp modifyTime;

    private String modifyUser;

    private Integer status;

    private Integer openFlag;

    private Integer onlineFlag;

    private BigDecimal mlwPrice;

    private BigDecimal marketPrice;

    private Integer saleNum;

    private Integer comNum;

    private Float score;

    private String discount;

    private Integer hideFlag;

    private Integer pageId;

    private Integer proId;

    private String linkDesc;

    private String mark;

    @Override
    public Integer getId() {
        return this.linkId;
    }

    public void setId(Integer id) {
        this.linkId = id;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Timestamp modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser == null ? null : modifyUser.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getLinkDesc() {
        return linkDesc;
    }

    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc == null ? null : linkDesc.trim();
    }

    public void setLinkId(Integer linkId) {
        this.linkId = linkId;
    }

    public Integer getLinkId() {
        return linkId;
    }

    public String getLinkNameExt() {
        return linkNameExt;
    }

    public void setLinkNameExt(String linkNameExt) {
        this.linkNameExt = linkNameExt;
    }

    public Integer getOnlineFlag() {
        return onlineFlag;
    }

    public void setOnlineFlag(Integer onlineFlag) {
        this.onlineFlag = onlineFlag;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }
}