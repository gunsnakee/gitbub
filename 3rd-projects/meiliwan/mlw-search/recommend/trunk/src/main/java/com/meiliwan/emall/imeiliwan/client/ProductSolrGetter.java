package com.meiliwan.emall.imeiliwan.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.imeiliwan.search.SearchExecutor;
import com.meiliwan.emall.imeiliwan.search.SearchParameters;
import com.meiliwan.emall.imeiliwan.search.SearchResultParser;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;
import com.meiliwan.emall.imeiliwan.search.vo.ProductSpuEntity;
import com.meiliwan.recommend.common.RecommendConstants;


/**
 * 整个进程中唯一一个缓存，多个推荐引擎都可以用它。
 * @author lgn
 *
 */
public class ProductSolrGetter implements IProductGetter{
//	private static Logger logger = LoggerFactory.getLogger(ProductSolrGetter.class);
	static MLWLogger logger = MLWLoggerFactory.getLogger(ProductSolrGetter.class);
	private Cache<Integer, ProductEntity> productCache = 
			CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS).build();
	
	private static IProductGetter instance = new ProductSolrGetter();
	
	public static IProductGetter getInstance(){
		return instance;
	}
	
	
	private Thread keeper ;
	
	
	private void cacheProductFromSolr(){
		try {
			int rand = RandomUtils.nextInt(999) + 1;

			SearchParameters param = new SearchParameters(null, null, null, null, null);
			JsonParser jp = new JsonParser();
			String result = SearchExecutor.skuSearch(param, null, null, null, "random_" + rand + " desc", new String[]{"state:1", "stock:[1 TO 9999999]"}, 0, 999, RecommendConstants.fl);
			JsonObject all = jp.parse(result).getAsJsonObject();
			List<ProductEntity> spuEntities = SearchResultParser.getProductSkuList(all, null);
			if(spuEntities.size() > 0)
				cacheProductFromList(spuEntities);
			
			
		} catch (BaseException e) {
			logger.error(e, "keep solr products BaseException", "");
		} catch (IOException e) {
			logger.error(e, "keep solr products IOException", "");
		}
	}
	
	
	private ProductSolrGetter(){
		keeper = new Thread(new Runnable() {
			public void run() {
				logger.info("start keeping cache with solr","","");
				while(true){
					cacheProductFromSolr();
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
					}
				}
				
			}
		}, "solrKeeper");
		keeper.setDaemon(true);
		keeper.start();
	}
	

	public boolean reloadAll() {
		productCache.invalidateAll();
		cacheProductFromSolr();
		return true;
	}

	private Collection<Integer> disjunction(Map<Integer, ?> allPresent, List<Integer> ids){
		List<Integer> needs = new ArrayList<Integer>();
		for(int i = 0 ; i < ids.size(); i++){
			if (!allPresent.containsKey(ids.get(i))){
				needs.add(ids.get(i));
			}
		}
		return needs;
	}
	

	public List<? extends ProductEntity> getProductEntities(List<Integer> ids)
			throws IOException {
		ImmutableMap<Integer, ProductEntity> allPresent = productCache.getAllPresent(ids);
		
		Collection<Integer> disjunction = this.disjunction(allPresent, ids);
		if (!disjunction.isEmpty()){
			try{

				SearchParameters param = new SearchParameters(null, null, null, null, null);
				JsonParser jp = new JsonParser();
				String result = SearchExecutor.skuSearch(param, null, null, null, null,new String[]{"state:1", "id:(" + StringUtils.join(disjunction, " OR") + ")"}, 0, 999, RecommendConstants.fl);
				JsonObject all = jp.parse(result).getAsJsonObject();
				List<ProductSpuEntity> spuEntities = SearchResultParser.getProductSpuList(all, null);
				if(spuEntities.size() > 0)
					cacheProductFromList(spuEntities);
				
				
			}catch (BaseException e) {
				logger.error(e, "connect to solr server error!!!!!!", "");
				return Collections.emptyList();
			}
		}
		List<ProductEntity> result = new ArrayList<ProductEntity>();
		for(Integer id : ids){
			result.add(productCache.getIfPresent(id));
		}
		return result;
	}




	public void cacheProductFromList(List<? extends ProductEntity> docs)
			throws IOException {
		for(ProductEntity pe : docs){
			productCache.put(new Integer(pe.getProId()), pe);
		}
	}

	public List<Integer> getProductIds() {
		List<Integer> tmp = new ArrayList<Integer>();
		for(Integer id  : this.productCache.asMap().keySet()){
			tmp.add(id.intValue());
		}
		return tmp;
	}

	public static void main(String[] args) throws InterruptedException {
		IProductGetter pp = ProductSolrGetter.getInstance();
		Thread.sleep(12222);
		System.out.println(pp.getProductIds().size());
		Thread.sleep(36222);
		System.out.println(pp.getProductIds().size());
	}
}
