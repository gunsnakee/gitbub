package com.meiliwan.emall.imeiliwan.search;


import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 只是美丽湾商品搜索的常量， 有些旧的量没删除，价格区间facet的字段不对 还没改
 *  @Description TODO
 *	@author shanbo.liang
 */
public class SearchConstants {
	
	public final static String productCollection = "productx";
	
	public final static String PIVOT_ID_VALUE = "value";
    public final static String PIVOT_CHILDREN = "pivot";
    public final static String PIVOT_COUNT = "count";

    public final static String FACET_COUNTS = "facet_counts";
    public final static String FACET_FIELDS = "facet_fields";
    public final static String FACET_RANGES = "facet_ranges";

    public final static String PIVOTFIELDS = "second_category_id,third_category_id";
    public final static String CATEGORY_TREE_STRING = "category_tree_str";
    public final static String MLW_PRICE_FACET = "mlwprice_volume";
    public final static String BRAND_STRING = "brand_id";
    public final static String PLACE_STRING = "place_id";
	//----------------------

    public final static String FIELDLIST_CORESITE="id,default_image_uri,pro_name,short_name,adv_name,stock,sale_volume,comment_volume,store_ids,sp_ids,mlw_prices,sp_prices,market_prices,avg_score,state";
    public final static String FILEDLIST_COREPLACE="id,is_falls,falls_image_uri,pro_name,mlw_prices,sp_prices,love_volume,height_volume,comment_volume,store_ids,avg_score";
    public final static String FILEDLIST_MOBILESITE="id,default_image_uri,pro_name,short_name,short_name,adv_name,stock,sale_volume,comment_volume,store_ids,sp_ids,mlw_prices,market_prices,sp_prices,avg_score,place_id,love_volume";
    public final static String FILEDLIST_MOBILEPLACE="id,is_falls,falls_image_uri,default_image_uri,pro_name,market_prices,mlw_prices,sp_prices,love_volume,height_volume,comment_volume,store_ids,avg_score";
    
    public final static String fqCategoryTags = "cat3p";

    public final static String PROPERTY_STRING = "property_str_ids";
    public final static String PROPERTY_PREFIX_IN_CONDITION = "propid:";
    public final static String BRANDID_IN_CONDITION = "brand:";
    public final static String PRICE_IN_CONDITION = "price:";
    public final static String PLACE_IN_CONDITION = "place:";
	public final static String PRICE_FACET_STRING = "mlwprice_volume";
	
	
	public final static String BRAND_ABBR = "br";
	public final static String PLACE_ABBR = "pl";
	public final static String PRICE_ABBR = "pr";
	
	
	
	/**
	 * 
	 * @param hasBrandPricePlace
	 */
	public static int setGlobalBPP(int hasBrandPricePlace){
		int tmp = hasBrandPricePlace;
		tmp |= 0x10; // 不做price facet
//		tmp |= 0x1; //不做品牌		
		return tmp;
	}

	public static Map<String, String> checkPrices(String sp, String ep){
		Map<String, String> priceMap = new HashMap<String, String>(4);
		if (StringUtils.isBlank(sp) && StringUtils.isBlank(ep)){
			priceMap.put("sp", "");
			priceMap.put("ep", "");
		}else if (StringUtils.isBlank(sp) && StringUtils.isNotBlank(ep)){
			priceMap.put("sp", "");
			priceMap.put("ep", ep);
		}else if (StringUtils.isNotBlank(sp) && StringUtils.isBlank(ep)){
			priceMap.put("sp", sp);
			priceMap.put("ep", "");
		}else{
			int startPrice = Integer.parseInt(sp);
			int endPrice = Integer.parseInt(ep);
			if (startPrice > endPrice){
				priceMap.put("sp", ep);
				priceMap.put("ep", sp);
			}else{
				priceMap.put("sp", sp);
				priceMap.put("ep", ep);
			}
		}
		return priceMap;
	}
	
	public static boolean hasPrice(int hasBrandPricePlace){
		return (hasBrandPricePlace & 0x10 ) > 0;
	}
	
	public static boolean hasPlace(int hasBrandPricePlace){
		return (hasBrandPricePlace & 0x100 ) > 0;
	}
	
	public static boolean hasBrand(int hasBrandPricePlace){
		return (hasBrandPricePlace & 0x1 ) > 0;
	}
	
	/**
	 * 1,2 为销量降序，3/4为价格升序降序，5是进入国家馆时间，6：被喜爱数降序 7：被喜爱数升序
	 * @param sortId
	 * @return
	 */
	public static  String parseSort(String sortId){
//		String dtNow = DateUtil.getDateTimeStr(System.currentTimeMillis()).replace(" ", "T")+"Z";
//        String priceField = "add(mul(and(map(ms($0,sp_a1_start_time),0,9914803620000,1,0),map(ms(sp_a1_end_time,$0),0,9914803620000,1,0)),sp_a1_price),mul(sub(1,and(map(ms($0,sp_a1_start_time),0,9914803620000,1,0),map(ms(sp_a1_end_time,$0),0,9914803620000,1,0))),mlw_price))";

		if (!StringUtils.isEmpty(sortId) && "3".equals(sortId)) {
//            sortId = priceField.replace("$0", dtNow) + " asc";
            sortId = "sp_price asc";
        } else if (!StringUtils.isEmpty(sortId) && "4".equals(sortId)) {
//            sortId = priceField.replace("$0", dtNow) + " desc";
            sortId = "sp_price desc";
        } else if (!StringUtils.isEmpty(sortId) && "2".equals(sortId)) {
            sortId = "sale_volume desc";
        } else if (!StringUtils.isEmpty(sortId) && "1".equals(sortId)) {
            sortId = "sale_volume desc";
        } else if(!StringUtils.isEmpty(sortId) && "5".equals(sortId)) {
        	sortId = "nation_time desc";
        } else if(!StringUtils.isEmpty(sortId) && "6".equals(sortId)) {
        	sortId = "love_volume desc";
        } else if(!StringUtils.isEmpty(sortId) && "7".equals(sortId)) {
        	sortId = "love_volume asc";
        }else if(!StringUtils.isEmpty(sortId) && "8".equals(sortId)) {
        	sortId = "on_time desc";
        } else {
            sortId = "";
        }
        return sortId;
    }
}
