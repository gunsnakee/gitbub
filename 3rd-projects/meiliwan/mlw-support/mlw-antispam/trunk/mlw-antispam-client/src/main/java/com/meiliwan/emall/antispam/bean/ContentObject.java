package com.meiliwan.emall.antispam.bean;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

public class ContentObject {
	
	/**
	 * 内容分类
	 */
	private ContentType	type;
	
	/**
	 * 业务名
	 *    由各业务线自己定义，如评论：mlw-pms/comment
	 */
	@NotBlank(message="业务名不能为空!")
	private String businessName;
	
	/**
	 * 评论或者咨询的id
	 */
	private int id;
	
	/**
	 * 待审核的内容，即用户编写的内容
	 */
	@NotBlank(message="内容不能为空!")
	private String 	content;
	
	/**
	 * 用户id
	 */
	private int 		uid;
	
	/**
	 * 用户ip
	 */
	private String 	ip;
	
	private Date		createTime;
	
	public ContentObject(){};

	public ContentType getType() {
		return type;
	}

	public void setType(ContentType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
