package com.meiliwan.search.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 发送HTTP请求
 * @author lgn
 *
 */
public class HttpRequester {
	
	/**
	 * Get请求
	 * @param url 请求地址
	 * @param connTimeOut connection time out  (ms)
	 * @param soTimeOut time out of getting data (ms)
	 */
	public static String doHttpGet(String url, int connTimeOut, int soTimeOut) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, connTimeOut);// 连接超时时间3秒
		HttpConnectionParams.setSoTimeout(httpParams, soTimeOut);// 获取数据超时时间
		
		try {
			HttpGet httpget = new HttpGet( url);
			return httpclient.execute(httpget, new BasicResponseHandler());
		} catch (Exception e) {
			return null;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * Post <b>string content</b> to url using <b>StringEntity</b>
	 * @param url
	 * @param content
	 * @param connTimeOut
	 * @param soTimeOut
	 * @return
	 */
	public static String  doHttpPost(String url, String content, int connTimeOut, int soTimeOut) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams httpParams = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, connTimeOut);// 连接超时时间3秒
		HttpConnectionParams.setSoTimeout(httpParams, soTimeOut);// 获取数据超时时间

		try {
			HttpPost httppost = new HttpPost( url);

			StringEntity entity = new StringEntity(content, "UTF-8");
		    httppost.setEntity(entity);
			return httpclient.execute(httppost, new BasicResponseHandler());
		} catch (Exception e) {
			return null;
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
	}
}
