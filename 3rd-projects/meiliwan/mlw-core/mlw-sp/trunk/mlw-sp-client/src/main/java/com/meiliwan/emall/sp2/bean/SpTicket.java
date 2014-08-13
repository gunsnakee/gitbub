package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;


public class SpTicket extends BaseEntity {
    private static final long serialVersionUID = -7816787111360944639L;
    private String ticketId;

    private String ticketPwd;

    private BigDecimal ticketPrice;

    private Integer batchId;

    private Short state;

    private Integer adminId;

    private Integer uid;

    private String userName;

    private String buyerPhone;

    private String buyerEmail;

    private Date activeTime;

    private Date sellTime;

    private Date createTime = new Date();

    private Short ticketType;

    private String actUrl;

    private Date startTime;

    private Date endTime;

    private String descp;

    private Date updateTime;
    
    /**
     * 相关查询实体
     */
    private Integer batchState;

    private String ticketName;

    private Timestamp sellTimeMin;

    private Timestamp sellTimeMax;

    private Timestamp endTimeMin;

    private Timestamp endTimeMax;

    private Timestamp startTimeMin;

    private Timestamp startTimeMax;
    
    /**
     * 辅助存储字段
     */
    private String orderId;

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId == null ? null : ticketId.trim();
    }

    public String getTicketPwd() {
        return ticketPwd;
    }

    public void setTicketPwd(String ticketPwd) {
        this.ticketPwd = ticketPwd == null ? null : ticketPwd.trim();
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getTicketType() {
        return ticketType;
    }

    public void setTicketType(Short ticketType) {
        this.ticketType = ticketType;
    }

    public String getActUrl() {
        return actUrl;
    }

    public void setActUrl(String actUrl) {
        this.actUrl = actUrl;
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

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp;
    }

    public Integer getBatchState() {
        return batchState;
    }

    public void setBatchState(Integer batchState) {
        this.batchState = batchState;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Timestamp getSellTimeMin() {
        return sellTimeMin;
    }

    public void setSellTimeMin(Timestamp sellTimeMin) {
        this.sellTimeMin = sellTimeMin;
    }

    public Timestamp getSellTimeMax() {
        return sellTimeMax;
    }

    public void setSellTimeMax(Timestamp sellTimeMax) {
        this.sellTimeMax = sellTimeMax;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
    public String getId() {
        return this.ticketId;
    }

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}