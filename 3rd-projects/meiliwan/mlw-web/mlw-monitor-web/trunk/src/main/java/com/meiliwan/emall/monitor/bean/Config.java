package com.meiliwan.emall.monitor.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class Config extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2497067761450076801L;

	private Integer id;

    private String code;

    private String value;

    private String type;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
}