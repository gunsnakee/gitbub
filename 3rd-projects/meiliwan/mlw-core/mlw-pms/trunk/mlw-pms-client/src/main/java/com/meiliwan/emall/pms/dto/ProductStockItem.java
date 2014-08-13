package com.meiliwan.emall.pms.dto;

import java.io.Serializable;

/**
 * 商品和库存对应的实体，用于增加商品销量
 */
public class ProductStockItem implements Serializable{

    private static final long serialVersionUID = -8437037694468769445L;
    private Integer proId;

    private Integer stockNum;

    public Integer getProId() {
        return proId;
    }

    public ProductStockItem(Integer proId, Integer stockNum) {
        this.proId = proId;
        this.stockNum = stockNum;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getStockNum() {
        return stockNum;
    }

    public void setStockNum(Integer stockNum) {
        this.stockNum = stockNum;
    }
}
