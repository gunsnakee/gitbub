package com.meiliwan.emall.imeiliwan.search;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gson.JsonElement;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.imeiliwan.search.cache.CacheTools;
import com.meiliwan.emall.imeiliwan.search.vo.PivotEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductSpuEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity.GroupSort;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyShowEntity;
import com.meiliwan.emall.imeiliwan.search.vo.StoreEntity;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 解析SOLR返回的结果，填写每个属性上的URL, 取代旧的解析工具类
 * @author lgn
 *
 */
public class SearchResultParser {

	public static List<String> EMPTYLIST = ImmutableList.of();
	static int MAX_GROUPS_ALLOW = 10;
	static Comparator<String> strSort = new Comparator<String>() {
		public int compare(String o1, String o2) {
			return o1.compareTo(o2);
		}
	};

	static GroupSort groupSort  = new GroupSort(0.5f);
	

	static class Attrbute{
		private int index;
		int count;
		String pKeyValue;

		public Attrbute(String pKeyValue, int count){
			this.pKeyValue = pKeyValue;
			index = pKeyValue.indexOf("_");
			this.count = count;
		}

		public String getValue(){
			return pKeyValue.substring(index + 1);
		}


	}

	public static List<PivotEntity> extractCatTree(JsonObject solrResult) {
		JsonArray facetPivotList = solrResult.getAsJsonObject(SearchConstants.FACET_COUNTS).getAsJsonObject(SearchConstants.FACET_FIELDS).getAsJsonArray(SearchConstants.CATEGORY_TREE_STRING);
		Map<String, Map<String, Integer>> catTreeAggregator = new HashMap<String, Map<String, Integer>>();
		//group by scid
		for(int i = 0 ; i < facetPivotList.size();i+=2){
			String[] scidAndTcid = facetPivotList.get(i).getAsString().split(",");
			Map<String, Integer> availableTcids = catTreeAggregator.get(scidAndTcid[0]);
			if (availableTcids == null){
				availableTcids = new HashMap<String, Integer>();
				catTreeAggregator.put(scidAndTcid[0], availableTcids);
			}
			availableTcids.put(scidAndTcid[1], facetPivotList.get(i+1).getAsInt());
		}

		List<PivotEntity> pivotEntities = new ArrayList<PivotEntity>(facetPivotList.size());
		//----loop scid
		for(Entry<String, Map<String, Integer>> scidEntry: catTreeAggregator.entrySet()){
			PivotEntity secondCategoryEntity = new PivotEntity();
			String[] secondCategoryDetail = CacheTools.get().getCategoryDetailById(Integer.parseInt(scidEntry.getKey()));
			if (secondCategoryDetail == null){
				continue;
			}
			secondCategoryEntity.setId(secondCategoryDetail[0]);
			secondCategoryEntity.setName(secondCategoryDetail[2]);
			secondCategoryEntity.setTail(new ArrayList<PivotEntity>()); //设置第二级pivot节点
			pivotEntities.add(secondCategoryEntity);//add to tree
			int counts = 0;
			//loop-tcid
			for(Entry<String, Integer> tcidEntry : scidEntry.getValue().entrySet()){
				PivotEntity thirdCategoryEntity = new PivotEntity(); //创建展现的pivot
				String[] thirdCategoryDetail = CacheTools.get().getCategoryDetailById(Integer.parseInt(tcidEntry.getKey()));
				if (thirdCategoryDetail == null){
					continue;
				}
				thirdCategoryEntity.setId(thirdCategoryDetail[0]);
				thirdCategoryEntity.setName(thirdCategoryDetail[2]);
				thirdCategoryEntity.setCount(tcidEntry.getValue());
				counts += tcidEntry.getValue();
				secondCategoryEntity.getTail().add(thirdCategoryEntity);
			}

			secondCategoryEntity.setCount(counts);

		}
		return pivotEntities;

	}



