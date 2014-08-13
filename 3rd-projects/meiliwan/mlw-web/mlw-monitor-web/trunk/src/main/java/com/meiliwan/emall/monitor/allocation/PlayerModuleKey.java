package com.meiliwan.emall.monitor.allocation;

import java.io.Serializable;

public class PlayerModuleKey implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6398830810289889230L;

	private Integer pid;

    private String moduleName;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }
}