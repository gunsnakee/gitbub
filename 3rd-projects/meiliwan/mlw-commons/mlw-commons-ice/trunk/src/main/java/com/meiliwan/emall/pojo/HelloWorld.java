package com.meiliwan.emall.pojo;

import java.io.Serializable;

public class HelloWorld implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7783727187737296365L;
	
	private String prefix;
	private String suffix;
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
}
