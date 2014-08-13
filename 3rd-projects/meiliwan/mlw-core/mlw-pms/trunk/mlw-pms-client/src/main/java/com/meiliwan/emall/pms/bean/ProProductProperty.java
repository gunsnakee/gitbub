package com.meiliwan.emall.pms.bean;


import java.util.Date;

public class ProProductProperty extends ProProductPropertyKey {
    private static final long serialVersionUID = -8690709839462999755L;
    private String valueId = "";

    private String value = "";

    private Short isSku = 0;

    private Short isImage = 0;

    private Date createTime = new Date();

    public String getValueId() {
        return valueId;
    }

    public void setValueId(String valueId) {
        this.valueId = valueId == null ? null : valueId.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Short getIsSku() {
        return isSku;
    }

    public void setIsSku(Short sku) {
        isSku = sku;
    }

    public Short getIsImage() {
        return isImage;
    }

    public void setIsImage(Short image) {
        isImage = image;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}