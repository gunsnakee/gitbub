package com.meiliwan.emall.search.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.imeiliwan.search.SearchConstants;
import com.meiliwan.emall.imeiliwan.search.SearchParameters;
import com.meiliwan.emall.imeiliwan.search.SearchResultParser;
import com.meiliwan.emall.imeiliwan.search.SearchParameters.SolrQuery;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyGroupEntity;
import com.meiliwan.emall.imeiliwan.search.vo.PropertyShowEntity;
import com.meiliwan.emall.search.common.SearchClient;

public class TestParameter {

	public static void main(String[] args) throws UnsupportedEncodingException, BaseException {
		SearchParameters sp = new SearchParameters(null, null, new String[]{"5_898,pl_6"});
		SolrQuery query = sp.generateSolrQuery();

		query.addFacetFieldSortByIndex("property_ids");
		query.setFieldList("id");
		query.setStartRows(0, 0);
		query.addFacetFieldSortByIndex("price_norm_i");
		String format = query.format("q=$0&defType=edismax", "contents^0.8 tags^3.0");
		System.out.println(URLDecoder.decode(format, "utf-8"));
		String executeSearch = SearchClient.get().executeSearch(SearchConstants.productCollection, format);
	
		JsonObject all = new JsonParser().parse(executeSearch).getAsJsonObject();
		
		PropertyGroupEntity pge = new PropertyGroupEntity("", new ArrayList<PropertyShowEntity>());
		List<PropertyGroupEntity> extractPropertyGroups = SearchResultParser.extractPropertyGroups(all, sp);
	
		System.out.println(pge);
		System.out.println("=============================");
		System.out.println(extractPropertyGroups);
	}

}
