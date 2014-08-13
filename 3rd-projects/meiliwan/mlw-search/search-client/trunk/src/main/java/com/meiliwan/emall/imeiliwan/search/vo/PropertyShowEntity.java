package com.meiliwan.emall.imeiliwan.search.vo;

import java.io.Serializable;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 实现了hashCode和equals. 用的是属性名-属性值 混合字符串的对应函数
 * 不用了！
 * @author lgn-mop
 */
public class PropertyShowEntity implements Serializable, Comparable<PropertyShowEntity> {
    /**
     *
     */
    private static final long serialVersionUID = -6575949558780361105L;

    private String[] propertyValue; //[id, 属性名, 属性值]

    private int hits;
    private String propQueryNew; //点击到这个属性上变成的条件
    private String tag;
    private boolean highlight;
    
    
    public PropertyShowEntity(String[] propertyValue, int hits) {
        this(propertyValue, hits, null);
    }

    public PropertyShowEntity(String[] propertyValue, int hits, String newPropQuery) {
        this.propertyValue = propertyValue;
        this.hits = hits;
        this.propQueryNew = newPropQuery;
        if (propertyValue == null){
        	this.tag = "-";
        }else
        	this.tag = propertyValue[1] + "-" + propertyValue[2];
    }



    public String toJSONObjectFormat() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"show\":[\"").append(propertyValue[0]).append("\",\"").append(propertyValue[1])
                .append("\",\"").append(propertyValue[2]).append("\"],\"pf\":\"").append(propQueryNew)
                .append(",\"hl\":").append(highlight).append("}");
        return builder.toString();
    }

    public String toString() {
    	return new Gson().toJson(this);
    }

    public void setPropertyIdKeyValue(String[] propIdKeyValue) {
        this.propertyValue = propIdKeyValue;
        if (propertyValue == null){
        	this.tag = "-";
        }else
        	this.tag = propertyValue[1] + "-" + propertyValue[2];
    }

    public void setQueryNew(String qNew) {
        this.propQueryNew = qNew;
    }

    //按属性值字符串排序
    public int compareTo(PropertyShowEntity o) {
        return o.propertyValue[2].compareTo(this.propertyValue[2]);
    }

    public int hashCode() {
        return tag.hashCode();
    }

    public boolean equals(Object obj) {
        PropertyShowEntity pse = (PropertyShowEntity) obj;
        return this.tag.equals(pse.tag);
    }

    public String[] getPropertyValue() {
        return propertyValue;
    }

    public int getHits() {
        return hits;
    }

    public String getPropQueryNew() {
        return propQueryNew;
    }

    public String getTag() {
        return tag;
    }

    public boolean getHighlight() {
        return highlight;
    }

    /**
     * 本属性是否高亮
     * @param highlight
     */
    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }
    
    public static PropertyShowEntity parseEntityFromJSON(JsonObject entity){
    	return new Gson().fromJson(entity, PropertyShowEntity.class);
    }
}
