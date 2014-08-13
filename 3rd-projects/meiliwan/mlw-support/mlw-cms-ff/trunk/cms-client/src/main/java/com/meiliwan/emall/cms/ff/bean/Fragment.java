package com.meiliwan.emall.cms.ff.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class Fragment extends BaseEntity implements Cloneable {
    private Integer id;

    private Integer idCode;

    private Date createTime;

    private Integer groupId;

    private String fragmentName;

    private Integer adminId;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCode() {
        return idCode;
    }

    public void setIdCode(Integer idCode) {
        this.idCode = idCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName == null ? null : fragmentName.trim();
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}