package com.meiliwan.emall.cms2.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class CmsFragment extends BaseEntity implements Cloneable{
    private Integer id;

    private Integer cacheCode;

    private Integer isPublished;

    private Date publishTime;

    private String publisherName;

    private Integer publisherId;

    private String fragmentName;

    private String fragmentDesc;

    private String fragmentContent;

    public String getFragmentContent() {
        return fragmentContent;
    }

    public void setFragmentContent(String fragmentContent) {
        this.fragmentContent = fragmentContent;
    }

    public Integer getPublished() {
        return isPublished;
    }

    public void setPublished(Integer published) {
        isPublished = published;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCacheCode() {
        return cacheCode;
    }

    public void setCacheCode(Integer cacheCode) {
        this.cacheCode = cacheCode;
    }

    public Integer getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Integer isPublished) {
        this.isPublished = isPublished;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName == null ? null : publisherName.trim();
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName == null ? null : fragmentName.trim();
    }

    public String getFragmentDesc() {
        return fragmentDesc;
    }

    public void setFragmentDesc(String fragmentDesc) {
        this.fragmentDesc = fragmentDesc == null ? null : fragmentDesc.trim();
    }
}