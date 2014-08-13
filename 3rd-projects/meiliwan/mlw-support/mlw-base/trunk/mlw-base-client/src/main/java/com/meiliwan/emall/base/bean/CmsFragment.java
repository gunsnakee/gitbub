package com.meiliwan.emall.base.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class CmsFragment extends BaseEntity implements Cloneable {
    private Integer id;

    private Integer pageId;
    
    private String fragmentName;

    private Date createTime;

    private Integer adminId;

    private String content;

    public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}