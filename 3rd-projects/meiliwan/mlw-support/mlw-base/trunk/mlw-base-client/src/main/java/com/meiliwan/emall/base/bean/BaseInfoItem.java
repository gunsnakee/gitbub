package com.meiliwan.emall.base.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class BaseInfoItem extends BaseEntity {
    private static final long serialVersionUID = -1021242743266959583L;
    private Integer infoItemId;

    private String infoItemName;

    private Integer parentId;

    private Short itemType;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    private String fileName;

    public Integer getInfoItemId() {
        return infoItemId;
    }

    public void setInfoItemId(Integer infoItemId) {
        this.infoItemId = infoItemId;
    }

    public String getInfoItemName() {
        return infoItemName;
    }

    public void setInfoItemName(String infoItemName) {
        this.infoItemName = infoItemName == null ? null : infoItemName.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Short getItemType() {
        return itemType;
    }

    public void setItemType(Short itemType) {
        this.itemType = itemType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public Integer getId() {
        return this.infoItemId;
    }
}