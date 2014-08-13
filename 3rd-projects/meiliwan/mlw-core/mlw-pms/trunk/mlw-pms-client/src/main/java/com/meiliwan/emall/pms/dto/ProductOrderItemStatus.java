package com.meiliwan.emall.pms.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 商品订单项列表项状态和整个订单状态
 */
public class ProductOrderItemStatus implements Serializable{

    /**
     * 商品项状态
     */
    private List<ProductItemStatus> itemStatus;

    /**
     * 订单状态
     */
    private Boolean status;

    public List<ProductItemStatus> getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(List<ProductItemStatus> itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
