package com.meiliwan.emall.oms.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class OrdInvoice extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7238395158361443041L;

    private String orderId;

    private String invoiceType;

    private String invoiceHead;

    private String invoiceContent;

    private Short billType;

    private Integer uid;

    private Date updateTime = new Date();

    private Date createTime = new Date();

    private Short state;

    private String remark;

    public String getOrderId() {
		return orderId;
	}

	public String getId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType == null ? null : invoiceType.trim();
    }

    public String getInvoiceHead() {
    	
        return invoiceHead==null?"":invoiceHead;
    }

    public void setInvoiceHead(String invoiceHead) {
        this.invoiceHead = invoiceHead == null ? null : invoiceHead.trim();
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent == null ? null : invoiceContent.trim();
    }

    public Short getBillType() {
        return billType;
    }

    public void setBillType(Short billType) {
        this.billType = billType;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}