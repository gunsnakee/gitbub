package com.meiliwan.emall.imeiliwan.search;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.util.concurrent.ConcurrentExecutor;
import com.meiliwan.emall.imeiliwan.search.SearchParameters.SolrQuery;
import com.meiliwan.emall.imeiliwan.search.cache.CacheTools;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductRecommendVO;
import com.meiliwan.emall.imeiliwan.search.vo.ProductVO;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyShowEntity;
import com.meiliwan.emall.search.common.CollectionConfig;
import com.meiliwan.emall.search.common.SearchClient;

public class SearchExecutor {

	static MLWLogger log = MLWLoggerFactory.getLogger(SearchExecutor.class);


	/**
	 * 使用了MD5字符串加密
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param addQueries
	 * @param start
	 * @param rows
	 * @param returnSpu
	 * @param fl
	 * @return
	 */
	private static String getCacheKey(SearchParameters param, String keyword, String storeEn,String tcid, String sortString ,String[] addQueries, int start, int rows, boolean returnSpu, String fl){
		StringBuilder builder = new StringBuilder();
		builder.append(param.toString()).append("&keywowrd" ).append(keyword)
		.append("&store=").append(storeEn).append("&tcid=").append(tcid).append("&sort=").append(sortString)
		.append("&startrows=").append(start).append(",").append(rows).append("&fl").append(fl);
		if (addQueries!= null){
			builder.append("&other=");
			for(String addq : addQueries){
				builder.append(addq).append(",");
			}
		}
		builder.append("&isspu=").append(returnSpu);
		return EncryptTools.EncryptByMD5(builder.toString());
	}

    public static String keywordFilter(String keyword) {
        if (!Strings.isNullOrEmpty(keyword)) {
        	return keyword.replaceAll("[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]", " ");
//            keyword = TextUtil.cleanHTML(RegexUtil.specialFilter(keyword));
        }
        return keyword;
    }

    private static void fillParameters(SolrQuery solrQuery , String keyword, String tcid, String storeEn, String[] addQueries){
		if (StringUtils.isNotBlank(storeEn))
			solrQuery.addToFq("store_ids:("+ StringUtils.join(storeEn.split(","), " OR ") + ")");
		if (StringUtils.isNotBlank(tcid))
			solrQuery.addToFq("third_category_id:"+tcid);
		//keyword
		if (StringUtils.isNotBlank(keyword)){
			solrQuery.addKeyword(keyword);
		}
		if (addQueries!=null && addQueries.length>0){
			for(String addQuery : addQueries){
				solrQuery.addToQ(addQuery);
			}
		}
    }
    
