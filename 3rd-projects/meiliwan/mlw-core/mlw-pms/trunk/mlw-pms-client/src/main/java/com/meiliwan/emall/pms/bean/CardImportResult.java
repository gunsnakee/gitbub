package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;

public class CardImportResult extends BaseEntity {
    private static final long serialVersionUID = -2059044354280031763L;
    private Integer id;

    private String batchId;

    private String fileName;

    private Integer cardType;

    private Integer totalNum = 0;

    private Integer errorNum = 0;

    private Integer dismatchNum = 0;

    private Integer sendSucNum = 0;

    private Integer sendErrorNum = 0;

    private Integer adminId;

    private String adminName;

    private String descp;

    private Date createTime = new Date();

    private Timestamp createTimeMin;

    private Timestamp createTimeMax;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId == null ? null : batchId.trim();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    public Integer getDismatchNum() {
        return dismatchNum;
    }

    public void setDismatchNum(Integer dismatchNum) {
        this.dismatchNum = dismatchNum;
    }

    public Integer getSendSucNum() {
        return sendSucNum;
    }

    public void setSendSucNum(Integer sendSucNum) {
        this.sendSucNum = sendSucNum;
    }

    public Integer getSendErrorNum() {
        return sendErrorNum;
    }

    public void setSendErrorNum(Integer sendErrorNum) {
        this.sendErrorNum = sendErrorNum;
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

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
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
}