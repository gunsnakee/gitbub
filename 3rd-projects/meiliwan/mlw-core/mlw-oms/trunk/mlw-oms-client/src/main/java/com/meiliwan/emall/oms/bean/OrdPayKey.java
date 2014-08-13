package com.meiliwan.emall.oms.bean;

import java.io.Serializable;

public class OrdPayKey implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4336473904366800486L;

	private String orderId;

    private String payCode;

    public OrdPayKey(){}

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode == null ? null : payCode.trim();
    }
    public OrdPayKey(String orderId) {
        this.orderId = orderId;
    }
    public OrdPayKey(String orderId, String payCode) {
        this.orderId = orderId;
        this.payCode = payCode;
    }
}