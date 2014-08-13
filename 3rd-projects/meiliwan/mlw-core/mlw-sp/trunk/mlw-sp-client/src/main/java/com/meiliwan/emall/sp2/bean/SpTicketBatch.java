package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class SpTicketBatch extends BaseEntity {
    private static final long serialVersionUID = -8012315036409758075L;
    private Integer batchId;

    private String ticketName;

    private BigDecimal ticketPrice;

    private Short ticketType;

    private Short state;

    private Short disable;

    private Integer adminId;

    private Integer initNum;

    private Integer realNum;

    private Integer sellNum;

    private Integer activeNum;

    private Integer useNum;

    private String actUrl;

    private String descp;

    private String disableDescp;

    private Date startTime;

    private Date endTime;

    private Date onTime;

    private Date createTime = new Date();

    /**
     * 相关查询时间实体
     */
    private Timestamp createTimeMin;

    private Timestamp createTimeMax;

    private Timestamp endTimeMin;

    private Timestamp endTimeMax;

    private Timestamp startTimeMin;

    private Timestamp startTimeMax;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName == null ? null : ticketName.trim();
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Short getTicketType() {
        return ticketType;
    }

    public void setTicketType(Short ticketType) {
        this.ticketType = ticketType;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Short getDisable() {
        return disable;
    }

    public void setDisable(Short disable) {
        this.disable = disable;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getInitNum() {
        return initNum;
    }

    public void setInitNum(Integer initNum) {
        this.initNum = initNum;
    }

    public Integer getRealNum() {
        return realNum;
    }

    public void setRealNum(Integer realNum) {
        this.realNum = realNum;
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

    public Integer getUseNum() {
        return useNum;
    }

    public void setUseNum(Integer useNum) {
        this.useNum = useNum;
    }

    public String getDescp() {
        return descp;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
    }

    public String getDisableDescp() {
        return disableDescp;
    }

    public void setDisableDescp(String disableDescp) {
        this.disableDescp = disableDescp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
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

    public Timestamp getStartTimeMin() {
        return startTimeMin;
    }

    public void setStartTimeMin(Timestamp startTimeMin) {
        this.startTimeMin = startTimeMin;
    }

    public Timestamp getStartTimeMax() {
        return startTimeMax;
    }

    public void setStartTimeMax(Timestamp startTimeMax) {
        this.startTimeMax = startTimeMax;
    }

    @Override
    public Integer getId() {
        return this.batchId;
    }
}