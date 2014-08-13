package com.meiliwan.emall.sp2.dto;

/**
 * User: wuzixin
 * Date: 14-6-4
 * Time: 上午10:46
 */
public class TicketImportVo {

    private Integer batchId;

    private Integer adminId;

    private String adminName;

    private String fileName;

    public Integer getBatchId() {
        return batchId;
    }

    public void setBatchId(Integer batchId) {
        this.batchId = batchId;
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
        this.adminName = adminName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
