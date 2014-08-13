package com.meiliwan.emall.account.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class AccountPayCard extends BaseEntity implements Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7933162165006797360L;

	private Integer id;

    private Integer uid;

    private String cardNo;

    private BigDecimal cardMoney;

    private BigDecimal balance;

    private Date deadTime;

    private Date firstUsedTime;

    private Date createTime;

    private Date buyTime;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public BigDecimal getCardMoney() {
        return cardMoney;
    }

    public void setCardMoney(BigDecimal cardMoney) {
        this.cardMoney = cardMoney;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public Date getFirstUsedTime() {
        return firstUsedTime;
    }

    public void setFirstUsedTime(Date firstUsedTime) {
        this.firstUsedTime = firstUsedTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Date buyTime) {
        this.buyTime = buyTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}