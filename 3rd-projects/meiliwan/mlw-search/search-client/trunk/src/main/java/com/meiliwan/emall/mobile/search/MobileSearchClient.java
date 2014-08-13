package com.meiliwan.emall.mobile.search;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.imeiliwan.search.SearchConstants;
import com.meiliwan.emall.imeiliwan.search.SearchExecutor;
import com.meiliwan.emall.imeiliwan.search.SearchParameters;
import com.meiliwan.emall.imeiliwan.search.SearchResultParser;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyShowEntity;
import com.meiliwan.emall.imeiliwan.search.vo.VOList;
import com.meiliwan.emall.mobile.search.PropertyFilterBag.PropertyGroup;



/**
 * 
 *  @Description 移动端搜索接口
 *	@author shanbo.liang
 */
public class MobileSearchClient {

	/**
	 * 如果个数是0 就返回Null
	 * @param keyword
	 * @param storeEn
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
//	public static ProdPropertiesFilter getAllFilters(String keyword, String storeEn) throws UnsupportedEncodingException, BaseException{
//		
//		SearchParameters param = new SearchParameters(null, null, null, null, null);
//		
//		String result = SearchExecutor.skuAllSearch(param, keyword, storeEn, null, null, null, 0, 0, null);
//		
//		JsonParser jParser = new JsonParser();
//		JsonObject jObj = jParser.parse(result).getAsJsonObject();
//		
//		int numFound = SearchResultParser.extractNumFound(false, jObj);
//		
//		if(numFound == 0){
//			return null;
//		}
//		
//		ProdPropertiesFilter filter = new ProdPropertiesFilter();
//		filter.setNumFounds(numFound);
//		
//		//处理展馆	
//		filter.setStores(SearchResultParser.extractStores(jObj, storeEn));
//		//处理目录树
//		filter.setCatTrees(SearchResultParser.extractCatTree(jObj));
//		//处理属性
//		List<PropertyGroupEntity> propertyGroupEntities = SearchResultParser.extractPropertyGroups(jObj, param);		
//			
//		List<ProdPropertiesFilter.PropertyGroup> propertyGroups = new ArrayList<ProdPropertiesFilter.PropertyGroup>();
//		for(int i = 0; i < propertyGroupEntities.size(); i++){
//			PropertyGroupEntity propertyGroupEntity = propertyGroupEntities.get(i);
//			
//			List<PropertyShowEntity> propertyShowEntities = propertyGroupEntity.getValues();			
//			
//			ProdPropertiesFilter.PropertyGroup propertyGroup = new PropertyGroup();
//			
//			propertyGroup.setGroupName(propertyGroupEntity.getPropertyGroupName());
//			
//			List<String[]> propertyValues = new ArrayList<String[]>();
//			for(PropertyShowEntity showEntity : propertyShowEntities){
//				String[] values = showEntity.getPropertyValue();
//				String[] idCountName = new String[]{null, null, null};
//				idCountName[0] = values[0];
//				idCountName[1] = showEntity.getHits() + "";
//				idCountName[2] = values[2];
//				propertyValues.add(idCountName);
//			}
//			propertyGroup.setPropertyValues(propertyValues);
//			
//			
//			propertyGroups.add(propertyGroup);
//		}
//		
//		filter.setPropertyGroups(propertyGroups);
//		
//		return filter;
//	}
	
	/**
	 * 如果个数是0 就返回Null
	 * @param keyword
	 * @param storeEn
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	@Deprecated
	public static PropertyFilterBag getAllFilters(String keyword, String storeEn, String brandId, String placeId, 
			String startPrice, String endPrice, String pIds) throws UnsupportedEncodingException, BaseException{
		
		SearchParameters param = new SearchParameters(startPrice, endPrice, pIds.split(","));
		
		String result = SearchExecutor.skuAllSearch(param, keyword, storeEn, null, null, null, 0, 0, null);
		
		JsonParser jParser = new JsonParser();
		JsonObject jObj = jParser.parse(result).getAsJsonObject();
		
		int numFound = SearchResultParser.extractNumFound(false, jObj);
		
		if(numFound == 0){
			return null;
		}
		
		PropertyFilterBag filter = new PropertyFilterBag();
		filter.setNumFounds(numFound);
		
		//处理展馆	
		filter.setStores(SearchResultParser.extractStores(jObj, storeEn));
		//处理目录树
		filter.setCatTrees(SearchResultParser.extractCatTree(jObj));
		//处理属性
		List<PropertyGroupEntity> propertyGroupEntities = SearchResultParser.extractPropertyGroups(jObj, param);		
			
		List<PropertyFilterBag.PropertyGroup> propertyGroups = new ArrayList<PropertyFilterBag.PropertyGroup>();
		for(int i = 0; i < propertyGroupEntities.size(); i++){
			PropertyGroupEntity propertyGroupEntity = propertyGroupEntities.get(i);
			
			List<PropertyShowEntity> propertyShowEntities = propertyGroupEntity.getValues();			
			
			PropertyFilterBag.PropertyGroup propertyGroup = new PropertyGroup();
			
			propertyGroup.setGroupName(propertyGroupEntity.getPropertyGroupName());
			
			List<String[]> propertyValues = new ArrayList<String[]>();
			for(PropertyShowEntity showEntity : propertyShowEntities){
				String[] values = showEntity.getPropertyValue();
				String[] idCountName = new String[]{values[0], showEntity.getHits() + "", values[2]};
				propertyValues.add(idCountName);
			}
			propertyGroup.setPropertyValues(propertyValues);
			
			
			propertyGroups.add(propertyGroup);
		}
		
		filter.setPropertyGroups(propertyGroups);
		
		return filter;
	}
	
	@Deprecated
	public static VOList getSiteProducts(String brandId, String placeId, 
			String startPrice, String endPrice, String pIds, String keyword, String storeEn,
			String sortCode, int start, int rows) throws UnsupportedEncodingException, BaseException{
		
		SearchParameters param = new SearchParameters(startPrice, endPrice, StringUtils.isBlank(pIds) ? null : pIds.split(","));
		
		String sortString = SearchConstants.parseSort(sortCode);
		
		String result = SearchExecutor.skuSearch(param, keyword, storeEn, null, sortString, new String[]{"state:1"}, 
				start, rows, SearchConstants.FILEDLIST_MOBILESITE);
		
		JsonParser jParser = new JsonParser();
		JsonObject jObj = jParser.parse(result).getAsJsonObject();

		int numFound = SearchResultParser.extractNumFound(false, jObj);
		VOList voList = new VOList();
		if(numFound == 0){
			return voList;
		}
		
		List<ProductEntity> productEntities = SearchResultParser.getProductSkuList(jObj, null);
		
		List<ProductMobileVO> productMobileVOs = ProductMobileVO.convertEntitiesToVos(productEntities);
		
		
		voList.setNumFound(numFound);
		voList.setProductList(productMobileVOs);
		
		return voList;		
	}
	
	@Deprecated
	public static VOList getCountryProducts(String placeId, String sortCode, int start, int rows) 
					throws UnsupportedEncodingException, BaseException{
		
		SearchParameters param = new SearchParameters(null, null, new String[]{SearchConstants.PLACE_ABBR + "_" + placeId});

		String sortString = SearchConstants.parseSort(sortCode);
		
		String result = SearchExecutor.skuSearch(param, null, null, null, sortString, new String[]{"is_falls:1", "state:1"}, start, rows, 
				SearchConstants.FILEDLIST_MOBILEPLACE);
		
		JsonObject jObj = new JsonParser().parse(result).getAsJsonObject();

		
		int numFound = SearchResultParser.extractNumFound(false, jObj);
		VOList voList = new VOList();
		if(numFound == 0){
			return voList;
		}
		
		List<ProductEntity> productEntities = SearchResultParser.getProductSkuList(jObj, null);
		
		
		List<FallsMobileVO> fallsMobileVOs = FallsMobileVO.convertEntitiesToVOs(productEntities);		
		
		voList.setNumFound(numFound);
		voList.setProductList(fallsMobileVOs);
		
		return voList;
	}
	
	
	public static PropertyFilterBag searchPropertyFilterBag(String keyword, String storeEn, String tcid, String startPrice, String endPrice, String[] attr , boolean selectedParamGroup) throws UnsupportedEncodingException, BaseException{
		SearchParameters param = new SearchParameters(startPrice, endPrice, attr);
		
		String result = SearchExecutor.skuAllSearch(param, keyword, storeEn, tcid, null, null, 0, 0, null);
		
		JsonParser jParser = new JsonParser();
		JsonObject jObj = jParser.parse(result).getAsJsonObject();
		
		int numFound = SearchResultParser.extractNumFound(false, jObj);
		
		if(numFound == 0){
			return null;
		}
		
		PropertyFilterBag filter = new PropertyFilterBag();
		filter.setNumFounds(numFound);
		
		//处理展馆	
		filter.setStores(SearchResultParser.extractStores(jObj, storeEn));
		//处理目录树
		filter.setCatTrees(SearchResultParser.extractCatTree(jObj));
		//获取属性 + 转换
		List<PropertyGroupEntity> propertyGroupEntities = SearchResultParser.extractPropertyGroups(jObj, param);		
		filter.setPropertyGroupsUsingEntities(propertyGroupEntities);
		
		if (selectedParamGroup){
			List<PropertyGroupEntity> fetchParameterGroup = SearchExecutor.fetchParameterGroup(param, keyword, storeEn, null, new String[]{"state:1"});
			filter.setSelectedGroupsUsingEntities(fetchParameterGroup);
		}
		return filter;

	}
	
	public static VOList searchDfgProducts(String placeId, String sortCode, int start, int rows) 
			throws UnsupportedEncodingException, BaseException{
		SearchParameters param = new SearchParameters(null, null, new String[]{SearchConstants.PLACE_ABBR + "_" + placeId});

		String sortString = SearchConstants.parseSort(sortCode);
		
		String result = SearchExecutor.skuSearch(param, null, null, null, sortString, new String[]{"is_falls:1", "state:1"}, start, rows, 
				SearchConstants.FILEDLIST_MOBILEPLACE);
		

		JsonObject jObj = new JsonParser().parse(result).getAsJsonObject();
		
		int numFound = SearchResultParser.extractNumFound(false, jObj);
		VOList voList = new VOList();
		if(numFound == 0){
			return voList;
		}
		
		List<ProductEntity> productEntities = SearchResultParser.getProductSkuList(jObj, null);
		
		List<FallsMobileVO> fallsMobileVOs = FallsMobileVO.convertEntitiesToVOs(productEntities);		
		
		voList.setNumFound(numFound);
		voList.setProductList(fallsMobileVOs);
		
		return voList;
	}
	
	
	
	public static VOList searchProducts(String startPrice, String endPrice, String[] attr, String keyword, String storeEn,
			String sortCode, int start, int rows) throws UnsupportedEncodingException, BaseException{
		SearchParameters param = new SearchParameters(startPrice, endPrice, attr);
		
		String sortString = SearchConstants.parseSort(sortCode);
		
		String result = SearchExecutor.skuSearch(param, keyword, storeEn, null, sortString, new String[]{"state:1"}, 
				start, rows, SearchConstants.FILEDLIST_MOBILESITE);

		JsonObject jObj = new JsonParser().parse(result).getAsJsonObject();

		int numFound = SearchResultParser.extractNumFound(false, jObj);
		VOList voList = new VOList();
		if(numFound == 0){
			return voList;
		}
		List<ProductEntity> productEntities = SearchResultParser.getProductSkuList(jObj, null);
		
		List<ProductMobileVO> productMobileVOs = ProductMobileVO.convertEntitiesToVos(productEntities);
		
		
		voList.setNumFound(numFound);
		voList.setProductList(productMobileVOs);
		
		return voList;		
	}
		
}
