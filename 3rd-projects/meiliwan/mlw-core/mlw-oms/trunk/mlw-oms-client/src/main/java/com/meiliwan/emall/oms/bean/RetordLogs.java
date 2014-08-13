package com.meiliwan.emall.oms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class RetordLogs extends BaseEntity{
    private Integer id;
    private String retOrderId;

    private Date createTime;

    private String optId;

    private String optMan;

    private String optContent;

    private String optResult;

    private String optStatusCode;

    private String oldOrderId;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(String oldOrderId) {
        this.oldOrderId = oldOrderId;
    }

    public String getRetOrderId() {
        return retOrderId;
    }

    public void setRetOrderId(String retOrderId) {
        this.retOrderId = retOrderId == null ? null : retOrderId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOptId() {
        return optId;
    }

    public void setOptId(String optId) {
        this.optId = optId == null ? null : optId.trim();
    }

    public String getOptMan() {
        return optMan;
    }

    public void setOptMan(String optMan) {
        this.optMan = optMan == null ? null : optMan.trim();
    }

    public String getOptContent() {
        return optContent;
    }

    public void setOptContent(String optContent) {
        this.optContent = optContent == null ? null : optContent.trim();
    }

    public String getOptStatusCode() {
        return optStatusCode;
    }

    public void setOptStatusCode(String optStatusCode) {
        this.optStatusCode = optStatusCode;
    }

    public String getOptResult() {
        return optResult;
    }

    public void setOptResult(String optResult) {
        this.optResult = optResult == null ? null : optResult.trim();
    }
}