package com.meiliwan.emall.oms.bean;

import java.io.Serializable;

public class OrdiPayKey implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4336473904366800486L;

	private String orderItemId;

    private String payCode;

    public OrdiPayKey(){}

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId == null ? null : orderItemId.trim();
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode == null ? null : payCode.trim();
    }
    public OrdiPayKey(String orderItemId) {
        this.orderItemId = orderItemId;
    }
    public OrdiPayKey(String orderItemId, String payCode) {
        this.orderItemId = orderItemId;
        this.payCode = payCode;
    }
}