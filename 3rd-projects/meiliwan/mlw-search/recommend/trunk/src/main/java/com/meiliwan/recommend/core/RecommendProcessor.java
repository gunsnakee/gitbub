package com.meiliwan.recommend.core;


import com.meiliwan.search.common.RequestProcessor;

public class RecommendProcessor extends RequestProcessor{

	public RecommendProcessor(String address) throws Exception {
		super(RecommendModule.class, RecommendModule.getZkServicePath(), address);
	}

}
