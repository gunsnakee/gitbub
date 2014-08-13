package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;
import java.util.List;

public class ProProperty extends BaseEntity {
    private static final long serialVersionUID = -5607537767027330750L;
    private Integer proPropId;

    private Integer parentId;

    private String name;

    private String descp;

    private Integer categoryId;

    private Short state;

    private Integer adminId;

    private Integer sequence;

    private Short isSku;

    private Short propertyType;

    private Short isRequired;

    private Short isFilter;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    /**
     * 商品对应的属性值概念
     */
    List<ProPropertyValue> proProValue;

    /**
     * 标记是否是异图
     */
    private int isImage = 0;

    /**
     * 标记是否选择的sku属性
     */
    private int checkSku = 0;

    public Integer getProPropId() {
        return proPropId;
    }

    public void setProPropId(Integer proPropId) {
        this.proPropId = proPropId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Short getIsSku() {
        return isSku;
    }

    public void setIsSku(Short sku) {
        isSku = sku;
    }

    public Short getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(Short propertyType) {
        this.propertyType = propertyType;
    }

    public Short getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Short isRequired) {
        this.isRequired = isRequired;
    }

    public Short getIsFilter() {
        return isFilter;
    }

    public void setIsFilter(Short isFilter) {
        this.isFilter = isFilter;
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

    public List<ProPropertyValue> getProProValue() {
        return proProValue;
    }

    public void setProProValue(List<ProPropertyValue> proProValue) {
        this.proProValue = proProValue;
    }

    public int getIsImage() {
        return isImage;
    }

    public void setIsImage(int image) {
        isImage = image;
    }

    public int getCheckSku() {
        return checkSku;
    }

    public void setCheckSku(int checkSku) {
        this.checkSku = checkSku;
    }

    @Override
    public Integer getId() {
        return this.proPropId;
    }
}