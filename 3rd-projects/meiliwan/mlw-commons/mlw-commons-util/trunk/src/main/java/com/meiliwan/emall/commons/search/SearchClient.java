package com.meiliwan.emall.commons.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.zookeeper.KeeperException;
//import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.search.CollectionConfig.Strategy;
import com.meiliwan.emall.commons.util.HttpClientUtil;
import com.meiliwan.emall.commons.util.ZKClient;
import com.meiliwan.emall.commons.util.ZKClient.ChildrenWatcher;


/**
 * solr的客户端，livenode监控用心跳，索引状态实时watch zookeeper。
 * @author lgn-mop
 *
 */
public class SearchClient {
//	private final static Logger log = LoggerFactory.getLogger(SearchClient.class);
	static MLWLogger log = MLWLoggerFactory.getLogger(SearchClient.class);
	public final static String CSEARCH = GlobalNames.SEARCH_CHROOT +"/csearch"; 
	public final static String REAL_INDEX = "indexName";
	public final static int LIVENODE_CHECK_INTERVAL = 2000;
	public final static int NUM_RETRY = 2;
	//watch 在线节点，用内部类
	//	private ChildrenWatcher liveNodeWatcher
	// live nodes
	private volatile List<String>  liveNodes = new ArrayList<String>();
	private Thread liveNodeThread ;
	private JsonObject clusterState;
	//watch 有多少在服务的collection，用内部类
	//	private ChildrenWatcher collectionsWatcher;
	//每个collection的配置项改变情况
	private Map<String, CollectionConfig> collectionConfigs = new ConcurrentHashMap<String, CollectionConfig>();


	private HttpClient client = HttpClientUtil.get().getHttpClient();

	private static SearchClient instance;


	private SearchClient() {
		preWatch();
		watch();
	}

	public static String parseLiveNode(String zkNode) {
		return "http://" + zkNode.replaceFirst("_", "/");
	}


	private void preWatch(){
		try{
			JsonParser jp = new JsonParser();
			//-----livenodes fetch
			List<String> liveChildren = ZKClient.get().getChildren(GlobalNames.SOLR_CHROOT + "/live_nodes");
			for(String liveNode : liveChildren){
				liveNodes.add(parseLiveNode(liveNode));
			}
			String clusterstatejson = ZKClient.get().getStringData(GlobalNames.SOLR_CHROOT + "/clusterstate.json");
			this.clusterState = jp.parse(clusterstatejson).getAsJsonObject();
			//----serving collections fetch
			List<String> collections = ZKClient.get().getChildren(CSEARCH);
			for(String collection : collections){
				CollectionConfig configAndWatch = new CollectionConfig(CSEARCH + "/" + collection); 
				collectionConfigs.put(collection, configAndWatch);
			}

		}catch (Exception e) {
//			log.error("get information on preWatch error!", e);
			log.error(e, "get information on preWatch error!", "");
			throw new BaseRuntimeException("search-500", e);
		}

	}

	private void watch() {
		//watch live nodes
		liveNodeThread = new Thread(new Runnable() {
			JsonParser jp = new JsonParser();
			@Override
			public void run() {
				while(true){
					try {
						Thread.sleep(LIVENODE_CHECK_INTERVAL);
					} catch (InterruptedException e) {
					}
					try {
						List<String> tmpLive = ZKClient.get().getChildren(GlobalNames.SOLR_CHROOT + "/live_nodes");
						ArrayList<String> swap = new ArrayList<String>();
						if (tmpLive != null && !tmpLive.isEmpty()){
							for(int i = 0; i < tmpLive.size(); i++){
								swap.add(parseLiveNode(tmpLive.get(i)));
							}
						}
						liveNodes = swap;
						String clusterstatejson = ZKClient.get().getStringData(GlobalNames.SOLR_CHROOT + "/clusterstate.json");
						clusterState = jp.parse(clusterstatejson).getAsJsonObject();
					} catch (InterruptedException e) {
//						log.error("get live node InterruptedException", e);
						log.error(e, "get live node InterruptedException", "");
					} catch (KeeperException e) {
//						log.error("get live node KeeperException", e);
						log.error(e, "get live node InterruptedException", "");
					}

				}

			}
		}, "liveNodeThread");
		liveNodeThread.setDaemon(true);
		liveNodeThread.start();
		//		ZKClient.get().watchChildren(GlobalNames.SOLR_CHROOT + "/live_nodes", new ChildrenWatcher() {
		//
		//			public void nodeRemoved(String node) {
		//				liveNodes.remove(parseLiveNode(node));
		//			}
		//
		//			public void nodeAdded(String node) {
		//				liveNodes.remove(parseLiveNode(node));
		//				liveNodes.add(parseLiveNode(node));
		//			}
		//		});
		//watch collections
		ZKClient.get().watchChildren(CSEARCH, new ChildrenWatcher() {

			@Override
			public void nodeAdded(String node) {

				try {
					CollectionConfig configAndWatch = collectionConfigs.get(node);
					if (configAndWatch == null){
						configAndWatch = new CollectionConfig(CSEARCH + "/" + node); 
						collectionConfigs.put(node, configAndWatch);
					}else{
						//已经在preWatch的时候初始化了
					}
				} catch (KeeperException e) {
					log.error(e, "node adding, init CollectionConfig KeeperException", "");
//					log.error("node adding, init CollectionConfig KeeperException", e);
				} catch (InterruptedException e) {
					log.error(e, "node adding, init CollectionConfig InterruptedException", "");
//					log.error("node adding, init CollectionConfig InterruptedException", e);
				}
			}

			@Override
			public void nodeRemoved(String node) {
				CollectionConfig remove = collectionConfigs.remove(node);
				ZKClient.get().destoryWatch(remove.getWatcher());
			}
		});


	}

