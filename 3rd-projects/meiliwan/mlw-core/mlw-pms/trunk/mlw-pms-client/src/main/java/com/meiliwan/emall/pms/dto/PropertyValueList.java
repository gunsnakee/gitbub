package com.meiliwan.emall.pms.dto;

import com.meiliwan.emall.pms.bean.ProProperty;
import com.meiliwan.emall.pms.bean.ProPropertyValue;

import java.io.Serializable;
import java.util.List;

/**
 * 保存商品属性和属性值list
 */
public class PropertyValueList implements Serializable {
    private List<ProProperty> properties;
    private List<ProPropertyValue> propertyValues;

    public List<ProProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ProProperty> properties) {
        this.properties = properties;
    }

    public List<ProPropertyValue> getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(List<ProPropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
