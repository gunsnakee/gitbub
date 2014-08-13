package com.meiliwan.emall.imeiliwan.search.vo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 属性行，包含很多个属性值
 *  @Description TODO
 *	@author shanbo.liang
 */
public class PropertyGroupEntity implements Serializable {

	public static class GroupSort implements Comparator<PropertyGroupEntity> {

		public float bias;

		public GroupSort(float bias) {
			this.bias = bias;
		}

		public int compare(PropertyGroupEntity o1, PropertyGroupEntity o2) {
			double score1 = Math.sqrt(o1.productHits> 0 ? o1.productHits: 0  + 1) * bias + o1.values.size() * (1 - bias);
			double score2 = Math.sqrt(o2.productHits> 0 ? o2.productHits: 0  + 1) * bias + o2.values.size() * (1 - bias);
			if (score1 > score2) {
				return -1;
			} else if (score1 < score2) {
				return 1;
			}
			return 0;
		}

	}

	/**
	 *
	 */
	 private static final long serialVersionUID = 544057177871546738L;

	private List<PropertyShowEntity> values;
	private int productHits;
	private String propertyGroupName;
	private double score;
	private String unSelected; //选择“不限”的三级类目URL
	private boolean highlight;//“不限”是否高亮

	public PropertyGroupEntity(String groupName, List<PropertyShowEntity> pValues) {
		this.propertyGroupName = groupName;
		this.values = pValues;
		productHits= 0;
		for(PropertyShowEntity pse : pValues){
			productHits+= pse.getHits();
		}
	}

	public void incrHits(int hits) {
		this.productHits+= hits;
	}

	public void addPropertyShowEntity(PropertyShowEntity showValue) {
		this.values.add(showValue);
	}

	public String toString() {
		return new Gson().toJson(this);
	}

	/**
	 * do no modify it
	 * @return
	 */
	public List<PropertyShowEntity> getValues() {
		return values;
	}

	public int getProductHits() {
		return productHits;
	}

	public String getPropertyGroupName() {
		return propertyGroupName;
	}

	public double getScore() {
		return score;
	}

	/**
	 *  “不限” 的条件串
	 * @return
	 */
	public String getUnSelected() {
		return unSelected;
	}

	/**
	 *  设置 “不限” 的条件串
	 * 
	 */
	public void setUnSelected(String unSelected) {
		this.unSelected = unSelected;
	}

	/**
	 *  “不限” 是否高亮
	 * @return
	 */
	public boolean getHighlight() {
		return highlight;
	}

	/**
	 * 设置 “不限” 是否高亮
	 * @param highlight
	 */
	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}


	public static PropertyGroupEntity parseEntityFromJSON(JsonObject jo){
		return new Gson().fromJson(jo, PropertyGroupEntity.class);
	}

	public void setValues(List<PropertyShowEntity> values) {
		this.values = values;
		productHits= 0;
		for(PropertyShowEntity pse : values){
			productHits += pse.getHits();
		}
	}

	public void setPropertyGroupName(String propertyGroupName) {
		this.propertyGroupName = propertyGroupName;
	}
	
	

}

