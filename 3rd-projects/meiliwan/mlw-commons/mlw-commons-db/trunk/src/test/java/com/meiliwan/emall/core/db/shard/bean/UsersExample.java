package com.meiliwan.emall.core.db.shard.bean;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.meiliwan.emall.commons.PageInfo;

import java.io.Serializable;

public class UsersExample implements Serializable {
    private static final long serialVersionUID = -385147893893593315L;
    private long id;

    private String userName;
private PageInfo pageInfo;
    

    public PageInfo getPageInfo() {
	return pageInfo;
}

public void setPageInfo(PageInfo pageInfo) {
	this.pageInfo = pageInfo;
}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}