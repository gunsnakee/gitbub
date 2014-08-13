package com.meiliwan.emall.mms.dto;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: yiyou.luo
 * Date: 13-7-5
 * Time: 下午3:39
 *  订单积分 dto 用于购买商品添加积分时使用
 */
public class OrderInteralDto {
    /**
     * 订单 id
     */
    private String orderId;
    /**
     * 订单明细
     */
    private List<OrderItemIntegralDto> item;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<OrderItemIntegralDto> getItem() {
        return item;
    }

    public void setItem(List<OrderItemIntegralDto> item) {
        this.item = item;
    }
}
