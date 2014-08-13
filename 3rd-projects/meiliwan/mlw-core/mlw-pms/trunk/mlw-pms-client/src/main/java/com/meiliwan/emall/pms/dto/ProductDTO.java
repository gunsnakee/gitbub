package com.meiliwan.emall.pms.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Set;

public class ProductDTO implements Serializable{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5928021684846387527L;

	private Integer proId;

    private Integer spuId;

    private String skuName;

    private String proName;

    private String shortName;

    private String advName;

    private String barCode;

    private String barName;

    private Float weight;

    private String proUnit;

    private String proSpec;

    private Integer sellType;

    private Double mlwPrice;
    private BigDecimal mlwPriceMin;
    private BigDecimal mlwPriceMax;

    private BigDecimal marketPrice;

    private BigDecimal tradePrice;

    private String defaultImageUri;

    private Integer isFalls;

    private String fallsImageUri;

    private Integer sellStock;

    private Integer firstCategoryId;

    private Integer secondCategoryId;

    private Integer thirdCategoryId;

    private Integer brandId;

    private Integer adminId;

    private Integer supplierId;

    private Integer placeId;

    private String saleTag;

    private String propertyString;

    private String searchKeyword;

    private String seoKeyword;

    private String seoDescp;

    private Integer state;

    private Integer isDel;

    private Integer oneScoreNum;

    private Integer twoScoreNum;

    private Integer threeScoreNum;

    private Integer fourScoreNum;

    private Integer fiveScoreNum;

    private Float score;

    private Integer realSaleNum;

    private Integer showSaleNum;

    private Integer realScanNum;

    private Integer showScanNum;

    private Timestamp createTime;

    private Timestamp onTime;

    private Timestamp offTime;

    private Timestamp deleteTime;
    
    private Timestamp updateTime;
    
    private Timestamp updateTimeMin;
    private Timestamp updateTimeMax;
    
    private String summary;

    private String descp;

    private String imageUris;

    private Integer safeStock;

    private Set<Integer> notInProIds;
    
