package com.meiliwan.emall.stock.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ProStock extends BaseEntity{
    private static final long serialVersionUID = -6028189378032866749L;
    private Integer proId;

    private Integer stock;

    private Integer sellStock;

    private Integer unsellStock;

    private Integer orderStock;

    private Date updateTime = new Date();

    private Integer safeStock;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSellStock() {
        return sellStock;
    }

    public void setSellStock(Integer sellStock) {
        this.sellStock = sellStock;
    }

    public Integer getUnsellStock() {
        return unsellStock;
    }

    public void setUnsellStock(Integer unsellStock) {
        this.unsellStock = unsellStock;
    }

    public Integer getOrderStock() {
        return orderStock;
    }

    public void setOrderStock(Integer orderStock) {
        this.orderStock = orderStock;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getSafeStock() {
        return safeStock;
    }

    public void setSafeStock(Integer safeStock) {
        this.safeStock = safeStock;
    }

    @Override
    public Integer getId() {
        return this.proId;
    }
}