package com.meiliwan.emall.pms.cache.dto;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-2-26
 * Time: 上午10:44
 * 属性值sku
 * 用于存每个属性值对应的sku
 */
public class PropValueSkuDto {
    private Integer skuId;    //属性值对应的skuid
    private Integer propValueId;  //属性值id
    private String propValue;  //属性值     42码
    private String imageUrl; //主图url
    private String  selected;//是否对应当前sku属性值，1表示是，0表示非。 如：红42码sku，然后刚好这颜色规格属性对应的就是“红”，最后在详情页显示时用选中框表示当前

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPropValueId() {
        return propValueId;
    }

    public void setPropValueId(Integer propValueId) {
        this.propValueId = propValueId;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
