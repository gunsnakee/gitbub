package com.meiliwan.emall.cms.ff.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class OptLog extends BaseEntity implements Cloneable {
    private Integer id;

    private Integer optObjId;

    private Integer optType;

    private Integer adminId;

    private Date optTime;

    private String ip;

    private String originalContent;

    private String newContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOptObjId() {
        return optObjId;
    }

    public void setOptObjId(Integer optObjId) {
        this.optObjId = optObjId;
    }

    public Integer getOptType() {
        return optType;
    }

    public void setOptType(Integer optType) {
        this.optType = optType;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getOptTime() {
        return optTime;
    }

    public void setOptTime(Date optTime) {
        this.optTime = optTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void setOriginalContent(String originalContent) {
        this.originalContent = originalContent;
    }
}