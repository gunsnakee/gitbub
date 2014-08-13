package com.meiliwan.emall.union.bean;

import java.util.List;

/**
 * User: wuzixin
 * Date: 14-5-14
 * Time: 下午6:08
 *
 */
public class EtaoSearchCat {
    private String parentId;

    private String parentName;

    List<CatItem> items;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<CatItem> getItems() {
        return items;
    }

    public void setItems(List<CatItem> items) {
        this.items = items;
    }
}