	public static SearchClient get(){
		if (instance == null){
			synchronized (SearchClient.class) {
				if (instance == null){
					instance = new SearchClient();
				}
			}
		}
		return instance;
	}

	/**
	 * 根据collection名获取HttpSolrServer, 随机获取Live节点
	 * @param collection
	 * @return
	 * @throws NullPointerException
	 */
	@Deprecated
	public HttpSolrServer getSolrServerByCollection(String collection) throws BaseException{
		String nodeURL = getSolrAddressByCollection(collection);
		return new HttpSolrServer(nodeURL, client);

	}

	/**
	 * 根据collection名获取config
	 * @param collection
	 * @return
	 * @throws NullPointerException
	 */
	public CollectionConfig getConfigByCollection(String collection){
		CollectionConfig collconf = collectionConfigs.get(collection);
		if (collconf == null)
			return null;
		return collconf;
	}

	public Strategy randomStrategyByCollection(String collection){
		CollectionConfig collconf = collectionConfigs.get(collection);
		if (collconf == null)
			return null;
		return collconf.getStratagy();
	}


	/**
	 * 根据collection名获取Solr的地址, 获取leader，保证一致；
	 * 到collection为止
	 * @param collection
	 * @return
	 * @throws BaseException 
	 * @throws NullPointerException
	 */
	public String getSolrAddressByCollection(String collection) throws BaseException{
		CollectionConfig conf = this.getConfigByCollection(collection);
		if (conf == null){
			throw new BaseException("search-400", "collection not found!");
		}
		if (liveNodes.isEmpty()){
			throw new BaseException("search-401", "no service node found!");
		}
		String collectionFull = conf.getConfiguration().get(REAL_INDEX).getAsString();
//		String leaderUrl = ClusterStateUtil.getLeaderFromObject(this.clusterState, collectionFull);
//		if (liveNodes.contains(leaderUrl)){
//			return leaderUrl + "/" + collectionFull;
//		}else{
//			int randomNodeIdx = RandomUtils.nextInt(liveNodes.size());
//			String nodeURL = liveNodes.get(randomNodeIdx) + "/" + collectionFull;
//			return nodeURL;
//		}
		int randomNodeIdx = RandomUtils.nextInt(liveNodes.size());
		return getCollectionUrl(liveNodes.get(randomNodeIdx), collectionFull);
	}
	
	public List<String> getShuffledSolrAddresses(String collection) throws BaseException{
		CollectionConfig conf = this.getConfigByCollection(collection);
		if (conf == null){
			throw new BaseException("search-400", "collection not found!");
		}
		if (liveNodes.isEmpty()){
			throw new BaseException("search-401", "no service node found!");
		}
		String collectionFull = conf.getConfiguration().get(REAL_INDEX).getAsString();
		List<String> result = new ArrayList<String>(liveNodes.size());
		for(String liveNodeStr : liveNodes){
			result.add(getCollectionUrl(liveNodeStr, collectionFull));
		}
		Collections.shuffle(result);
		return result;
	}
	

	private String getCollectionUrl(String url, String collectionFull){
		String abbr = SolrUtils.extractAbbrFromClient(url);
		return url + "/" + collectionFull + "." + abbr;
	}
	
	public List<String> getLiveNodes(){
		return ImmutableList.copyOf(liveNodes);
	}

	@Deprecated
	public void forceRefreshLiveNodes() throws InterruptedException, KeeperException{
		List<String> liveChildren = ZKClient.get().getChildren(GlobalNames.SOLR_CHROOT + "/live_nodes");
		for(String liveNode : liveChildren){
			liveNodes.remove(parseLiveNode(liveNode));
			liveNodes.add(parseLiveNode(liveNode));
		}
	}

	public String getCollectionStatus(){
		return collectionConfigs.toString();
	}

	/**
	 * 从/select 开始， 必须是encode过的URL余下部分
	 * @throws BaseException 
	 */
	public String executeSearch(String collection, String parameters) throws BaseException{
		List<String> addresses = getShuffledSolrAddresses(collection);
		int j = 0;
		for(int i = 0; i < NUM_RETRY; i++, j++){
			if (j >= addresses.size()){
				j = 0;
			}
			String url = addresses.get(j) + parameters;
			HttpGet get = new HttpGet(url);
			get.getParams().setIntParameter(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_SO_TIMEOUT, 3000);
			get.getParams().setIntParameter(org.apache.solr.client.solrj.impl.HttpClientUtil.PROP_CONNECTION_TIMEOUT, 2000);
			try {
				return  HttpClientUtil.get().getHttpClient().execute(get, new BasicResponseHandler());
			} catch (ClientProtocolException e) {
//				log.warn(" request : " + url + " error", e);
				log.error(e, " request : " + url + " ClientProtocolException", "");
			} catch (IOException e) {
				log.error(e, " request : " + url + " IOException", "");
//				log.warn(" request : " + url + " error", e);
			}
		}
		throw new BaseException("search-500", "try " + NUM_RETRY + " times failed");
	}
	
	
}
