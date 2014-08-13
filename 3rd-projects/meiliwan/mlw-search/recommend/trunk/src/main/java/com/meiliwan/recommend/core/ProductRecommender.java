package com.meiliwan.recommend.core;

import java.util.List;

import com.meiliwan.emall.imeiliwan.client.ProductSolrGetter;
import com.meiliwan.emall.imeiliwan.search.vo.ProductRecommendVO;
import com.meiliwan.recommend.common.BaseRecommender;
import com.meiliwan.recommend.common.ItemItemRelations;


/**
 * 商品详情页推荐
 * @author lgn-mop
 *
 */
public class ProductRecommender extends BaseRecommender{

	/*关系矩阵在内存，不需要缓存。*/
	public ProductRecommender(String dir)throws Exception {
		super(dir);
		
	}

	public void init(String dir)throws Exception {
		if (assembler == null){
			assembler = ProductSolrGetter.getInstance();
		}
		if (itemRelations == null){
			itemRelations = new ItemItemRelations(dir);
		}
	}
	
	public List<ProductRecommendVO> recommendRelatedItems(List<Integer> itemIds,
			int type, int topK) {
//		int catType = type / 100;
//		int sortType = (type % 100) / 10;
		int actionType = type % 10;
		return super.recommendRelatedItems(itemIds, actionType, topK);
	}

	@Override
	public void expireCache() {
		super.expireCache();
	}
	
	
	
	
}
