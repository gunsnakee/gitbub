package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 店铺商品
 * @author yiyou.luo
 *2013-09-23
 */

public class ProStoreProd extends BaseEntity {
    private static final long serialVersionUID = 4839067011227605319L;
    private Integer id;

    private Integer storeId;

    private String storeName;

    private Integer spuId;

    private Integer thirdCategoryId;

    private Integer brandId;

    private Integer state;

    private Integer isHide;

    private BigDecimal storePrice;

    private Integer isDel;

    private Date onTime;

    private Date offTime;

    private Date delTime;

    private String enName;

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsHide() {
        return isHide;
    }

    public void setIsHide(Integer isHide) {
        this.isHide = isHide;
    }

    public BigDecimal getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(BigDecimal storePrice) {
        this.storePrice = storePrice;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
    }

    public Date getOffTime() {
        return offTime;
    }

    public void setOffTime(Date offTime) {
        this.offTime = offTime;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
    }
}