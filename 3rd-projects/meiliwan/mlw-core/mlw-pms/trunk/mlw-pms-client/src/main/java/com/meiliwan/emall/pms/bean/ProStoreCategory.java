package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

/**
 * 店铺类目
 * @author yiyou.luo
 *2013-09-23
 */

public class ProStoreCategory extends BaseEntity {
    private static final long serialVersionUID = -1207549125435731316L;
    private Integer id;

    private Integer storeId;

    private String storeName;

    private Integer firstCategoryId;

    private String firstCategoryName;

    private Integer secondCategoryId;

    private String secondCategoryName;

    private Integer thirdCategoryId;

    private String thirdCategoryName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getFirstCategoryId() {
        return firstCategoryId;
    }

    public void setFirstCategoryId(Integer firstCategoryId) {
        this.firstCategoryId = firstCategoryId;
    }

    public String getFirstCategoryName() {
        return firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName) {
        this.firstCategoryName = firstCategoryName == null ? null : firstCategoryName.trim();
    }

    public Integer getSecondCategoryId() {
        return secondCategoryId;
    }

    public void setSecondCategoryId(Integer secondCategoryId) {
        this.secondCategoryId = secondCategoryId;
    }

    public String getSecondCategoryName() {
        return secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName) {
        this.secondCategoryName = secondCategoryName == null ? null : secondCategoryName.trim();
    }

    public Integer getThirdCategoryId() {
        return thirdCategoryId;
    }

    public void setThirdCategoryId(Integer thirdCategoryId) {
        this.thirdCategoryId = thirdCategoryId;
    }

    public String getThirdCategoryName() {
        return thirdCategoryName;
    }

    public void setThirdCategoryName(String thirdCategoryName) {
        this.thirdCategoryName = thirdCategoryName == null ? null : thirdCategoryName.trim();
    }
}