	public static List<StoreEntity> extractStores(JsonObject solrResult, String store){
		JsonArray facetStoreList = solrResult.getAsJsonObject(SearchConstants.FACET_COUNTS).getAsJsonObject(SearchConstants.FACET_FIELDS).getAsJsonArray("store_ids");
		List<StoreEntity> result = new ArrayList<StoreEntity>();
		Set<String> storeSet = new HashSet<String>();
		String[] stores = new String[]{};
		if (!StringUtils.isEmpty(store)){
			stores = store.split(",");
			for(String s :  stores){
				storeSet.add(s);
			}
		}
		if (facetStoreList!= null ) {
			//
			List<Object> facetList = new ArrayList<Object>( facetStoreList.size() + storeSet.size());
			//一边转存到一个临时列表，一边删除URL上的
			for (int i = 0; i < facetStoreList.size(); i += 2) {
				storeSet.remove(facetStoreList.get(i).getAsString());
				facetList.add(facetStoreList.get(i).getAsString());
				facetList.add(facetStoreList.get(i+1).getAsInt());
			}
			//不在facet结果 但是在URL上的，设置成facetcount等于0
			for(String notFound : storeSet){
				facetList.add(notFound);
				facetList.add(0);
			}
			//补回
			for(String s :  stores){
				storeSet.add(s);
			}
			//在原来的逻辑上增加那些facetcount等于0的。
			//计算结果
			for (int i = 0; i < facetList.size(); i += 2) {
				String enName = (String)facetList.get(i);
				int count = (Integer)facetList.get(i+1);
				String[] storeFromCache = CacheTools.get().getStoreDetailByEn(enName);
				StoreEntity pstore = new StoreEntity();
				pstore.setEnName(enName);
				if (storeFromCache == null){	
					pstore.setStoreId(-1);
					pstore.setCnName(enName);
					continue;
				}else{
					pstore.setStoreId(new Integer(storeFromCache[0]));
					pstore.setCnName(storeFromCache[2]);
				}
				//set facet count
				pstore.setCount(count);
				//set selected
				if (storeSet.contains(pstore.getEnName())){
					pstore.setSelected(true);
					//不带这个store
					boolean removed = storeSet.remove(enName);
					pstore.setSearchNames(StringUtils.join(storeSet, ","));
					if (removed)
						storeSet.add(enName);
				}else{
					//带上这个store
					if (storeSet.isEmpty())
						pstore.setSearchNames(enName);
					else{
						pstore.setSearchNames(enName + "," + store);
					}
				}
				result.add(pstore);
			}
		}
		return result;
	}



	protected static void addPrice(PropertyGroupEntity priceRow, JsonArray priceResult) throws UnsupportedEncodingException {
		List<String[]> priceRanges = assessPriceRanges(priceResult);
		for(String[] priceRange : priceRanges){
			String[] startEnd = priceRange[0].split("-|>");
			if (startEnd.length >= 2){
				priceRow.getValues().add(new PropertyShowEntity(new String[]{SearchConstants.PRICE_ABBR, "价格", priceRange[0]}, Integer.parseInt(priceRange[1]),  "&sp=" + startEnd[0] + "&ep=" + startEnd[1]));
			}else if (startEnd.length == 1){
				priceRow.getValues().add(new PropertyShowEntity(new String[]{SearchConstants.PRICE_ABBR, "价格", priceRange[0].replace(">","以上")}, Integer.parseInt(priceRange[1]),  "&sp=" + startEnd[0] ));
			}else{
				continue;
			}
		}
	}


