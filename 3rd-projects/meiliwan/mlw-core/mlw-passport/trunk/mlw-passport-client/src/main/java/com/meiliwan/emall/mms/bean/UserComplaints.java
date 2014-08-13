package com.meiliwan.emall.mms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class UserComplaints extends BaseEntity {
    private static final long serialVersionUID = -2379841091810170906L;
    private Integer id;

    private String orderId;

    private Short complaintsType;

    private Integer uid;

    private String nickName;

    private Integer adminId;

    private String adminName;

    private BigDecimal orderPrice;

    private String contactInfo;

    private String proofImage;

    private Short state;

    private Timestamp createTime;

    private Timestamp updateTime;

    private Timestamp replyTime;

    private Short isUserDel;

    private Short isAdminDel;

    private String content;

    private String replyContent;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent == null ? null : replyContent.trim();
    }

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

    public Short getComplaintsType() {
        return complaintsType;
    }

    public void setComplaintsType(Short complaintsType) {
        this.complaintsType = complaintsType;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
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

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo == null ? null : contactInfo.trim();
    }

    public String getProofImage() {
        return proofImage;
    }

    public void setProofImage(String proofImage) {
        this.proofImage = proofImage == null ? null : proofImage.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
    }

    public Short getIsUserDel() {
        return isUserDel;
    }

    public void setIsUserDel(Short isUserDel) {
        this.isUserDel = isUserDel;
    }

    public Short getIsAdminDel() {
        return isAdminDel;
    }

    public void setIsAdminDel(Short isAdminDel) {
        this.isAdminDel = isAdminDel;
    }
}