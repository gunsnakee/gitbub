package com.meiliwan.emall.monitor.allocation;

import java.io.Serializable;

import com.meiliwan.emall.core.bean.BaseEntity;

public class PlayerModule  extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5965549325409566343L;

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

	@Override
	public <T extends Serializable> T getId() {
		// TODO Auto-generated method stub
		return null;
	}
}