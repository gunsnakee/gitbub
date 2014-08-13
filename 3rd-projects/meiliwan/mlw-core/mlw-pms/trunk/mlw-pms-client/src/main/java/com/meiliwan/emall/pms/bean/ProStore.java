package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

/**
 * 店铺
 * @author yiyou.luo
 *2013-09-23
 */

public class ProStore extends BaseEntity {

    private static final long serialVersionUID = -2630995124080774161L;
    private Integer storeId;

    private String storeName;

    private String storeAdvName;

    private Integer adminId;

    private String seoKeyword;

    private String seoDescp;

    private String seoTitle;

    private Byte state;

    private Date createTime;

    private String enName;

    private String seoTitleList;

    public String getSeoTitleList() {
        return seoTitleList;
    }

    public void setSeoTitleList(String seoTitleList) {
        this.seoTitleList = seoTitleList;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public Integer getId() {
        return storeId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName == null ? null : storeName.trim();
    }

    public String getStoreAdvName() {
        return storeAdvName;
    }

    public void setStoreAdvName(String storeAdvName) {
        this.storeAdvName = storeAdvName == null ? null : storeAdvName.trim();
    }

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getSeoKeyword() {
        return seoKeyword;
    }

    public void setSeoKeyword(String seoKeyword) {
        this.seoKeyword = seoKeyword == null ? null : seoKeyword.trim();
    }

    public String getSeoDescp() {
        return seoDescp;
    }

    public void setSeoDescp(String seoDescp) {
        this.seoDescp = seoDescp == null ? null : seoDescp.trim();
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}