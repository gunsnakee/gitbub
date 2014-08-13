package com.meiliwan.emall.base.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class BaseMsgTemplate extends BaseEntity {
    private static final long serialVersionUID = -3504638625867190069L;
    private Integer tmpId;

    private Integer tmpType;

    private Integer contentType;

    private String content;

    private Date createTime = new Date();

    public Integer getTmpId() {
        return tmpId;
    }

    public void setTmpId(Integer tmpId) {
        this.tmpId = tmpId;
    }

    public Integer getTmpType() {
        return tmpType;
    }

    public void setTmpType(Integer tmpType) {
        this.tmpType = tmpType;
    }

    public Integer getContentType() {
        return contentType;
    }

    public void setContentType(Integer contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Integer getId() {
        return this.tmpId;
    }
}