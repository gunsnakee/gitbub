package com.meiliwan.emall.stock.bean;

import java.io.Serializable;

/**
 * 商品库存项状态
 */
public class StockItemStatus implements Serializable {

    private static final long serialVersionUID = 5838435799871448244L;
    private StockItem stockItem;

    private Boolean status;

    private String code;

    public StockItemStatus(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public StockItem getStockItem() {
        return stockItem;
    }

    public void setStockItem(StockItem stockItem) {
        this.stockItem = stockItem;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
