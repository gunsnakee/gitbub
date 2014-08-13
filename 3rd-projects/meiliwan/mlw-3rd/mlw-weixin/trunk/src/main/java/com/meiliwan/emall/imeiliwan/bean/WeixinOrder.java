package com.meiliwan.emall.imeiliwan.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * 微信订单信息
 *  create by yyluo
 */

public class WeixinOrder extends BaseEntity {

    private static final long serialVersionUID = -7653439042493738747L;

    private Integer id;

    private String transid;

    private String openId;

    private String outTradeNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTransid() {
        return transid;
    }

    public void setTransid(String transid) {
        this.transid = transid == null ? null : transid.trim();
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }
}