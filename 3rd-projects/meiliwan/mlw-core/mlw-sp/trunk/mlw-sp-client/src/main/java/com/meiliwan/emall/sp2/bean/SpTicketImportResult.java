package com.meiliwan.emall.sp2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class SpTicketImportResult extends BaseEntity {
    private static final long serialVersionUID = 113865776248982737L;
    private Integer id;

    private String fileName;

    private Integer batchId;

    private Short optType = 1;

    private Integer adminId;

    private String adminName;

    private Integer totalNum;

    private Integer sucNum;

    private Integer dismatchNun;

    private String descp;

    private Date createTime = new Date();

    private SpTicketImportDetail detail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
    }

    public Short getOptType() {
        return optType;
    }

    public void setOptType(Short optType) {
        this.optType = optType;
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

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }

    public Integer getSucNum() {
        return sucNum;
    }

    public void setSucNum(Integer sucNum) {
        this.sucNum = sucNum;
    }

    public Integer getDismatchNun() {
        return dismatchNun;
    }

    public void setDismatchNun(Integer dismatchNun) {
        this.dismatchNun = dismatchNun;
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

    public SpTicketImportDetail getDetail() {
        return detail;
    }

    public void setDetail(SpTicketImportDetail detail) {
        this.detail = detail;
    }
}