package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Card extends BaseEntity {
    private static final long serialVersionUID = -6436256842997743764L;
    private String cardId;

    private String password;

    private BigDecimal cardPrice;

    private String batchId;

    private Integer state;

    private Integer isFreeze;

    private Integer isDel;

    private Integer isSell;

    private Integer userId;

    private String userName;

    private String buyerPhone;

    private String buyerEmail;

    private String sellerName;

    private Date activeTime;

    private Date sellTime;

    private Date freezeTime;

    private Date delTime;

    private Date updateTime = new Date();

    private Date createTime = new Date();

    private String cardName;

    private Integer cardType;

    private Timestamp endTime;

    private Integer actNum;

    /**
     * 相关查询时间实体
     */
    private Timestamp createTimeMin;

    private Timestamp createTimeMax;

    private Timestamp endTimeMin;

    private Timestamp endTimeMax;

    private Timestamp activeTimeMin;

    private Timestamp activeTimeMax;

    /**
     * 卡对应的批次号信息
     */
    private CardBatch batch;

    /**
     * 卡的相关操作记录
     */
    private List<CardOptLog> logs;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public BigDecimal getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(BigDecimal cardPrice) {
        this.cardPrice = cardPrice;
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

    public Integer getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(Integer isFreeze) {
        this.isFreeze = isFreeze;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Integer getIsSell() {
        return isSell;
    }

    public void setIsSell(Integer isSell) {
        this.isSell = isSell;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(String buyerPhone) {
        this.buyerPhone = buyerPhone == null ? null : buyerPhone.trim();
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail == null ? null : buyerEmail.trim();
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName == null ? null : sellerName.trim();
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getSellTime() {
        return sellTime;
    }

    public void setSellTime(Date sellTime) {
        this.sellTime = sellTime;
    }

    public Date getFreezeTime() {
        return freezeTime;
    }

    public void setFreezeTime(Date freezeTime) {
        this.freezeTime = freezeTime;
    }

    public Date getDelTime() {
        return delTime;
    }

    public void setDelTime(Date delTime) {
        this.delTime = delTime;
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

    public CardBatch getBatch() {
        return batch;
    }

    public void setBatch(CardBatch batch) {
        this.batch = batch;
    }

    public List<CardOptLog> getLogs() {
        return logs;
    }

    public void setLogs(List<CardOptLog> logs) {
        this.logs = logs;
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

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getCreateTimeMin() {
        return createTimeMin;
    }

    public void setCreateTimeMin(Timestamp createTimeMin) {
        this.createTimeMin = createTimeMin;
    }

    public Timestamp getCreateTimeMax() {
        return createTimeMax;
    }

    public void setCreateTimeMax(Timestamp createTimeMax) {
        this.createTimeMax = createTimeMax;
    }

    public Timestamp getEndTimeMin() {
        return endTimeMin;
    }

    public void setEndTimeMin(Timestamp endTimeMin) {
        this.endTimeMin = endTimeMin;
    }

    public Timestamp getEndTimeMax() {
        return endTimeMax;
    }

    public void setEndTimeMax(Timestamp endTimeMax) {
        this.endTimeMax = endTimeMax;
    }

    public Timestamp getActiveTimeMin() {
        return activeTimeMin;
    }

    public void setActiveTimeMin(Timestamp activeTimeMin) {
        this.activeTimeMin = activeTimeMin;
    }

    public Timestamp getActiveTimeMax() {
        return activeTimeMax;
    }

    public void setActiveTimeMax(Timestamp activeTimeMax) {
        this.activeTimeMax = activeTimeMax;
    }

    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    @Override
    public String getId() {
        return this.cardId;
    }
}