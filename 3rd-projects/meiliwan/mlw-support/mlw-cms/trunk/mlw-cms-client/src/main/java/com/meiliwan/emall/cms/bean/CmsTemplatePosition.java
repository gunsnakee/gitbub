package com.meiliwan.emall.cms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class CmsTemplatePosition extends BaseEntity {
    private static final long serialVersionUID = -7774061801142903303L;
    private Integer id;

    private Integer positionId;

    private String positionName;

    private Integer blockId;

    private String positionType;

    private Integer carouseFlag;

    private Integer templateId;

    private Integer proId;

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

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName == null ? null : positionName.trim();
    }

    public Integer getBlockId() {
        return blockId;
    }

    public void setBlockId(Integer blockId) {
        this.blockId = blockId;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType == null ? null : positionType.trim();
    }

    public Integer getCarouseFlag() {
        return carouseFlag;
    }

    public void setCarouseFlag(Integer carouseFlag) {
        this.carouseFlag = carouseFlag;
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
}