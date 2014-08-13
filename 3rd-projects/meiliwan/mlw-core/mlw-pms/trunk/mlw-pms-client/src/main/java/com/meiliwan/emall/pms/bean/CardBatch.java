package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class CardBatch extends BaseEntity {
    private static final long serialVersionUID = 8605332034305439316L;
    private String batchId;

    private String cardName;

    private Integer cardType;

    private BigDecimal cardPrice;

    private Integer initNum;

    private Integer cardNum;

    private Integer validMonth;

    private Integer preWarnDay;

    private Integer state;

    private Integer adminId;

    private String adminName;

    private String adminEmail;

    private Integer sellNum;

    private Integer activeNum;

    private Integer freezeNum;

    private Integer delNum;

    private Date endTime;

    private Date warnTime;

    private Date updateTime = new Date();

    private Date createTime = new Date();

    private Integer actNum;

    /**
     * 相关查询时间实体
     */
    private Timestamp createTimeMin;

    private Timestamp createTimeMax;

    private Timestamp endTimeMin;

    private Timestamp endTimeMax;

    private Timestamp warnTimeMin;

    private Timestamp warnTimeMax;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName == null ? null : cardName.trim();
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

    public Integer getInitNum() {
        return initNum;
    }

    public void setInitNum(Integer initNum) {
        this.initNum = initNum;
    }

    public Integer getCardNum() {
        return cardNum;
    }

    public void setCardNum(Integer cardNum) {
        this.cardNum = cardNum;
    }

    public Integer getValidMonth() {
        return validMonth;
    }

    public void setValidMonth(Integer validMonth) {
        this.validMonth = validMonth;
    }

    public Integer getPreWarnDay() {
        return preWarnDay;
    }

    public void setPreWarnDay(Integer preWarnDay) {
        this.preWarnDay = preWarnDay;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName == null ? null : adminName.trim();
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail == null ? null : adminEmail.trim();
    }

    public Integer getSellNum() {
        return sellNum;
    }

    public void setSellNum(Integer sellNum) {
        this.sellNum = sellNum;
    }

    public Integer getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    public Integer getFreezeNum() {
        return freezeNum;
    }

    public void setFreezeNum(Integer freezeNum) {
        this.freezeNum = freezeNum;
    }

    public Integer getDelNum() {
        return delNum;
    }

    public void setDelNum(Integer delNum) {
        this.delNum = delNum;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
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

    public Timestamp getWarnTimeMin() {
        return warnTimeMin;
    }

    public void setWarnTimeMin(Timestamp warnTimeMin) {
        this.warnTimeMin = warnTimeMin;
    }

    public Timestamp getWarnTimeMax() {
        return warnTimeMax;
    }

    public void setWarnTimeMax(Timestamp warnTimeMax) {
        this.warnTimeMax = warnTimeMax;
    }

    public Integer getActNum() {
        return actNum;
    }

    public void setActNum(Integer actNum) {
        this.actNum = actNum;
    }

    @Override
    public String getId() {
        return this.batchId;
    }
}