package com.meiliwan.emall.commons.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.apache.zookeeper.KeeperException;

//import com.google.common.collect.Multiset.Entry;
//import com.meiliwan.common.search.SolrSearcher;
/**
 * httpclient 4.1 . 
 * a global connection pool, <b>DO NOT shutdown the client</b> 
 * @author lgn-mop
 *
 */
public class HttpClientUtil {
	private static HttpClientUtil http = new HttpClientUtil();
	private HttpClient httpClient ;

//	private static class SolrSearcherHolder{
//		private static SolrSearcher instance = new SolrSearcher(http.httpClient); 
//	}
	/**
	 * be careful on the 
	 * <b>asynchronous</b> mode of SolrSearcher's initialization
	 * <p></p>  
	 * to connect solrcloud, use this 
	 * @return SolrSearcher
	 */
//	public SolrSearcher getSearcher(){
//		return SolrSearcherHolder.instance;
//	}
	

	
	private HttpClientUtil(){
		//TODO
		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_MAX_CONNECTIONS, 196);
		params.set(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_MAX_CONNECTIONS_PER_HOST, 64);
		params.set(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_FOLLOW_REDIRECTS, false);
		params.set(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_SO_TIMEOUT, 6000);
		params.set(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_CONNECTION_TIMEOUT, 3000);
		httpClient =  org.apache.solr.client.solrj.impl.HttpClientUtil.createClient(params);
		
		
	}
	
	public static HttpClientUtil get(){
		return http;
	}
	
	/**
	 * fetch httpClient for your usage,
	 * you can also use {@link #doGet(String)} or {@link #doPost(String, String)} in common 
	 * @return
	 */
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	public String doGet(String url) throws ClientProtocolException, IOException{
		HttpGet get = new HttpGet(url);
		return httpClient.execute(get, new BasicResponseHandler());
	}
	
	/**
	 * POST UTF-8 string content
	 * @param url
	 * @param content
	 * @return
	 * @throws java.io.IOException
	 */
	public String doPost(String url, String content) throws IOException{
		HttpPost post = new HttpPost(url);
		StringEntity entity = new StringEntity(content, "UTF-8");
	    post.setEntity(entity);
	    return httpClient.execute(post, new BasicResponseHandler());
	}

    public String doPost(String url) throws IOException{
        HttpPost post = new HttpPost(url);
        return httpClient.execute(post, new BasicResponseHandler());
    }
	
	/**
	 * for payment submit usage
	 * @param url
	 * @param param
	 * @return
	 */
	public static String getPostBody(String url, Map param){
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> nvps = new ArrayList <NameValuePair>();
        Iterator it = param.entrySet().iterator();
        String result = "";
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if(value==null){
            	value="";
            }
            nvps.add(new BasicNameValuePair(key.toString(), value.toString()));
        }
        
        try{
        	post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
        	result = HttpClientUtil.get().getHttpClient().execute(post, new BasicResponseHandler());
        }catch(IOException e){
        }
        return result;
        
	}
}
