package com.meiliwan.emall.mobile.search;

import java.util.ArrayList;
import java.util.List;




import com.google.gson.Gson;
import com.meiliwan.emall.imeiliwan.search.vo.PivotEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyShowEntity;
import com.meiliwan.emall.imeiliwan.search.vo.StoreEntity;

/**
 * 移动端属性、店铺、分类树的合集VO
 *  @Description TODO
 *	@author shanbo.liang
 */
public class PropertyFilterBag {
	private int numFounds;
	private List<StoreEntity> stores;
	private List<PivotEntity> catTrees;
	private List<PropertyGroup> selectedGroups; //已经选的属性组
	private List<PropertyGroup> propertyGroups; //新的可选属性组

	
	public static class PropertyGroup{
		String groupName;
		List<String[]> propertyValues;
		//String tag;
		
		public String getGroupName() {
			return groupName;
		}

		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}

		public List<String[]> getPropertyValues() {
			return propertyValues;
		}

		public void setPropertyValues(List<String[]> propertyValues) {
			this.propertyValues = propertyValues;
		}


		public String toString(){
			return new Gson().toJson(this);
		}
		
		
		public static PropertyGroup convertFromGroupEntity(PropertyGroupEntity pge){
			List<PropertyShowEntity> propertyShowEntities = pge.getValues(); 			
			
			PropertyGroup newGroup = new PropertyGroup();
			
			newGroup.setGroupName(pge.getPropertyGroupName());
			
			List<String[]> propertyValues = new ArrayList<String[]>();
			for(PropertyShowEntity showEntity : propertyShowEntities){
				String[] values = showEntity.getPropertyValue();
				String[] idCountName = new String[]{values[0], showEntity.getHits() + "", values[2]};
				propertyValues.add(idCountName);
			}
			newGroup.setPropertyValues(propertyValues);
			return newGroup;
					
		}
		
	}

	public int getNumFounds() {
		return numFounds;
	}

	public void setNumFounds(int numFounds) {
		this.numFounds = numFounds;
	}

	public List<PropertyGroup> getPropertyGroups() {
		return propertyGroups;
	}

	public List<PropertyGroup> getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(List<PropertyGroup> selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	public void setPropertyGroups(List<PropertyGroup> propertyGroups) {
		this.propertyGroups = propertyGroups;
	}

	public void setPropertyGroupsUsingEntities(List<PropertyGroupEntity> pges) {
		this.propertyGroups = new ArrayList<PropertyGroup>();
		for(PropertyGroupEntity pge:pges){
			this.propertyGroups.add(PropertyGroup.convertFromGroupEntity(pge));
		}
	}
	
	public void setSelectedGroupsUsingEntities(List<PropertyGroupEntity> pges) {
		this.selectedGroups = new ArrayList<PropertyGroup>();
		for(PropertyGroupEntity pge:pges){
			this.selectedGroups.add(PropertyGroup.convertFromGroupEntity(pge));
		}
	}
	
	
	
	public List<StoreEntity> getStores() {
		return stores;
	}

	public void setStores(List<StoreEntity> stores) {
		this.stores = stores;
	}

	public List<PivotEntity> getCatTrees() {
		return catTrees;
	}

	public void setCatTrees(List<PivotEntity> catTrees) {
		this.catTrees = catTrees;
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}

	
}
