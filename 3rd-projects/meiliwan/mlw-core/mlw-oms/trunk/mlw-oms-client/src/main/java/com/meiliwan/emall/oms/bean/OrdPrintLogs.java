package com.meiliwan.emall.oms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class OrdPrintLogs extends BaseEntity{
    private static final long serialVersionUID = 5910110443072050131L;
    private Integer id;

    private String orderId;

    private Integer printType;

    private Integer optUid;

    private String optUname;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getPrintType() {
        return printType;
    }

    public void setPrintType(Integer printType) {
        this.printType = printType;
    }

    public Integer getOptUid() {
        return optUid;
    }

    public void setOptUid(Integer optUid) {
        this.optUid = optUid;
    }

    public String getOptUname() {
        return optUname;
    }

    public void setOptUname(String optUname) {
        this.optUname = optUname == null ? null : optUname.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}