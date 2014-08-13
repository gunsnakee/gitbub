package com.meiliwan.recommend.handler;


import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import com.meiliwan.recommend.common.BaseRecommender;
import com.meiliwan.recommend.core.RecommendModule;
import com.meiliwan.recommend.core.RecommendProcessor;
import com.meiliwan.search.util.HttpResponseUtil;
import com.meiliwan.search.util.UrlPathUtil;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.NettyHttpRequest;

public class AssemblerRefreshHandler implements Handler{

	RecommendProcessor processor;

	public AssemblerRefreshHandler(RecommendProcessor processor){
		this.processor = processor;
	}

	@Override
	public String getPath() {
		// TODO Auto-generated method stub
		return "refresh";
	}

	@Override
	public void handle(NettyHttpRequest req, DefaultHttpResponse resp) {

		String collName = UrlPathUtil.getCollName(req.path());
		RecommendModule recommend = (RecommendModule) processor.getModuleByName(collName);
		if (recommend == null){
			HttpResponseUtil.setHttpResponseWithStatus(resp, HttpResponseStatus.BAD_REQUEST);
		}else{
			boolean ok;
			ok = recommend.refreshResultAssembler();
			((BaseRecommender)recommend.getRecommender()).expireCache();
			if (ok){
				HttpResponseUtil.setHttpResponseOkReturn(resp, "refresh finish!");
			}else{
				HttpResponseUtil.setHttpResponseOkReturn(resp, "refresh error?");
			}


		}
	}

}
