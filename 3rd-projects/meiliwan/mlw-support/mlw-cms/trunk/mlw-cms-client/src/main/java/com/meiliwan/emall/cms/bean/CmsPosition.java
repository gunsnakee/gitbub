package com.meiliwan.emall.cms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.List;

public class CmsPosition extends BaseEntity{
    private Integer id;

    private Integer positionId;

    private String positionName;

    private Integer blockId;

    private String positionType;

    private Integer carouseFlag;

    private Integer pageId;

    private Integer proId;

    private List<CmsLink> linkList;

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

    public void setLinkList(List<CmsLink> linkList) {
        this.linkList = linkList;
    }

    public List<CmsLink> getLinkList(){
        return  this.linkList;
    }
}