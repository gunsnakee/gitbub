package com.meiliwan.emall.cms2.bean;

import java.util.Date;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.core.bean.BaseEntity;

public class TvPage extends BaseEntity{
    private Integer id;

    private String pageName;

    private short state=GlobalNames.STATE_VALID;

    private String proIds;

    private Integer sequence;

    private Integer adminId;

    private Date createTime;

    private Date updateTime;

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

    public short getState() {
		return state;
	}

	public void setState(short state) {
		this.state = state;
	}

	public String getProIds() {
        return proIds;
    }

    public void setProIds(String proIds) {
        this.proIds = proIds == null ? null : proIds.trim();
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
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
}