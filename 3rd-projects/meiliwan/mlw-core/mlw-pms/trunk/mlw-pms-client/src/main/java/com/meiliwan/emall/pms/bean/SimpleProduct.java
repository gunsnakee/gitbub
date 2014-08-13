package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品简单实体，主要去掉相关的冗余字段
 */
public class SimpleProduct extends BaseEntity {
    private static final long serialVersionUID = 3210909114957333883L;
    private Integer proId;

    private Integer spuId;

    private String proName;

    private String shortName;

    private String advName;

    private String skuName;

    private String barCode;

    private BigDecimal mlwPrice;

    private BigDecimal marketPrice;

    private String defaultImageUri;

    private Integer thirdCategoryId;

    private Integer brandId;

    private Integer supplierId;

    private Short state;

    private Short isCod;

    private String skuPropertyStr;

    private Date onTime;

    private Date presaleEndTime;

    private Date presaleSendTime;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public String getProName() {
        return proName != null ? (proName + " " + (getSkuName() == null ? "" : getSkuName())) : proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAdvName() {
        return advName;
    }

    public void setAdvName(String advName) {
        this.advName = advName;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
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

    public String getDefaultImageUri() {
        return defaultImageUri;
    }

    public void setDefaultImageUri(String defaultImageUri) {
        this.defaultImageUri = defaultImageUri;
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

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getIsCod() {
        return isCod;
    }

    public void setIsCod(Short cod) {
        isCod = cod;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getSkuPropertyStr() {
        return skuPropertyStr;
    }

    public void setSkuPropertyStr(String skuPropertyStr) {
        this.skuPropertyStr = skuPropertyStr;
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
    }

    public Date getPresaleEndTime() {
        return presaleEndTime;
    }

    public void setPresaleEndTime(Date presaleEndTime) {
        this.presaleEndTime = presaleEndTime;
    }

    public Date getPresaleSendTime() {
        return presaleSendTime;
    }

    public void setPresaleSendTime(Date presaleSendTime) {
        this.presaleSendTime = presaleSendTime;
    }

    public boolean isPresale() {
        if (getPresaleEndTime() == null) {
            return false;
        } else {
            if (DateUtil.compare(new Date(), getPresaleEndTime()) == -1) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Integer getId() {
        return this.proId;
    }

    @Override
    public String toString() {
        return "SimpleProduct{" +
                "proId=" + proId +
                ", spuId=" + spuId +
                ", proName='" + proName + '\'' +
                ", shortName='" + shortName + '\'' +
                ", advName='" + advName + '\'' +
                ", skuName='" + skuName + '\'' +
                ", barCode='" + barCode + '\'' +
                ", mlwPrice=" + mlwPrice +
                ", marketPrice=" + marketPrice +
                ", defaultImageUri='" + defaultImageUri + '\'' +
                ", thirdCategoryId=" + thirdCategoryId +
                ", brandId=" + brandId +
                ", supplierId=" + supplierId +
                ", state=" + state +
                ", isCod=" + isCod +
                ", skuPropertyStr='" + skuPropertyStr + '\'' +
                ", onTime=" + onTime +
                ", presaleEndTime=" + presaleEndTime +
                ", presaleSendTime=" + presaleSendTime +
                '}';
    }
}
