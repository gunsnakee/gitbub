package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class CardImportLog extends BaseEntity {
    private static final long serialVersionUID = -9049762405916298867L;
    private Integer id;

    private String cardId;

    private String batchId;

    private Integer state;

    private Integer isSend = 1;

    private String descp;

    private String cardBatchId;

    private String cardName;

    private Integer cardType;

    private BigDecimal cardPrice = new BigDecimal("0");

    private String buyerName;

    private String sellerName;

    private Date createTime = new Date();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getIsSend() {
        return isSend;
    }

    public void setIsSend(Integer isSend) {
        this.isSend = isSend ;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getCardBatchId() {
        return cardBatchId;
    }

    public void setCardBatchId(String cardBatchId) {
        this.cardBatchId = cardBatchId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(BigDecimal cardPrice) {
        this.cardPrice = cardPrice;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}