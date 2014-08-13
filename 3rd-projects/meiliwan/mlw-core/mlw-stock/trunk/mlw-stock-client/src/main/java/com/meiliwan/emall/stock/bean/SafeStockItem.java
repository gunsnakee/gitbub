package com.meiliwan.emall.stock.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品安全库存VO
 * User: wuzixin
 * Date: 13-10-24
 * Time: 上午10:45
 */
public class SafeStockItem implements Serializable{

    private static final long serialVersionUID = 2089650634427367725L;

    private Integer proId;

    private String barCode;

    private String proName;

    private BigDecimal mlwPrice;

    private Integer sellStock;

    private Integer safeStock;

    private String storeName;

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
        this.barCode = barCode;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public BigDecimal getMlwPrice() {
        return mlwPrice;
    }

    public void setMlwPrice(BigDecimal mlwPrice) {
        this.mlwPrice = mlwPrice;
    }

    public Integer getSellStock() {
        return sellStock;
    }

    public void setSellStock(Integer sellStock) {
        this.sellStock = sellStock;
    }

    public Integer getSafeStock() {
        return safeStock;
    }

    public void setSafeStock(Integer safeStock) {
        this.safeStock = safeStock;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
