package com.meiliwan.emall.search.text;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.imeiliwan.search.SearchExecutor;
import com.meiliwan.emall.imeiliwan.search.vo.ProductEntity;

public class TestSPUlimited {

	public static void main(String[] args) throws UnsupportedEncodingException, BaseException {
		List<ProductEntity> searchLimitedSpuBySpuId = SearchExecutor.searchLimitedSpuBySpuId("10239149", "10239658");
		System.out.println(searchLimitedSpuBySpuId);
	}

}
