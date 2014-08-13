package com.meiliwan.search.suggest.core;

import com.meiliwan.search.common.RequestProcessor;

public class SuggestProcessor extends RequestProcessor{

	public SuggestProcessor(String address) throws Exception {
		super(SuggestModule.class, SuggestModule.getZkServicePath(), address);
	}

}
