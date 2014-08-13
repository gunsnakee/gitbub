package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ThematicPage extends BaseEntity{
    private static final long serialVersionUID = 4645278449131398305L;
    private Integer pageId;

    private String pageName;

    private String title;

    private String keyword;

    private String descp;

    private Integer templateId;

    private Date beginTime;

    private Date endTime;

    private String url;
    

    private String proIds;

    private String jsonData;

    private String enName;

    private Integer adminId;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    private Short state;


	public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName == null ? null : pageName.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword == null ? null : keyword.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getProIds() {
        return proIds;
    }

    public void setProIds(String proIds) {
        this.proIds = proIds == null ? null : proIds.trim();
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData == null ? null : jsonData.trim();
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    @Override
    public Integer getId() {
        return this.pageId;
    }
    
}