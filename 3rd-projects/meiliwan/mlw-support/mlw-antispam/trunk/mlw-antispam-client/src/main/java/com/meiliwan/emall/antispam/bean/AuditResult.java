package com.meiliwan.emall.antispam.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class AuditResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 审核结果类型
	 * 		NORMAL	:	通过
	 * 		FORBIDDEN	:	未通过
	 */
	private AuditResultType auditResultType;
	
	/**
	 * 审核后的内容
	 */
	private String content;
	
	private Set<String> urls = new HashSet<String>();
	
	private Set<String> forbiddenWords = new HashSet<String>();

	public AuditResultType getAuditResultType() {
		return auditResultType;
	}

	public void setAuditResultType(AuditResultType auditResultType) {
		this.auditResultType = auditResultType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Set<String> getForbiddenWords() {
		return forbiddenWords;
	}

	public void setForbiddenWords(Set<String> forbiddenWords) {
		this.forbiddenWords = forbiddenWords;
	}

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
	
}
