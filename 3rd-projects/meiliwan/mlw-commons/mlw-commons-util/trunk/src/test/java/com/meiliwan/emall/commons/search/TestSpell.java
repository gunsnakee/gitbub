package com.meiliwan.emall.commons.search;

//import com.alibaba.fastjson.JSONObject;
import com.meiliwan.emall.commons.exception.BaseException;

public class TestSpell {

	/**
	 * @param args
	 * @throws BaseException 
	 */
	public static void main(String[] args) throws BaseException {
		// TODO Auto-generated method stub
		//JSONObject a = SpellClient.get().getConfigByCollection("product");
		//System.out.println(a);
//		System.out.println(SpellClient.get().getSpellAddressByCollection("product"));
		
		SearchComponentHttpClient recommend = SearchComponents.getByComponentTypeName(SearchComponents.Type.recommend);
		System.out.println(recommend.getConfigByCollection("category"));
		
		try{
			SearchComponentHttpClient spell = SearchComponents.getByComponentTypeName(SearchComponents.Type.spell);
		}catch (BaseException e) {
			System.out.println(e.getMessage());
		}
		
	}

}
