package com.meiliwan.emall.stock.bean;

import java.io.Serializable;

/**
 * 订单行列表
 *
 * User: wuzixin
 * Date: 13-10-11
 * Time: 上午11:58
 */
public class OrderItem implements Serializable{
    private static final long serialVersionUID = 72071082225085904L;

    /**
     * 订单ID
     */
    private String orderId = "0";

    /**
     * 订单行ID
     */
    private String orderItemId = "0";

    private Boolean status = true;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
