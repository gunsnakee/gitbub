package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class SpTicketOrder extends BaseEntity {
    private static final long serialVersionUID = 1571666656110705878L;
    private String ticketId;

    private Integer batchId;

    private BigDecimal payPrice;

    private Integer uid;

    private String orderId;

    private String ordiTicket;

    private Short state;

    private Date createTime = new Date();

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId == null ? null : ticketId.trim();
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrdiTicket() {
        return ordiTicket;
    }

    public void setOrdiTicket(String ordiTicket) {
        this.ordiTicket = ordiTicket;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getId() {
        return this.ticketId;
    }
}