	/**
	 * 获取属性、店铺、类目树的facet。 只需要列表页的请走{@link basicSearch}
	 * 注意店铺storeEn 三级类目项tcid的facet 是排除自己的facet
	 * store and pivot facet are not change during 'storeEn' and 'tcid' variation
	 * @param param  属性参数对象
	 * @param keyword 关键词
	 * @param storeEn  店铺英文名
	 * @param tcid  三级类目id
	 * @param sortString 从searchconstants 里获取过的 xxx desc这样的
	 * @param start 从多少个开始
	 * @param rows 每页多少条
	 * @param returnSpu 是否使用SKU聚合
	 * @param fl 返回doc字段  aa,bb,cc
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String basicAllSearch(SearchParameters param, String keyword, String storeEn,String tcid, String sortString ,String[] addQueries, int start, int rows, boolean returnSpu, String fl) throws UnsupportedEncodingException, BaseException{
		keyword = keywordFilter(keyword);
		if (param == null){
			param = new SearchParameters(null, null, null);
		}
		String key = getCacheKey(param, keyword, storeEn, tcid, sortString, addQueries, start, rows, returnSpu, fl);
		//cache first
		try{
			String result = ShardJedisTool.getInstance().get(JedisKey.search$key, key);
			if (result != null){
				ShardJedisTool.getInstance().set(JedisKey.search$key, key, result);
				return result;
			}
		}catch(JedisClientException e){
			log.error(e, "fail to fetch search cache !",IPUtil.getLocalIp());
		}

		CollectionConfig cc = SearchClient.get().getConfigByCollection(SearchConstants.productCollection);
		CollectionConfig.Strategy conf = cc.getStratagy();
		String qString = conf.getString("keywordSearch");
		String qf = conf.getString("keywordSearchqf");

		SolrQuery sq = param.generateSolrQuery();
		//sort
		if (StringUtils.isNotEmpty(sortString)){
			sq.setSort(sortString);
		} else{
			String sort = conf.getString("keywordSearchSort");
			sq.setSort(sort);
		}
		//properties facet
		if (!SearchConstants.hasPrice(param.hasPrice())){//如果没有价格
			sq.addFacetFieldSortByIndex("price_norm_i");	//添加facet.field=mlwprice_volume和facet按索引排序
		}
		sq.addFacetFieldSortByIndex("property_ids");

		//store_id & store_id facet
		String store_ids = StringUtils.isNotEmpty(storeEn)? StringUtils.join(storeEn.split(","), " OR "): "*";
		sq.pushTagFacetCondition("store", "facet.field=store_ids", "store_ids:(" + store_ids + ")");
		//tcid & tcid facet
		String third_category_id =  StringUtils.isNotEmpty(tcid)? tcid: "*";
		sq.pushTagFacetCondition("pivot", "facet.field="+SearchConstants.CATEGORY_TREE_STRING, "third_category_id:" + third_category_id);

		//keyword
		if (StringUtils.isNotBlank(keyword)){
			sq.addKeyword(keyword);
		}
		//other query
		if (addQueries!=null && addQueries.length>0){
			for(String addQuery : addQueries){
				sq.addToQ(addQuery);
			}
		}
		//start rows
		sq.setStartRows(start, rows);
		//fl
		if (StringUtils.isNotBlank(fl)){
			sq.setFieldList(fl);
		}
		//grouping; only in spu search
		if (returnSpu){
			sq.setGrouping("spu_id", 20, "sku_image_i desc", null);
		}

		String urlParam = sq.format(qString, qf);
		log.debug(urlParam);
		return SearchClient.get().executeSearch(SearchConstants.productCollection, urlParam);
	}

	/**
	 * 不包含facet那些, 各种排序可以用它
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param addQueries
	 * @param start
	 * @param rows
	 * @param returnSpu
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String basicSearch(SearchParameters param, String keyword, String storeEn,String tcid, String sortString ,String[] addQueries, int start, int rows, boolean returnSpu, String fl) throws UnsupportedEncodingException, BaseException{
		keyword = keywordFilter(keyword);
		if (param == null){
			param = new SearchParameters(null, null, null);
		}
		String key = getCacheKey(param, keyword, storeEn, tcid, sortString, addQueries, start, rows, false, fl);
		//cache first
		try{
			String result = ShardJedisTool.getInstance().get(JedisKey.search$key, key);
			if (result != null){
				ShardJedisTool.getInstance().set(JedisKey.search$key, key, result);
				return result;
			}
		}catch(JedisClientException e){
			log.error(e, "fail to fetch search cache !",IPUtil.getLocalIp());
		}

		CollectionConfig cc = SearchClient.get().getConfigByCollection(SearchConstants.productCollection);
		CollectionConfig.Strategy conf = cc.getStratagy();
		String qString = conf.getString("keywordSearch");
		String qf = conf.getString("keywordSearchqf");

		SolrQuery sq = param.generateSolrQuery();
		//sort
		if (StringUtils.isNotEmpty(sortString)){
			sq.setSort(sortString);
		} else{
			String sort = conf.getString("keywordSearchSort");
			sq.setSort(sort);
		}
		//storeid& tcid & keyword & addQueries
		fillParameters(sq, keyword, tcid, storeEn, addQueries);
		//start rows
		sq.setStartRows(start, rows);
		//fl
		if (StringUtils.isNotBlank(fl)){
			sq.setFieldList(fl);
		}
		//grouping; only in spu search
		if (returnSpu){
			sq.setGrouping("spu_id", 20, "sku_image_i desc", null);
		}
		String urlParam = sq.format(qString, qf);
		return SearchClient.get().executeSearch(SearchConstants.productCollection, urlParam);
	}	
	/**
	 * 不包含facet的 SPU搜索
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param start
	 * @param rows
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String spuSearch(SearchParameters param, String keyword, String storeEn,String tcid,	String sortString ,int start, int rows,  String fl) throws UnsupportedEncodingException, BaseException{
		return basicSearch(param, keyword, storeEn, tcid, sortString, new String[]{"state:1"}, start, rows, true, fl);
	}

	/**
	 * 包含facet的 SPU搜索， 只需要列表页的请走{@link spuSearch}
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param start
	 * @param rows
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String spuAllSearch(SearchParameters param, String keyword, String storeEn,String tcid,	String sortString ,int start, int rows,  String fl) throws UnsupportedEncodingException, BaseException{
		return basicAllSearch(param, keyword, storeEn, tcid, sortString, new String[]{"state:1"}, start, rows, true, fl);
	}





	/**
	 * 不包含facet grouping那些东西!自己添加state:1 等等条件，
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param addQueries 需要手动添加state:1条件！ 地方馆需要添加 is_falls:1
	 * @param start
	 * @param rows
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String skuSearch(SearchParameters param, String keyword, String storeEn, String tcid,	String sortString , String[] addQueries,int start, int rows,  String fl) throws UnsupportedEncodingException, BaseException{
		return basicSearch(param, keyword, storeEn, tcid, sortString, addQueries, start, rows, false, fl);
	}

	/**
	 * 包含facet的 SKU搜索， 自己添加state:1条件，   只需要列表页的请走{@link skuSearch}
	 * 注意店铺storeEn 三级类目项tcid的facet 是排除自己的facet
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param sortString
	 * @param addQueries
	 * @param start
	 * @param rows
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static String skuAllSearch(SearchParameters param, String keyword, String storeEn, String tcid,	String sortString , String[] addQueries,int start, int rows,  String fl) throws UnsupportedEncodingException, BaseException{
		return basicAllSearch(param, keyword, storeEn, tcid, sortString, addQueries, start, rows, false, fl);
	}


	/**
	 * 不包含state:1
	 * @param ids
	 * @param highlight
	 * @param fl
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws JsonSyntaxException
	 * @throws BaseException
	 */
	public static Map<String, ProductVO> getSkuById(Collection<String> ids, String highlight, String fl) throws UnsupportedEncodingException, JsonSyntaxException, BaseException{
		if(ids == null || ids.isEmpty()){
			return Collections.emptyMap();
		}
		CollectionConfig cc = SearchClient.get().getConfigByCollection(SearchConstants.productCollection);
		CollectionConfig.Strategy conf = cc.getStratagy();
		String qString = conf.getString("keywordSearch");
		String qf = conf.getString("keywordSearchqf");

		SolrQuery sq = new SearchParameters(null, null, null).generateSolrQuery();
		String sort = conf.getString("keywordSearchSort");
		sq.setSort(sort);
		sq.addToQ("id:(" + StringUtils.join(ids, " OR ") + ")");
		sq.setStartRows(0, ids.size() + 1);
		sq.setFieldList(fl);
		String urlParam = sq.format(qString, qf);
		JsonParser jp = new JsonParser();
		JsonObject resp = jp.parse(SearchClient.get().executeSearch(SearchConstants.productCollection, urlParam)).getAsJsonObject();
		Map<String, ProductVO> idDocMap = new HashMap<String, ProductVO>();
		List<ProductEntity> productSkuList = SearchResultParser.getProductSkuList(resp, highlight);
		List<ProductRecommendVO> convertEntitiesToVOs = ProductRecommendVO.convertEntitiesToVOs(productSkuList);
		for(int i = 0 ; i < convertEntitiesToVOs.size();i++){
			ProductRecommendVO vo = convertEntitiesToVOs.get(i);
			idDocMap.put(vo.getProId(), vo);
		}
		return idDocMap;

	}
	
	
	/**
	 * 让每个已选属性 还能出现同组其他可选的， 靠的是 搜其他属性组条件来获取的。
	 * 本功能务必谨慎使用，因为可能需要请求多次。
	 * @param param
	 * @param keyword
	 * @param storeEn
	 * @param tcid
	 * @param addQueries
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 */
	public static List<PropertyGroupEntity> fetchParameterGroup(SearchParameters param, String keyword, String storeEn, String tcid, String[] addQueries ) throws UnsupportedEncodingException{
		if (param == null || param.propsSelected == null || param.propsSelected.isEmpty()){
			return Collections.emptyList();
		}
		keyword = keywordFilter(keyword);
		CollectionConfig cc = SearchClient.get().getConfigByCollection(SearchConstants.productCollection);
		CollectionConfig.Strategy conf = cc.getStratagy();
		String qString = conf.getString("keywordSearch");
		String qf = conf.getString("keywordSearchqf");
		
		List<Callable<PropertyGroupEntity>> groupSolrFetcher = new ArrayList<Callable<PropertyGroupEntity>>();
		//先去掉价格, 如果存在价格。
		if (SearchConstants.hasPrice(param.hasPrice())){
			String startPrice = param.startPrice;
			String endPrice = param.endPrice;
			param.startPrice = null;
			param.endPrice = null;
			//------
			SolrQuery solrQuery = param.generateSolrQuery();
			solrQuery.setStartRows(0, 0);
			solrQuery.addFacetFieldSortByIndex("price_norm_i");
			//storeid& tcid & keyword & addQueries
			fillParameters(solrQuery, keyword, tcid, storeEn, addQueries);
			final String url = solrQuery.format(qString, qf);
			//加回去
			param.startPrice = startPrice;
			param.endPrice = endPrice;
			groupSolrFetcher.add(new Callable<PropertyGroupEntity>() {

				@Override
				public PropertyGroupEntity call() throws Exception {
					String result =  SearchClient.get().executeSearch(SearchConstants.productCollection, url);
					JsonObject resultJson = new JsonParser().parse(result).getAsJsonObject();
					JsonArray priceFacet = resultJson.getAsJsonObject(SearchConstants.FACET_COUNTS).getAsJsonObject(SearchConstants.FACET_FIELDS).getAsJsonArray("price_norm_i");
					PropertyGroupEntity priceRow = new PropertyGroupEntity("价格", new ArrayList<PropertyShowEntity>());
					SearchResultParser.addPrice(priceRow, priceFacet);
					return priceRow;
				}
				
			});
		}
		
		//然后考察属性
		List<String> pKeys = new ArrayList<String>(param.propsSelected.keySet());
		for(String pKey : pKeys){
			List<String> removedValues = param.propsSelected.remove(pKey); //除去自己
			SolrQuery solrQuery = param.generateSolrQuery();
			solrQuery.setStartRows(0, 0);
			solrQuery.addFacetField("property_ids");
			solrQuery.addRawParameter("facet.prefix=" + pKey + "_");
			//storeid& tcid & keyword & addQueries
			fillParameters(solrQuery, keyword, tcid, storeEn, addQueries);
			final String url = solrQuery.format(qString, qf);
			param.propsSelected.put(pKey, removedValues); //加回来
			groupSolrFetcher.add(new Callable<PropertyGroupEntity>() {
				@Override
				public PropertyGroupEntity call() throws Exception {
					String result =  SearchClient.get().executeSearch(SearchConstants.productCollection, url);
					JsonObject resultJson = new JsonParser().parse(result).getAsJsonObject();
					JsonArray pKeyValues = resultJson.getAsJsonObject(SearchConstants.FACET_COUNTS).getAsJsonObject(SearchConstants.FACET_FIELDS).getAsJsonArray("property_ids");
					if (pKeyValues.size() == 0){
						return null;
					}else{
						//查出 中文信息
						List<PropertyShowEntity> propertyShowList = new ArrayList<PropertyShowEntity>();
						for(int i = 0 ; i < pKeyValues.size(); i+=2){
							String[] pKeyValue = pKeyValues.get(i).getAsString().split("_");
							String[] detail ;
							if (       pKeyValue[0].equals(SearchConstants.BRAND_ABBR)){
								detail = CacheTools.get().getBrandDetailById(Integer.parseInt(pKeyValue[1]));
							}else if ( pKeyValue[0].equals(SearchConstants.PLACE_ABBR)){
								detail = CacheTools.get().getPlaceDetailById(Integer.parseInt(pKeyValue[1]));
							}else{
								detail = CacheTools.get().getPropDetailById(Integer.parseInt(pKeyValue[1]));
							}
							if (detail != null){
								PropertyShowEntity pse = new PropertyShowEntity(new String[]{pKeyValues.get(i).getAsString(), detail[1], detail[2]}, pKeyValues.get(i+1).getAsInt());
								propertyShowList.add(pse);
							}
						}
						PropertyGroupEntity group = new PropertyGroupEntity(propertyShowList.get(0).getPropertyValue()[1], propertyShowList);
						return group;
					}
				}
			});

		}
		//并发执行获取结果。
		try {
			List<PropertyGroupEntity> groups = ConcurrentExecutor.execute(groupSolrFetcher);
			return groups;
		} catch (Exception e) {
			log.error(e, "parallel execution error in (fetch parameter groups)", "");
			return Collections.emptyList();
		} 
	}

	/**
	 * 一个spu 只返回异图的sku商品， 
	 * @param spuId
	 * @param skuId 该sku排在第一位
	 * @return
	 * @throws java.io.UnsupportedEncodingException
	 * @throws BaseException
	 */
	public static List<ProductEntity> searchLimitedSpuBySpuId(String spuId, String skuId) throws UnsupportedEncodingException, BaseException{
		String skuSearch = skuSearch(null, null, null, null, null, new String[]{"state:1", "spu_id:" + spuId}, 0, 64, SearchConstants.FIELDLIST_CORESITE);
		return SearchResultParser.collapseSkusFromResult(new JsonParser().parse(skuSearch).getAsJsonObject(), skuId);
	}
	
}