	public static List<String[]> assessPriceRanges( JsonArray priceFloors){
		int[] array = new int[priceFloors.size()];
		for(int i = 0 ; i < array.length; i+=2){
			array[i] = priceFloors.get(i).getAsInt();
			array[i+1] = priceFloors.get(i+1).getAsInt();
		}
		float sum = 0.0f;
		for(int i = array.length-1; i >= 0; i-=2){
			sum += array[i];
		}
		float boundf = sum * 0.1f;
		int bound = 0; //得到最大范围边界， 计算step
		int sum2 = 0;  
		int boundi = 0;
		for(int i = array.length-2; i >= 0; i-=2){
			sum2 += array[i+1];
			if (sum2 > boundf){
				bound = array[i];
				boundi = i;
				break;
			}
		}
		if (boundi == 0){
			return Collections.emptyList();
		}
		List<Integer> bounds = new ArrayList<Integer>();
		List<Integer> hitss = new ArrayList<Integer>();
		bounds.add(array[boundi]);
		hitss.add(sum2);
		float as = 0.0f;
		int step = bound <= 300 ? 20 :(bound <= 600 ? 50: 100);
		
		int hits = 0;
		for(int i = boundi-2; i>= 0 ; i -= 2){
			hits += array[i+1];
			as += (array[i+1]/sum);
			if (as > 0.16){
				int nbound = array[i]/ step * step;
				if (nbound != bounds.get(bounds.size()-1)){
					bounds.add(nbound);
					hitss.add(hits);
				}
				as = 0.0f;
				hits = 0;
						
			}
		}
		if (as > 0){
			int nbound = array[0]/ step * step;
			if (nbound != bounds.get(bounds.size()-1)){
				bounds.add(nbound);
				hitss.add(hits);
			}
		}
		Collections.reverse(bounds);
		Collections.reverse(hitss);
		List<String[]> result = new ArrayList<String[]>();
		for(int i = 0 ; i < bounds.size()-1; i ++){
			//xxx-yyy
			result.add(new String[]{bounds.get(i) + "-" + (bounds.get(i+1)), hitss.get(i).toString()});
		}
		result.add(new String[]{bound+">",  hitss.get(hitss.size()-1).toString()});
		
		return result;

	}

