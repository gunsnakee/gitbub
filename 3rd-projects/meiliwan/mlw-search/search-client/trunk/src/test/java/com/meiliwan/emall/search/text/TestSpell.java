package com.meiliwan.emall.search.text;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.search.common.SearchComponentHttpClient;
import com.meiliwan.emall.search.common.SearchComponents;
import com.meiliwan.emall.search.common.SearchComponents.Type;

public class TestSpell {

	public static void main(String[] args) throws BaseException, InterruptedException {
		SearchComponentHttpClient byComponentTypeName = SearchComponents.getByComponentTypeName(Type.spell);
		byComponentTypeName.getLiveNodes();
		Thread.sleep(10000);
		System.out.println("finished");
	}

}
