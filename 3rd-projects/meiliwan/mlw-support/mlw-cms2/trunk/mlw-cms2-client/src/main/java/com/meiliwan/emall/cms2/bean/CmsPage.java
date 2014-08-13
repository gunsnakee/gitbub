package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class CmsPage extends BaseEntity implements Cloneable{
    private Integer id;

    private String pageName;

    private String site;

    private Date createTime;

    private Integer adminId;

    private String adminName;

    private String pageDescription;

    private Date updateTime;

    private String pageFtl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName == null ? null : pageName.trim();
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site == null ? null : site.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public String getPageDescription() {
        return pageDescription;
    }

    public void setPageDescription(String pageDescription) {
        this.pageDescription = pageDescription == null ? null : pageDescription.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPageFtl() {
        return pageFtl;
    }

    public void setPageFtl(String pageFtl) {
        this.pageFtl = pageFtl == null ? null : pageFtl.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CmsPage)) return false;

        CmsPage cmsPage = (CmsPage) o;

        if (id != null ? !id.equals(cmsPage.id) : cmsPage.id != null) return false;
        if (pageName != null ? !pageName.equals(cmsPage.pageName) : cmsPage.pageName != null) return false;
        if (site != null ? !site.equals(cmsPage.site) : cmsPage.site != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (pageName != null ? pageName.hashCode() : 0);
        result = 31 * result + (site != null ? site.hashCode() : 0);
        return result;
    }
}