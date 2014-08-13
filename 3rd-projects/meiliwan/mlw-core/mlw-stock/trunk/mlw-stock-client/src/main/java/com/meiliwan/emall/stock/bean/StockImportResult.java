package com.meiliwan.emall.stock.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;

public class StockImportResult extends BaseEntity {
    private Integer id;

    private String batchNo;

    private Integer adminId;

    private String adminName;

    private String fileName;

    private Short state;

    private Integer totalNum;

    private Integer stockShortNum;

    private Integer mismatchNum;

    private String descp;

    private Date importTime;

    private Integer errorNum;

    private Timestamp updateTimeMin;

    private Timestamp updateTimeMax;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getStockShortNum() {
        return stockShortNum;
    }

    public void setStockShortNum(Integer stockShortNum) {
        this.stockShortNum = stockShortNum;
    }

    public Integer getMismatchNum() {
        return mismatchNum;
    }

    public void setMismatchNum(Integer mismatchNum) {
        this.mismatchNum = mismatchNum;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Date getImportTime() {
        return importTime;
    }

    public void setImportTime(Date importTime) {
        this.importTime = importTime;
    }

    public Integer getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
    }

    public Timestamp getUpdateTimeMin() {
        return updateTimeMin;
    }

    public void setUpdateTimeMin(Timestamp updateTimeMin) {
        this.updateTimeMin = updateTimeMin;
    }

    public Timestamp getUpdateTimeMax() {
        return updateTimeMax;
    }

    public void setUpdateTimeMax(Timestamp updateTimeMax) {
        this.updateTimeMax = updateTimeMax;
    }
}