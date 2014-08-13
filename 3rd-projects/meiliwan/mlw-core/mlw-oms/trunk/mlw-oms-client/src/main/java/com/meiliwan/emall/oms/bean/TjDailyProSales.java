package com.meiliwan.emall.oms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class TjDailyProSales   extends BaseEntity {
    private Integer id;

    private Integer proId;

    private String barCode;

    private String proName;

    private BigDecimal mlwPrice;

    private BigDecimal tradePrice;

    private Integer sellStock;

    private Integer dailySales;

    private Integer totalSales;

    private String storeName;

    private BigDecimal avgMlwPrice;

    private BigDecimal salesAmount;

    private BigDecimal profitAmount;

    private Date createTime;

    private Integer sellDate;
    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode == null ? null : barCode.trim();
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public BigDecimal getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(BigDecimal mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public BigDecimal getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(BigDecimal tradePrice) {
        this.tradePrice = tradePrice;
    }

    public Integer getSellStock() {
        return sellStock;
    }

    public void setSellStock(Integer sellStock) {
        this.sellStock = sellStock;
    }

    public Integer getDailySales() {
        return dailySales;
    }

    public void setDailySales(Integer dailySales) {
        this.dailySales = dailySales;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public BigDecimal getAvgMlwPrice() {
        return avgMlwPrice;
    }

    public void setAvgMlwPrice(BigDecimal avgMlwPrice) {
        this.avgMlwPrice = avgMlwPrice;
    }

    public BigDecimal getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(BigDecimal salesAmount) {
        this.salesAmount = salesAmount;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSellDate() {
        return sellDate;
    }

    public void setSellDate(Integer sellDate) {
        this.sellDate = sellDate;
    }

}