	public Double getMlwPrice() {
		return mlwPrice;
	}

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public void setMlwPrice(Double mlwPrice) {
		this.mlwPrice = mlwPrice;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public Set<Integer> getNotInProIds() {
		return notInProIds;
	}

	public void setNotInProIds(Set<Integer> notInProIds) {
		this.notInProIds = notInProIds;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
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

	public String getBarName() {
		return barName;
	}

	public void setBarName(String barName) {
		this.barName = barName;
	}

	public Float getWeight() {
		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public String getProUnit() {
		return proUnit;
	}

	public void setProUnit(String proUnit) {
		this.proUnit = proUnit;
	}

	public String getProSpec() {
		return proSpec;
	}

	public void setProSpec(String proSpec) {
		this.proSpec = proSpec;
	}

	public Integer getSellType() {
		return sellType;
	}

	public void setSellType(Integer sellType) {
		this.sellType = sellType;
	}


	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getTradePrice() {
		return tradePrice;
	}

	public void setTradePrice(BigDecimal tradePrice) {
		this.tradePrice = tradePrice;
	}

	public String getDefaultImageUri() {
		return defaultImageUri;
	}

	public void setDefaultImageUri(String defaultImageUri) {
		this.defaultImageUri = defaultImageUri;
	}

	public Integer getIsFalls() {
		return isFalls;
	}

	public void setIsFalls(Integer isFalls) {
		this.isFalls = isFalls;
	}

	public String getFallsImageUri() {
		return fallsImageUri;
	}

	public void setFallsImageUri(String fallsImageUri) {
		this.fallsImageUri = fallsImageUri;
	}

    public Integer getSellStock() {
        return sellStock;
    }

    public void setSellStock(Integer sellStock) {
        this.sellStock = sellStock;
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

	public Integer getAdminId() {
		return adminId;
	}

	public void setAdminId(Integer adminId) {
		this.adminId = adminId;
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

	public String getSaleTag() {
		return saleTag;
	}

	public void setSaleTag(String saleTag) {
		this.saleTag = saleTag;
	}

	public String getPropertyString() {
		return propertyString;
	}

	public void setPropertyString(String propertyString) {
		this.propertyString = propertyString;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getSeoKeyword() {
		return seoKeyword;
	}

	public void setSeoKeyword(String seoKeyword) {
		this.seoKeyword = seoKeyword;
	}

	public String getSeoDescp() {
		return seoDescp;
	}

	public void setSeoDescp(String seoDescp) {
		this.seoDescp = seoDescp;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getOneScoreNum() {
		return oneScoreNum;
	}

	public void setOneScoreNum(Integer oneScoreNum) {
		this.oneScoreNum = oneScoreNum;
	}

	public Integer getTwoScoreNum() {
		return twoScoreNum;
	}

	public void setTwoScoreNum(Integer twoScoreNum) {
		this.twoScoreNum = twoScoreNum;
	}

	public Integer getThreeScoreNum() {
		return threeScoreNum;
	}

	public void setThreeScoreNum(Integer threeScoreNum) {
		this.threeScoreNum = threeScoreNum;
	}

	public Integer getFourScoreNum() {
		return fourScoreNum;
	}

	public void setFourScoreNum(Integer fourScoreNum) {
		this.fourScoreNum = fourScoreNum;
	}

	public Integer getFiveScoreNum() {
		return fiveScoreNum;
	}

	public void setFiveScoreNum(Integer fiveScoreNum) {
		this.fiveScoreNum = fiveScoreNum;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Integer getRealSaleNum() {
		return realSaleNum;
	}

	public void setRealSaleNum(Integer realSaleNum) {
		this.realSaleNum = realSaleNum;
	}

	public Integer getShowSaleNum() {
		return showSaleNum;
	}

	public void setShowSaleNum(Integer showSaleNum) {
		this.showSaleNum = showSaleNum;
	}

	public Integer getRealScanNum() {
		return realScanNum;
	}

	public void setRealScanNum(Integer realScanNum) {
		this.realScanNum = realScanNum;
	}

	public Integer getShowScanNum() {
		return showScanNum;
	}

	public void setShowScanNum(Integer showScanNum) {
		this.showScanNum = showScanNum;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getOnTime() {
		return onTime;
	}

	public void setOnTime(Timestamp onTime) {
		this.onTime = onTime;
	}

	public Timestamp getOffTime() {
		return offTime;
	}

	public void setOffTime(Timestamp offTime) {
		this.offTime = offTime;
	}

	public Timestamp getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Timestamp deleteTime) {
		this.deleteTime = deleteTime;
	}

	public BigDecimal getMlwPriceMin() {
		return mlwPriceMin;
	}

	public void setMlwPriceMin(BigDecimal mlwPriceMin) {
		this.mlwPriceMin = mlwPriceMin;
	}

	public BigDecimal getMlwPriceMax() {
		return mlwPriceMax;
	}

	public void setMlwPriceMax(BigDecimal mlwPriceMax) {
		this.mlwPriceMax = mlwPriceMax;
	}

	public Timestamp getUpdateTimeMin() {
		return updateTimeMin;
	}

	public void setUpdateTimeMin(Timestamp updateTimeMin) {
		this.updateTimeMin = updateTimeMin;
	}

	public Timestamp getUpdateTimeMax() {
		return updateTimeMax;
	}

	public void setUpdateTimeMax(Timestamp updateTimeMax) {
		this.updateTimeMax = updateTimeMax;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getDescp() {
		return descp;
	}

	public void setDescp(String descp) {
		this.descp = descp;
	}

	public String getImageUris() {
		return imageUris;
	}

	public void setImageUris(String imageUris) {
		this.imageUris = imageUris;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

    public Integer getSafeStock() {
        return safeStock;
    }

    public void setSafeStock(Integer safeStock) {
        this.safeStock = safeStock;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
