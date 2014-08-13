package com.meiliwan.search.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.zookeeper.KeeperException;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.util.concurrent.ConcurrentExecutor;
import com.meiliwan.emall.search.common.SearchClient;
import com.meiliwan.emall.search.common.SolrUtils;


public class CollectionManager {

	private static HttpClient createClient(){
		HttpParams params = new BasicHttpParams();
		DefaultHttpClient.setDefaultHttpParams(params);
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 60000);
		HttpConnectionParams.setSocketBufferSize(params, 10240);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpClientParams.setRedirecting(params, true);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        
        ThreadSafeClientConnManager tsccm = new ThreadSafeClientConnManager();
		tsccm.setMaxTotal(200);
		tsccm.setDefaultMaxPerRoute(40);
		
		SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		
		return new DefaultHttpClient(tsccm, params);
	}

	
	public static HttpGet createHttpGet(final String url){
		HttpGet get = new HttpGet(url);
		return get;
	}
	
	public void createCollection(final String collectionFull, int replicas) throws InterruptedException, KeeperException, ExecutionException{
		final HttpClient client = createClient();
		List<String> liveSolr =SearchClient.get().getLiveNodes();
		ArrayList<Callable<String>> taskList = new ArrayList<Callable<String>>(liveSolr.size());
		final String createFormat = "%s/admin/collections?action=CREATE&name=%s.%s&collection.configName=%s&createNodeSet=%s&numShards=1&replicationFactor=1&maxShardsPerNode=1&wt=json";
		
		Collections.shuffle(liveSolr);
		List<String> toCreateNodes = new ArrayList<String>();
		int r = 0;
		for(int i = liveSolr.size()-1 ; i >=0 && r < replicas; i--,r++ ){
			toCreateNodes.add(liveSolr.get(i));
			liveSolr.remove(i);
		}
		
		
		for(final String node : toCreateNodes){
			taskList.add(new Callable<String>() {

				public String call() throws Exception {
					
					String address = SolrUtils.convertLiveToUrl(node);
					String abbr = SolrUtils.extractNumberFromLive(node);
					String finalUrl = String.format(createFormat, address, collectionFull, abbr, collectionFull, node);
					System.out.println(finalUrl);
					;
					return client.execute(createHttpGet(finalUrl), new BasicResponseHandler());
				}
			});
		}
		List<String> resps = ConcurrentExecutor.execute(taskList);
		for(String resp : resps){
			System.out.println(resp);
		}
		client.getConnectionManager().shutdown();
	}
	
	
	public void createCollection(final String collectionFull) throws InterruptedException, KeeperException, ExecutionException{
		createCollection(collectionFull, Integer.MAX_VALUE);
	}
			
	
	/**
	 * 读取Solr存活节点，迭代这些节点，拼成Solr服务地址，多线程调用HttpClient删除指定的collection
	 * */
	public void dropCollection(final String collectionFull) throws InterruptedException, KeeperException, ExecutionException{
		final HttpClient client = createClient();
		List<String> liveSolr = SearchClient.get().getLiveNodes();
		//Map<String, String> collectionCores = SearchClient.get().getCollectionCores(collectionFull);
		
		ArrayList<Callable<String>> taskList = new ArrayList<Callable<String>>(liveSolr.size());
		final String createFormat = "%s/admin/collections?action=DELETE&name=%s.%s&wt=json";		
		
		//有可能存在节点删不完的情况，有待改进
		for(final String node : liveSolr){
			//TODO
			final String address = SolrUtils.convertLiveToUrl(node);
			taskList.add(new Callable<String>() {

				public String call() throws Exception {
					String finalUrl = String.format(createFormat, address, collectionFull, SolrUtils.extractNumberFromLive(node));
					System.out.println(finalUrl);
					return client.execute(createHttpGet(finalUrl), new BasicResponseHandler());
				}
			});
		}

		List<String> resps = ConcurrentExecutor.execute(taskList);
		for(String resp : resps){
			System.out.println(resp);
		}
		client.getConnectionManager().shutdown();
	}
	
	
	public void truncateCollection(String collectionAbbr) throws BaseException, InterruptedException, ExecutionException{
		SearchClient.get().executeUpdate(collectionAbbr, "{\"delete\":{\"query\":\"*:*\"}}");
	}
	
	
	/**
	 * @param args
	 * @throws java.util.concurrent.ExecutionException
	 * @throws KeeperException 
	 * @throws InterruptedException 
	 * @throws BaseException 
	 */
	public static void main(String[] args) throws InterruptedException, KeeperException, ExecutionException, BaseException {
		if (args.length < 2){
			System.out.println("exe <action> <collectionName> ; truncateMethod use product");
			System.out.println("exe create <product_xxx> ");
			System.out.println("exe drop <product_xxx> ");
			System.out.println("exe truncate <product> ");
			System.exit(0);
		}
		CollectionManager manager = new CollectionManager();
		if (args[0].equalsIgnoreCase("create")){
			manager.createCollection(args[1]);
		}else if (args[0].equalsIgnoreCase("drop")) {
			manager.dropCollection(args[1]);
		}else if (args[0].equalsIgnoreCase("truncate")) {
			manager.truncateCollection(args[1]);
		}else{
			System.out.println("action must be create | drop | truncate");
		}
		System.exit(0);
	}

}
