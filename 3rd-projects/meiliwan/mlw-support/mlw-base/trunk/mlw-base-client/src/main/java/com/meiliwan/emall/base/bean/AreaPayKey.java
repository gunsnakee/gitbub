package com.meiliwan.emall.base.bean;

import java.io.Serializable;

public class AreaPayKey implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8606822992256660584L;

	private Integer transCompanyId;

	private String areaCode;

    public AreaPayKey(Integer transCompanyId, String areaCode) {
		// TODO Auto-generated constructor stub
    	this.transCompanyId=transCompanyId;
    	this.areaCode=areaCode;
	}
    public AreaPayKey(){};
    
	public Integer getTransCompanyId() {
        return transCompanyId;
    }

    public void setTransCompanyId(Integer transCompanyId) {
        this.transCompanyId = transCompanyId;
    }

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	@Override
	public String toString() {
		return "AreaPayKey [transCompanyId=" + transCompanyId + ", areaCode="
				+ areaCode + "]";
	}
    
    
}