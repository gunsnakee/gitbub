package com.meiliwan.emall.antispam.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class AntispamLog extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private String contentType;

    private String businessName;

    private Integer contentId;

    private String content;

    private Integer uid;

    private String ip;

    private Date createTime;

    private Date auditTime;

    private AuditResultType result;

    private String reason;

    @SuppressWarnings("unchecked")
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType == null ? null : contentType.trim();
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public AuditResultType getResult() {
        return result;
    }

    public void setResult(AuditResultType result) {
        this.result = result == null ? null : result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }
}