package com.meiliwan.recommend.common;


import java.util.List;

import com.meiliwan.emall.imeiliwan.search.vo.ProductRecommendVO;



public abstract class Recommender {
	public Recommender(String dir){
	}
	
	public abstract List<ProductRecommendVO> recommendWithUser(int userId, List<Integer> itemIds, int type, int topK);
	
	public abstract List<ProductRecommendVO> recommendRelatedItems(List<Integer> itemIds, int type, int topK) ;
}
