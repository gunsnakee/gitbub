package com.meiliwan.emall.stock.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 订单扣减库存的状态情况
 */
public class StockItemsUpdateResult implements Serializable {

    private static final long serialVersionUID = 8155261243810691761L;
    private Boolean status;

    private List<StockItemStatus> stockItemStatusList;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<StockItemStatus> getStockItemStatusList() {
        return stockItemStatusList;
    }

    public void setStockItemStatusList(List<StockItemStatus> stockItemStatusList) {
        this.stockItemStatusList = stockItemStatusList;
    }
}
