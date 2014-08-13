package com.meiliwan.emall.monitor.allocation;

import java.io.Serializable;

import com.meiliwan.emall.core.bean.BaseEntity;

public class PlayerApp extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7104909635096550529L;

	private Integer pid;

    private String appName;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

	@SuppressWarnings("unchecked")
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return pid;
	}
}