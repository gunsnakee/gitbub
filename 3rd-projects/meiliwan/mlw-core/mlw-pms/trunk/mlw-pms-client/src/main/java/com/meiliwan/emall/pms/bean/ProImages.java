package com.meiliwan.emall.pms.bean;

import java.util.Date;

public class ProImages extends ProImagesKey {
    private static final long serialVersionUID = 6223839510579187292L;
    private Integer proPropId;

    private String defaultImageUri;

    private String imageUris;

    private Date createTime = new Date();

    private Date updateTime = new Date();

    public Integer getProPropId() {
        return proPropId;
    }

    public void setProPropId(Integer proPropId) {
        this.proPropId = proPropId;
    }

    public String getDefaultImageUri() {
        return defaultImageUri;
    }

    public void setDefaultImageUri(String defaultImageUri) {
        this.defaultImageUri = defaultImageUri == null ? null : defaultImageUri.trim();
    }

    public String getImageUris() {
        return imageUris;
    }

    public void setImageUris(String imageUris) {
        this.imageUris = imageUris == null ? null : imageUris.trim();
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
}