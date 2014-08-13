package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.sql.Timestamp;

public class ProConsult extends BaseEntity {
    private static final long serialVersionUID = 1875134399088107034L;
    private Integer id;

    private Integer proId;

    private String proName;

    private Short consultType;

    private Integer uid;

    private String nickName;

    private Integer adminId;

    private String adminName;

    private Long createTimeLong;
    
    private Timestamp createTime;

    private Long replyTimeLong;
    
    private Timestamp replyTime;

    private Short isWebVisible;

    private Short isUserDel;

    private Short isAdminDel;

    private String content;

    private String replyContent;

    private SimpleProduct product;

    public SimpleProduct getProduct() {
        return product;
    }

    public void setProduct(SimpleProduct product) {
        this.product = product;
    }

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

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public Short getConsultType() {
        return consultType;
    }

    public void setConsultType(Short consultType) {
        this.consultType = consultType;
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

    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public long getCreateTimeLong() {
        return createTime.getTime();
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
        this.createTimeLong = createTime.getTime() ;
    }

    public Timestamp getReplyTime() {
        return replyTime;
    }
    
    public long getReplyTimeLong() {
        return replyTime.getTime();
    }

    public void setReplyTime(Timestamp replyTime) {
        this.replyTime = replyTime;
        this.replyTimeLong = replyTime.getTime();
    }

    public Short getIsWebVisible() {
        return isWebVisible;
    }

    public void setIsWebVisible(Short isWebVisible) {
        this.isWebVisible = isWebVisible;
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