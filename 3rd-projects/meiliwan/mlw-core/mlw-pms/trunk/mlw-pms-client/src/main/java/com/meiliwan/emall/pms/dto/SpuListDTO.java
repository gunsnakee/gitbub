package com.meiliwan.emall.pms.dto;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User: wuzixin
 * Date: 14-3-2
 * Time: 下午4:38
 */
public class SpuListDTO implements Serializable{
    private static final long serialVersionUID = 8468247048650204910L;
    private Integer spuId;

    private String proName;

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    private Integer thirdCategoryId;

    private String categoryName;

    private Integer brandId;

    private Integer supplierId;

    private Integer placeId;

    private Integer isCod;

    private String skuPropA;

    private String skuPropB;

    private Short isUploadImg;

    private Integer storeId;

    private String storeName;

    private Integer state;

    private Timestamp createTime;

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
        this.proName = proName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Integer getIsCod() {
        return isCod;
    }

    public void setIsCode(Integer isCod) {
        isCod = isCod;
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

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
