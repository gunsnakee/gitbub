package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class CmsLink extends BaseEntity implements Cloneable{
    private Integer id;

    private Integer blockId;

    private Integer positionId;
    
    private Integer proId;

    private String linkUrl;

    private String linkName;

    private String linkExtName;

    private String picUrl;

    private Integer openFlag;

    private Double mlwPrice;

    private Double marketPrice;

    private String mark;

    private Integer hideFlag;

    private Date createTime;

    private Integer adminId;

    private String adminName;

    private Integer groupId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
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

    public String getLinkExtName() {
        return linkExtName;
    }

    public void setLinkExtName(String linkExtName) {
        this.linkExtName = linkExtName == null ? null : linkExtName.trim();
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl == null ? null : picUrl.trim();
    }

    public Integer getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Integer openFlag) {
        this.openFlag = openFlag;
    }

    public Double getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(Double mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public Double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Double marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public Integer getHideFlag() {
        return hideFlag;
    }

    public void setHideFlag(Integer hideFlag) {
        this.hideFlag = hideFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}
    
}