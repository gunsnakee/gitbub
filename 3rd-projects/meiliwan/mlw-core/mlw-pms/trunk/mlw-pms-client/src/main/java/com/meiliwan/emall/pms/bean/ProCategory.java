package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;
import java.util.List;

public class ProCategory extends BaseEntity {
    private static final long serialVersionUID = -1630201532721576516L;
    private Integer categoryId;

    private Integer parentId;

    private String categoryName;

    private Integer sequence;

    private Short state;

    private String descp;

    private String seachKeyword;

    private Integer adminId;

    private Date createTime = new Date();

    private Date updateTime =  new Date();

    private List<ProCategory> children;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public String getSeachKeyword() {
        return seachKeyword;
    }

    public void setSeachKeyword(String seachKeyword) {
        this.seachKeyword = seachKeyword == null ? null : seachKeyword.trim();
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ProCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ProCategory> children) {
        this.children = children;
    }

    @Override
    public Integer getId() {
        return this.categoryId;
    }
}