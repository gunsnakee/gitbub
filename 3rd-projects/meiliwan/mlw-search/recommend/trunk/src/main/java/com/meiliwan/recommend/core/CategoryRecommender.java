package com.meiliwan.recommend.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


//import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.imeiliwan.client.ProductSolrGetter;
import com.meiliwan.emall.imeiliwan.search.SearchExecutor;
import com.meiliwan.emall.imeiliwan.search.SearchParameters;
import com.meiliwan.emall.imeiliwan.search.SearchResultParser;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductRecommendVO;
import com.meiliwan.recommend.common.BaseRecommender;
import com.meiliwan.recommend.common.ItemItemRelations;
import com.meiliwan.recommend.common.RecommendConstants;


/**
 * 列表页下的推荐，主要是几级类目页。
 * @author lgn-mop
 *
 */
public class CategoryRecommender extends BaseRecommender{

//	private static ESLogger logger = Loggers.getLogger(CategoryRecommender.class);
	//
	static MLWLogger logger = MLWLoggerFactory.getLogger(CategoryRecommender.class);
	private Cache<String, List<Integer>> resultIdsCache; //cache ranking of solr


	public void expireCache(){
		super.expireCache();
		resultIdsCache.invalidateAll();
		
	}
	
	public CategoryRecommender(String dir) throws Exception {
		super(dir);
		resultIdsCache = 
				CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build();
	}

	@Override
	protected void init(String dir) throws Exception {
		this.assembler = ProductSolrGetter.getInstance();
		this.itemRelations = new ItemItemRelations(dir);
	}

	@Override
	@Deprecated
	public List<ProductRecommendVO> recommendWithUser(int userId,
			List<Integer> itemIds, int type, int topK) {
		return null;
	}

	private String getSortStringByType(int sortType){
		if (sortType == 2){
			return "commet_volume desc";
		}
		return "sale_volume desc";

	}

	@Override
	/**
	 * 推荐某个
	 */
	public List<ProductRecommendVO> recommendRelatedItems(List<Integer> itemIds,
			int type, int topK) {
		int categoryId = itemIds.get(0);
		int catType = type / 100;
		int sortType = (type % 100) / 10;
		int actionType = type % 10; //from 0~
		try{
			if (catType == 2){
				return dfgSortProductBy(categoryId,  getSortStringByType(sortType), topK);
			}else if (catType >= 3 && catType <= 5){
				if (sortType > 0){
					return categorySortProductBy(categoryId, catType -2, getSortStringByType(sortType), topK);
				}else{
					if (actionType < 0 || actionType > 2)
						actionType = 1;
					return browseCategoryThenDo(itemIds, actionType, topK);
				}
			}else{ //category X?
				return Collections.emptyList();
			}

		}catch (Exception e) {
//			logger.error("fetch result error", e);
			logger.error(e, "fetch result error", "");
			return Collections.emptyList();
		}
	}
//
//	//TODO
//	private List<ProductEntity> thirdCategoryViewThenView(List<Integer> itemIds, int topK) throws IOException{
//		return super.recommendRelatedItems(itemIds, 0, topK);
//	}
//	//TODO
//	private List<ProductEntity> thirdCategoryViewThenBuy(List<Integer> itemIds, int topK) throws IOException{
//		return super.recommendRelatedItems(itemIds, 1, topK);
//	}

	private List<ProductRecommendVO> browseCategoryThenDo(List<Integer> itemIds, int actionType, int topK) throws IOException{
		return super.recommendRelatedItems(itemIds, actionType, topK);
	}

	/**
	 * 从solr中的X类目按XXX规则排序获取结果
	 * @param categoryId
	 * @param level
	 * @param sort
	 * @return
	 * @throws java.io.IOException
	 */
	private List<ProductRecommendVO> categorySortProductBy(int categoryId, int level, String sort, int topK) throws IOException {
		String cacheKey = level + "_" + categoryId + "_" + sort;
		List<Integer> topProductIds = resultIdsCache.getIfPresent(cacheKey);
		if (topProductIds != null){
			List<Integer> docIds = topProductIds.subList(0, Math.min(topK, topProductIds.size()));
			List<? extends ProductEntity> productEntities = assembler.getProductEntities(docIds);
			return ProductRecommendVO.convertEntitiesToVOs(productEntities);
		}else{
			try{

				String[] condition ;
				if (level == 1){
					condition = new String[]{"first_category_id:" + categoryId , "state:1" };
				}else if (level == 2){
					condition = new String[]{"second_category_id:" + categoryId , "state:1" };
				}else if (level == 3) {
					condition = new String[]{"third_category_id:" + categoryId , "state:1" };
				}else{
					return Collections.emptyList();
				}
				SearchParameters param = new SearchParameters(null, null, null, null, null);
				JsonParser jp = new JsonParser();
				String spuSearch = SearchExecutor.skuSearch(param, null, null, null, sort, condition, 0, Math.min(topK*2, 50), RecommendConstants.fl);
				JsonObject all = jp.parse(spuSearch).getAsJsonObject();
				List<ProductEntity> list = SearchResultParser.getProductSkuList(all, null);
				List<ProductRecommendVO> result = ProductRecommendVO.convertEntitiesToVOs(list);
				this.assembler.cacheProductFromList(list);
				List<Integer> docIds = new ArrayList<Integer>();
				for(ProductEntity pe : list){
					docIds.add(Integer.parseInt(pe.getProId()));
				}
				this.resultIdsCache.put(cacheKey, docIds);
				return result.subList(0, Math.min(topK, result.size()));
			}catch (Exception e) {
//				logger.error("get solr address error",e);
				logger.error(e, "get solr address error", "");
				return Collections.emptyList();
			}
		}

	}

	private List<ProductRecommendVO> dfgSortProductBy(int categoryId, String sort, int topK) throws IOException {
		String cacheKey = "place_" + categoryId + "_" + sort;
		List<Integer> topProductIds = resultIdsCache.getIfPresent(cacheKey);
		if (topProductIds != null){
			List<Integer> docIds = topProductIds.subList(0, Math.min(topK, topProductIds.size()));
			List<? extends ProductEntity> productEntities = assembler.getProductEntities(docIds);
			return ProductRecommendVO.convertEntitiesToVOs(productEntities);

		}else{
			try{
				
				SearchParameters param = new SearchParameters(null, categoryId+"", null, null, null);
				JsonParser jp = new JsonParser();
				String spuSearch = SearchExecutor.skuSearch(param, null, null, null, sort, new String[]{"state:1"}, 0, Math.min(topK*2, 50), RecommendConstants.fl);
				JsonObject all = jp.parse(spuSearch).getAsJsonObject();
				List<ProductEntity> list = SearchResultParser.getProductSkuList(all, null);
				List<ProductRecommendVO> result = ProductRecommendVO.convertEntitiesToVOs(list);
				this.assembler.cacheProductFromList(list);
				List<Integer> docIds = new ArrayList<Integer>();
				for(ProductEntity pe : list){
					docIds.add(Integer.parseInt(pe.getProId()));
				}
				this.resultIdsCache.put(cacheKey, docIds);
				return result.subList(0, Math.min(topK, result.size()));
			}catch (Exception e) {
//				logger.error("get solr address error",e);
				logger.error(e, "get solr address error", "");
				return Collections.emptyList();
			}

		}
	}


}
