package com.meiliwan.emall.stock.bean;

import java.io.Serializable;

/**
 * 商品库存项
 */
public class StockItem implements Serializable {
    private static final long serialVersionUID = -4143259841471016509L;
    /**
     * 商品ID
     */
    private Integer proId;

    /**
     * 用户购买数量
     */
    private Integer buyNum;

    /**
     * 订单相关信息
     */
    private OrderItem orderItem;

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
