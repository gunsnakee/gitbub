package com.meiliwan.recommend.handler;

import java.io.IOException;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.recommend.core.RecommendProcessor;
import com.meiliwan.search.util.HttpResponseUtil;
import com.meiliwan.search.util.http.Handler;
import com.meiliwan.search.util.http.NettyHttpRequest;

public class RecommendHandler implements Handler {
	// static ESLogger log = Loggers.getLogger(RecommendHandler.class);
	static MLWLogger log = MLWLoggerFactory.getLogger(RecommendHandler.class);
	RecommendProcessor processor;

	public RecommendHandler(RecommendProcessor core) {
		this.processor = core;
	}

	public String getPath() {
		return "select";
	}

	public void handle(NettyHttpRequest req, DefaultHttpResponse resp) {

		String items = req.param("item");
		String type = req.param("type");
		String limit = req.param("size");

		String url = "http://www.meiliwan.com/recommend/product?item=" + items
				+ "&type=" + type + "&size=" + limit;
		try {
			String products = HttpClientUtil.getInstance().doGet(url);

			String resultStr = jsonListToResponseWithReqId(products, null, null);
			
			String jsonpCallback = req.param("callback");
			if (jsonpCallback != null){
				HttpResponseUtil.setHttpResponseOkReturn(resp, jsonpCallback + "("+ resultStr + ")");
			}
			else{
				HttpResponseUtil.setHttpResponseOkReturn(resp, resultStr);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String jsonListToResponseWithReqId(String products,
			String reqIdPrefix, String strategyJson) {
		if (StringUtils.isBlank(products)) {
			return "{\"status\":\"null\", \"numFound\":0,\"result\":null}";
		}
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\"");
		if (reqIdPrefix != null) {
			long k = new Random().nextInt(100000) + 100000;
			String r = reqIdPrefix + System.currentTimeMillis()
					+ String.valueOf(k).substring(1);
			builder.append(", \"reqId\":\"").append(r).append("\"");
		}
		if (strategyJson != null) {
			builder.append(",\"abtest\":").append(strategyJson);
		}
		builder.append(",\"result\":[");

		String proStr = null;
		String[] parts = products.split("[{");
		if (parts.length >= 2) {
			products = parts[1];
			parts = products.split("}]");
			if (parts.length >= 1) {
				proStr = parts[0];
			}
		}

		int num = 0;
		if (StringUtils.isBlank(proStr)) {
			builder.append("]}");
		} else {
			builder.append(proStr).append("]}");
			num = proStr.split("proId").length;
		}
		
		builder.append(", \"numFound\":").append(num);
		return builder.toString();
	}

}
