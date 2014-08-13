package com.meiliwan.search.job.attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


public class ProductActivityAggregator {
	private final static Map<String, String> actTypes = new HashMap<String, String>();
	public Map<String, String> typeAggregator;
    
	
	public ProductActivityAggregator(String proId){
		typeAggregator = new HashMap<String, String>();
	}
	
	public static Map<String, ProductActivityAggregator> aggregateActs(List<ProductActivity> productActivities, 
			Set<String> unparsedActTypes){
		parseActTypes(unparsedActTypes);
		
		Map<String, ProductActivityAggregator> results = new HashMap<String, ProductActivityAggregator>();
		
		Map<String, List<ProductActRelation>> sameProducts =  getSameProducts(productActivities);
		
		for(Entry<String, List<ProductActRelation>> sameProductsEntry : sameProducts.entrySet()){
			ProductActivityAggregator pActAggregator = new ProductActivityAggregator(sameProductsEntry.getKey());			
			
			for(ProductActRelation relation : sameProductsEntry.getValue()){
				String actType = relation.getActType();
				
				//只保留一个类型活动
				String actTypeParsed = actTypes.get(actType.toLowerCase().trim());
				
				if(!StringUtils.isEmpty(actTypeParsed)){
					relation.setActType(actTypeParsed);
					pActAggregator.typeAggregator.put(actTypeParsed, relation.toStringWithoutActType());
				}
			}
			
			results.put(sameProductsEntry.getKey(), pActAggregator);
			
		}
		return results;
	}
	
	private static void parseActTypes(Set<String> unparsedActTypes) {
		for(String unparsedActType : unparsedActTypes){			
			String actTypeParsed = actTypes.get(unparsedActType.toLowerCase().trim());
			if(actTypeParsed == null){
				actTypes.put(unparsedActType.toLowerCase().trim(), unparsedActType.toLowerCase().trim());
			}
		}
	}

	private static Map<String, List<ProductActRelation>> getSameProducts(List<ProductActivity> productActivities){
		Map<String, List<ProductActRelation>> sameProducts = new HashMap<String, List<ProductActRelation>>();
		
		for(ProductActivity pActivity : productActivities){
			Integer proId = pActivity.getProId();
			
			List<ProductActRelation> tmp = sameProducts.get(proId + "");
			
			if(tmp == null){
				tmp = new ArrayList<ProductActRelation>();
				ProductActRelation relation = new ProductActRelation();
				relation.setActPrice(pActivity.getActPrice());
				relation.setDiscount(pActivity.getDiscount()+"");	
				relation.setActType(pActivity.getActType());
				
				tmp.add(relation);
				
				sameProducts.put(proId+"", tmp);
			}else{
				ProductActRelation relation = new ProductActRelation();
				relation.setActPrice(pActivity.getActPrice());
				relation.setDiscount(pActivity.getDiscount()+"");
				relation.setActType(pActivity.getActType());
				tmp.add(relation);
			}
		}
		
		return sameProducts;
	}

}
