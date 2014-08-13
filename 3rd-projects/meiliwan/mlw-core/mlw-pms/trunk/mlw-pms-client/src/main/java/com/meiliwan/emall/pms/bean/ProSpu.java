package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;
import java.util.List;

public class ProSpu extends BaseEntity {
    private static final long serialVersionUID = 3706485495096477989L;
    private Integer spuId;

    private String proName;

    private String shortName;

    private String advName;

    private Short proType;

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    private Integer thirdCategoryId;

    private Integer brandId;

    private Integer supplierId;

    private Integer placeId;

    private Integer adminId;

    private String propertyString;

    private Short isCod;

    private Short state;

    private Short skuFlag;

    private String skuPropA;

    private String skuPropB;

    private Short isUploadImg;

    private Date createTime = new Date();

    private Date deleteTime;

    private Date updateTime = new Date();

    /**
     * 商品固有的属性
     */
    private List<ProSelfProperty> psplist;

    /**
     * 商品和属性的对应关系
     */
    private List<ProProductProperty> pppropertyList;

    /**
     * 商品详情
     */
    private ProDetail detail;

    private List<ProImages> imageses;

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName == null ? null : shortName.trim();
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName == null ? null : advName.trim();
    }

    public Short getProType() {
        return proType;
    }

    public void setProType(Short proType) {
        this.proType = proType;
    }

    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public Integer getSecondCategoryId() {
        return secondCategoryId;
    }

    public void setSecondCategoryId(Integer secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }

    public Integer getThirdCategoryId() {
        return thirdCategoryId;
    }

    public void setThirdCategoryId(Integer thirdCategoryId) {
        this.thirdCategoryId = thirdCategoryId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getPropertyString() {
        return propertyString;
    }

    public void setPropertyString(String propertyString) {
        this.propertyString = propertyString == null ? null : propertyString.trim();
    }

    public Short getIsCod() {
        return isCod;
    }

    public void setIsCod(Short isCod) {
        this.isCod = isCod;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getSkuFlag() {
        return skuFlag;
    }

    public void setSkuFlag(Short skuFlag) {
        this.skuFlag = skuFlag;
    }

    public String getSkuPropA() {
        return skuPropA;
    }

    public void setSkuPropA(String skuPropA) {
        this.skuPropA = skuPropA;
    }

    public String getSkuPropB() {
        return skuPropB;
    }

    public void setSkuPropB(String skuPropB) {
        this.skuPropB = skuPropB;
    }

    public Short getIsUploadImg() {
        return isUploadImg;
    }

    public void setIsUploadImg(Short uploadImg) {
        isUploadImg = uploadImg;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ProSelfProperty> getPsplist() {
        return psplist;
    }

    public void setPsplist(List<ProSelfProperty> psplist) {
        this.psplist = psplist;
    }

    public List<ProProductProperty> getPppropertyList() {
        return pppropertyList;
    }

    public void setPppropertyList(List<ProProductProperty> pppropertyList) {
        this.pppropertyList = pppropertyList;
    }

    public ProDetail getDetail() {
        return detail;
    }

    public void setDetail(ProDetail detail) {
        this.detail = detail;
    }

    public List<ProImages> getImageses() {
        return imageses;
    }

    public void setImageses(List<ProImages> imageses) {
        this.imageses = imageses;
    }

    @Override
    public Integer getId() {
        return this.spuId;
    }
}