	/**
	 * 解析可供展示的属性，本初处理完之后可直接展示。
	 * 确定请求时候必须有facet属性 才能调用，否则可能报错
	 * @param solrResult
	 * @param selectedGroup
	 * @param param
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static List<PropertyGroupEntity> extractPropertyGroups(JsonObject solrResult,SearchParameters param) throws UnsupportedEncodingException {

		JsonArray propertyList = null;//取出属性 facet， 务必列表要id有序排列
		JsonElement facets = solrResult.get(SearchConstants.FACET_COUNTS);
		if (facets != null){
			JsonObject  fcount = facets.getAsJsonObject();
			JsonElement facetFields =  fcount.get(SearchConstants.FACET_FIELDS) ;
			if (facetFields != null){
				propertyList =   facetFields.getAsJsonObject().getAsJsonArray("property_ids") ;
			}
		}
		//开始合并属性组
		List<PropertyGroupEntity> topK = new ArrayList<PropertyGroupEntity>();
		
		//先聚合, 如果属性过多，就不返回给用户了
		if( propertyList.size() <= 180){
			//聚合
			HashMap<String, List<Attrbute>> aggregator = new HashMap<String, List<Attrbute>>();
			for(int i = 0 ; i < propertyList.size(); i+=2){ //遍历排序的facet列表

				String[] pKeyValue = propertyList.get(i).getAsString().split("_");//11_223, 00b_111
				if (!param.containsAttributeKey(pKeyValue[0])){ //过滤已选的行
					List<Attrbute> existList = aggregator.get(pKeyValue[0]);
					if (existList == null){
						existList = new ArrayList<Attrbute>();
						aggregator.put(pKeyValue[0], existList);
					}
					existList.add(new Attrbute(propertyList.get(i).getAsString(), propertyList.get(i+1).getAsInt()));
				}
			}
			//聚合完成
			
			for(Entry<String, List<Attrbute>> pKeyValueList :  aggregator.entrySet()){
				if (pKeyValueList.getValue().size() <= 1){
					continue;
				}
				List<PropertyShowEntity> propertyShowList = new ArrayList<PropertyShowEntity>();
				for(Attrbute attrbute : pKeyValueList.getValue()){
					String[] detail ;
					if (       pKeyValueList.getKey().equals(SearchConstants.BRAND_ABBR)){
						detail = CacheTools.get().getBrandDetailById(Integer.parseInt(attrbute.getValue()));
					}else if ( pKeyValueList.getKey().equals(SearchConstants.PLACE_ABBR)){
						detail = CacheTools.get().getPlaceDetailById(Integer.parseInt(attrbute.getValue()));
					}else{
						detail = CacheTools.get().getPropDetailById(Integer.parseInt(attrbute.getValue()));
					}
					if (detail != null){
						PropertyShowEntity pse = new PropertyShowEntity(new String[]{attrbute.pKeyValue, detail[1], detail[2]}, attrbute.count);
						pse.setQueryNew( "&attr=" + attrbute.pKeyValue);//下一步点击的属性
						propertyShowList.add(pse);
					}
				}
				PropertyGroupEntity group = new PropertyGroupEntity(propertyShowList.get(0).getPropertyValue()[1], propertyShowList);

				//同名属性行是否存在， 如果有，说明该属性存在于不同类目，筛选会丢失数据。
				//比如同叫 “大小”的属性组  但是属性id不一样， 问题属于属性设计的问题。
				for(PropertyGroupEntity existGroup : topK){
					if (stringNearDuplicate(existGroup.getPropertyGroupName(), group.getPropertyGroupName())){
						existGroup.incrHits(-(group.getProductHits() >> 0));
						group.incrHits(-(group.getProductHits() >> 0));
					}
				}
				if (group.getValues().size() > 1) {//属性值不止一个的, 上面那次过滤未必过滤干净，也可能是detail获取不到
					topK.add(group);
					if ( pKeyValueList.getKey().equals(SearchConstants.BRAND_ABBR)){
						group.incrHits(600000000);
					}
					if ( pKeyValueList.getKey().equals(SearchConstants.PLACE_ABBR)){
						group.incrHits(500000000);
					}
				}
			}
		}

		//价格区间排序
		if (!SearchConstants.hasPrice(param.hasPrice())) { // 不包括价格过滤，因此要展示价格
			JsonArray priceFacet = solrResult.getAsJsonObject(SearchConstants.FACET_COUNTS).getAsJsonObject(SearchConstants.FACET_FIELDS).getAsJsonArray("price_norm_i");
			PropertyGroupEntity priceRow = new PropertyGroupEntity("价格", new ArrayList<PropertyShowEntity>());
			addPrice(priceRow, priceFacet);
			if(priceRow.getValues().size() > 1){ //只有一个项的属性就不显示了
				priceRow.incrHits(1000000); //强制排序很高
				topK.add(priceRow);
			}
		}
		List<PropertyGroupEntity> propertyGroups = new ArrayList<PropertyGroupEntity>();
		Collections.sort(topK, groupSort);
		for(int i = 0 ; i < Math.min(MAX_GROUPS_ALLOW, topK.size()) ; i++){
			PropertyGroupEntity group = topK.get(i);
			if (group.getProductHits() > 0){
				propertyGroups.add(group);
			}
		}

		return propertyGroups;

	}

	private static boolean stringNearDuplicate(String name1, String name2){
		int length1 = name1.length();
		int length2 = name2.length();
		
		int commonPrefixLength = Strings.commonPrefix(name1, name2).length();
		int commonSuffixLength = Strings.commonSuffix(name1, name2).length();
		double ratio1 = commonPrefixLength / (length1 + 0.0);
		double ratio2 = commonPrefixLength / (length2 + 0.0);
		double ratio3 = commonSuffixLength / (length1 + 0.0);
		double ratio4 = commonSuffixLength / (length2 + 0.0);
		
		if ( ratio1 > 0.5 && ratio2 > 0.5 && ratio3 > 0.5 && ratio4 > 0.5 ) {
			return true;
		}else{
			return false;
		}
	}
	

	/**
	 * 必须不是grouping的结果！,注意ProductEntity 里面成员有限,
	 * @param solrResult
	 * @param hightQuery
	 * @return
	 */
	public static List<ProductEntity> getProductSkuList(JsonObject solrResult, String highlightQuery) {
		JsonObject response = solrResult.getAsJsonObject("response");
		//
		JsonArray docs = response.getAsJsonArray("docs");
		List<ProductEntity> productList = new ArrayList<ProductEntity>(docs.size());
		for (int i = 0; i < docs.size(); i++) {
			JsonObject doc = docs.get(i).getAsJsonObject();
			ProductEntity product = new ProductEntity();
			product.parseFromDoc(doc, highlightQuery);
			productList.add(product);
		}
		return productList;
	}

