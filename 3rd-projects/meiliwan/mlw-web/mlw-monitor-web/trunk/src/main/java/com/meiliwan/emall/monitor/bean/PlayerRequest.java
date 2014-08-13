package com.meiliwan.emall.monitor.bean;

import java.io.Serializable;

import com.meiliwan.emall.core.bean.BaseEntity;

public class PlayerRequest  extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6330023893663528389L;
	private Integer pid;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

	@SuppressWarnings("unchecked")
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return pid;
	}
}