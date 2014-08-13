package com.meiliwan.emall.commons.constant;

/**
 * 
 * @author lsf
 *
 */
public enum ActType {

	my("免邮", "tag-free"),
	zk("折扣", "tag-discount"),
	my_zk("免邮+折扣", "tag-free-discount"),
	mlj("满立减", "line-mj")
	;
	
	
	private String cnName;
	private String cssClz;
	
	private ActType(String cnName, String cssClz){
		this.cnName = cnName;
		this.cssClz = cssClz;
	}

	public String getCnName() {
		return cnName;
	}

	public String getCssClz() {
		return cssClz;
	}
	
}