	//
	public static List<ProductSpuEntity> getProductSpuList(JsonObject solrResult, String highlightQuery){
		JsonArray spuResult = solrResult.getAsJsonObject("grouped").getAsJsonObject("spu_id").getAsJsonArray("groups");
		List<ProductSpuEntity> result = new ArrayList<ProductSpuEntity>(spuResult.size());
		for(int i = 0 ; i < spuResult.size();i++){
			JsonObject doc = spuResult.get(i).getAsJsonObject();
			ProductSpuEntity spuEntity = new ProductSpuEntity();
			spuEntity.parseFromDoc(doc, highlightQuery);
			result.add(spuEntity);
		}
		return result;
	}

	public static int extractNumFound(boolean isSpu, JsonObject solrResult){
		if (!isSpu){
			return solrResult.getAsJsonObject("response").get("numFound").getAsInt();
		}else{
			return solrResult.getAsJsonObject("grouped").getAsJsonObject("spu_id").get("ngroups").getAsInt();
		}
	}

	/**
	 * 虽然不要求结果都是一个spuid的，但最好是 skuid 是同一个商品
	 * 只是通过图片来识别异图，如果不同spu必然异图了。
	 * @param solrResult
	 * @param skuId
	 * @return
	 */
	public static List<ProductEntity> collapseSkusFromResult(JsonObject solrResult, String firstSkuId){
		JsonObject response = solrResult.getAsJsonObject("response");
		//
		JsonArray docs = response.getAsJsonArray("docs");
		List<ProductEntity> productList = new ArrayList<ProductEntity>(docs.size());
		HashMap<String, ProductEntity> differentImageProducts = new HashMap<String, ProductEntity>();
		for (int i = 0; i < docs.size(); i++) {
			JsonObject doc = docs.get(i).getAsJsonObject();
			String defaultImg = docs.get(i).getAsJsonObject().get("default_image_uri").getAsString();
			String id = docs.get(i).getAsJsonObject().get("id").getAsString();
			if (id.equals(firstSkuId)){  
				ProductEntity product = new ProductEntity();
				product.parseFromDoc(doc, null);
				differentImageProducts.put(defaultImg, product);
				productList.add(product); //放到第一个
			}else{
				if (!differentImageProducts.containsKey(defaultImg)){
					ProductEntity product = new ProductEntity();
					product.parseFromDoc(doc, null);
					differentImageProducts.put(defaultImg, product);
				}
			}
		}
		if (productList.isEmpty()){ //如果是空的 说明没有这个sku id的商品，
			productList.addAll(differentImageProducts.values());
		}else{  
			for(Entry<String, ProductEntity> differentProduct : differentImageProducts.entrySet()){
				if (!differentProduct.getValue().getProId().equals(firstSkuId)){ // 
					productList.add(differentProduct.getValue());
				}
			}
		}

		return productList;
	}
	
}
