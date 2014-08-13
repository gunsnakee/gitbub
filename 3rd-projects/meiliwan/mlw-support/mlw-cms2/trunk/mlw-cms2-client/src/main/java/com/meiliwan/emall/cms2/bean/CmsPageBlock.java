package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class CmsPageBlock extends BaseEntity implements Cloneable{
    private Integer id;

    private String blockName;

    private Integer cacheCode;

    private Integer adminId;

    private String adminName;

    private String blockDescription;

    private Date updateTime;

    private Date createTime;

    private Integer blockEditType;

    private Integer pageId;

    private String roleStr;

    private String blockBroweFtl;

    private String blockEditFtl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName == null ? null : blockName.trim();
    }

    public Integer getCacheCode() {
        return cacheCode;
    }

    public void setCacheCode(Integer cacheCode) {
        this.cacheCode = cacheCode;
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

    public String getBlockDescription() {
        return blockDescription;
    }

    public void setBlockDescription(String blockDescription) {
        this.blockDescription = blockDescription == null ? null : blockDescription.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getBlockEditType() {
        return blockEditType;
    }

    public void setBlockEditType(Integer blockEditType) {
        this.blockEditType = blockEditType;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }


    public String getBlockEditFtl() {
        return blockEditFtl;
    }

    public void setBlockEditFtl(String blockEditFtl) {
        this.blockEditFtl = blockEditFtl;
    }

    public String getRoleStr() {
        return roleStr;
    }

    public void setRoleStr(String roleStr) {
        this.roleStr = roleStr;
    }

    public String getBlockBroweFtl() {
        return blockBroweFtl;
    }

    public void setBlockBroweFtl(String blockBroweFtl) {
        this.blockBroweFtl = blockBroweFtl;
    }
}