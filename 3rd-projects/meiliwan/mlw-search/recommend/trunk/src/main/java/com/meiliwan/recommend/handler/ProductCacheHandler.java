package com.meiliwan.recommend.handler;

import java.util.List;

import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

import com.meiliwan.emall.imeiliwan.client.IProductGetter;
import com.meiliwan.emall.imeiliwan.client.ProductSolrGetter;
import com.meiliwan.search.util.HttpResponseUtil;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.NettyHttpRequest;

public class ProductCacheHandler implements Handler{

	@Override
	public String getPath() {
		return "cache";
	}

	@Override
	public void handle(NettyHttpRequest req, DefaultHttpResponse resp) {
		IProductGetter getter = ProductSolrGetter.getInstance();
		List<Integer> ids = getter.getProductIds();
		String content = "{\"size\":" + ids.size() + ",\"list\":" + ids.toString()+"}";
		HttpResponseUtil.setHttpResponseOkReturn(resp, content);
	}

}
