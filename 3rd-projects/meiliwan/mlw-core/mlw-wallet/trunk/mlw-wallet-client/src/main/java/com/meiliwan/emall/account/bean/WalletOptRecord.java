package com.meiliwan.emall.account.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class WalletOptRecord extends BaseEntity implements Cloneable {

    private String innerNum;

    private String outNum;

    private Integer uid;

    private BigDecimal mlwCoin;

    private String optType;

    private Date optTime;

    private BigDecimal money;

    private Integer successFlag;

    private String source;

    private String extraData;

    private String payAccount;

    private String orderId;

    private Integer adminId;

    private String clientIp;

    private String childType;

    public String getChildType() {
        return childType;
    }

    public void setChildType(String childType) {
        this.childType = childType;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getInnerNum() {
        return innerNum;
    }

    public void setInnerNum(String innerNum) {
        this.innerNum = innerNum;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getMlwCoin() {
        return mlwCoin;
    }

    public void setMlwCoin(BigDecimal mlwCoin) {
        this.mlwCoin = mlwCoin;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType == null ? null : optType.trim();
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Integer successFlag) {
        this.successFlag = successFlag;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData == null ? null : extraData.trim();
    }

    @Override
    public String  getId() {
        return innerNum;
    }


    @Override
    public String toString() {
        return new StringBuilder("uId: ").append(uid).append(" innerNum:").append(innerNum).append(" optType: ").append(optType).append(" money:").append(money).append(" orderId: ").append(orderId).toString();
    }
}