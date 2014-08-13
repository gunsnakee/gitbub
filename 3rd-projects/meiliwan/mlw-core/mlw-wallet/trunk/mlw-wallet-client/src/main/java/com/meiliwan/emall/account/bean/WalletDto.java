package com.meiliwan.emall.account.bean;


import java.io.Serializable;

import com.meiliwan.emall.commons.bean.PayCode;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-9-10
 * Time: 下午4:52
 * To change this template use File | Settings | File Templates.
 */
public class WalletDto implements Serializable {

    /** 内部流水号 **/
    private String innerNum;
    /** 外部流水号 **/
    private String outerNum;
    /** 用户id **/
    private Integer uId;
    /** 订单 id  **/
    private String orderId;
    /** 用户支付账号**/
    private String payAccount;

    private double money;

    private PayCode payCode;

    private Integer adminId;

    private String clientIp;

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getInnerNum() {
        return innerNum;
    }

    public void setInnerNum(String innerNum) {
        this.innerNum = innerNum;
    }

    public String getOuterNum() {
        return outerNum;
    }

    public void setOuterNum(String outerNum) {
        this.outerNum = outerNum;
    }

    public Integer getuId() {
        return uId;
    }

    public void setuId(Integer uId) {
        this.uId = uId;
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

    public PayCode getPayCode() {
        return payCode;
    }

    public void setPayCode(PayCode payCode) {
        this.payCode = payCode;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}

