package com.meiliwan.emall.search.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.KeeperException;

import com.google.common.collect.ImmutableList;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.plugin.zk.ZKClient.ChildrenWatcher;

/**
 * 同质架构的服务客户端。与searchclient有些许不同，主要是Livenode
 * @author lgn-mop
 *
 */
public class SearchComponentHttpClient {
	//	private final static Logger log = LoggerFactory.getLogger(SearchComponentHttpClient.class);
	static MLWLogger log = MLWLoggerFactory.getLogger(SearchComponentHttpClient.class);
	public final static int LIVENODE_CHECK_INTERVAL = 2000;

	Random random = new Random();


	private String clientName = null;
	private String clientZkPath = null;
	private String clientAliveZkPath = null;
	private boolean isServing = false;
	private volatile List<String>  liveNodes = new ArrayList<String>();


	private Map<String, CollectionConfig> collectionConfigs = new ConcurrentHashMap<String, CollectionConfig>();

	SearchComponentHttpClient(String clientName){
		this.clientName = clientName;
		this.clientZkPath = Constants.SEARCH_CHROOT  + "/" + clientName;
		this.clientAliveZkPath = Constants.SEARCH_CHROOT  + "/" + clientName + "_alive";
		openClient();
	}

	public void openClient(){
		try{
			List<String> collections = ZKClient.get().getChildren(clientZkPath);
			List<String> children = ZKClient.get().getChildren(clientAliveZkPath);
			if (collections.size()> 0 && children.size() > 0){
				this.isServing = true;
				preWatch();
				watch();
			}else{
				this.isServing = false;
			}
		}catch (KeeperException e){
			this.isServing = false;
		}catch (InterruptedException e) {
			this.isServing = false;
		}
	}

	public String parseLiveNode(String zkNode) {
		return "http://" + zkNode + "/" + clientName;
	}

	private void preWatch(){
		try{
			//-----livenodes fetch
			List<String> liveChildren = ZKClient.get().getChildren(clientAliveZkPath);
			for(String liveNode : liveChildren){
				liveNodes.add(parseLiveNode(liveNode));
			}

			//----serving collections fetch
			List<String> collections = ZKClient.get().getChildren(clientZkPath);
			for(String collection : collections){
				CollectionConfig configAndWatch = new CollectionConfig(clientZkPath + "/" + collection); 
				collectionConfigs.put(collection, configAndWatch);
			}

		}catch (Exception e) {
			//			log.error("get information on preWatch error!", e);
			log.error(e, "get information on preWatch error!", "");
			throw new BaseRuntimeException("commons-spellclient-500", e);
		}
	}
	private void watch(){

		ScheduledExecutor.submit(new Runnable() {

			@Override
			public void run() {
//				System.out.println("ha");
				try {
					List<String> tmpLive = ZKClient.get().getChildren(clientAliveZkPath);
					ArrayList<String> swap = new ArrayList<String>();
					if (tmpLive != null && !tmpLive.isEmpty()){
						for(int i = 0; i < tmpLive.size(); i++){
							swap.add(parseLiveNode(tmpLive.get(i)));
						}
					}
					liveNodes = swap;
				} catch (InterruptedException e) {
					log.error(e, "get live node InterruptedException", "");
					//						log.error("get live node InterruptedException", e);
				} catch (KeeperException e) {
					log.error(e, "get live node KeeperException", "");
					//						log.error("get live node KeeperException", e);
				}
			}


		},LIVENODE_CHECK_INTERVAL );

		ZKClient.get().watchChildren(clientZkPath, new ChildrenWatcher() {

			@Override
			public void nodeAdded(String node) {

				try {
					CollectionConfig configAndWatch = collectionConfigs.get(node);
					if (configAndWatch == null){
						configAndWatch = new CollectionConfig(clientZkPath + "/" + node); 
						collectionConfigs.put(node, configAndWatch);
					}else{
						//已经在preWatch的时候初始化了
					}
				} catch (KeeperException e) {
					log.error(e, "client node adding, init CollectionConfig KeeperException","");
					//					log.error("client node adding, init CollectionConfig KeeperException", e);
				} catch (InterruptedException e) {
					log.error(e, "client node adding, init CollectionConfig KeeperException","");
					//					log.error("client node adding, init CollectionConfig InterruptedException", e);
				} 
			}

			@Override
			public void nodeRemoved(String node) {
				CollectionConfig remove = collectionConfigs.remove(node);
				ZKClient.get().destoryWatch(remove.getWatcher());
			}
		});

	}


	public CollectionConfig getConfigByCollection(String collection){
		CollectionConfig conf = this.collectionConfigs.get(collection);
		if (conf == null){
			return null;
		}else{
			return conf;
		}
	}

	@Deprecated
	public String getSpellAddressByCollection(String collection) throws BaseException{
		CollectionConfig conf = this.collectionConfigs.get(collection);
		if (conf == null){
			throw new BaseException("spell-400", "collection not found!");
		}
		if (liveNodes.isEmpty()){
			throw new BaseException("spell-401", "no service node found!");
		}
		int randomNodeIdx = RandomUtils.nextInt(liveNodes.size());
		String nodeURL = liveNodes.get(randomNodeIdx) + "/" + collection;
		return nodeURL;

	}

	public String getRemoteAddressByCollection(String collection) throws BaseException{
		CollectionConfig conf = this.collectionConfigs.get(collection);
		if (conf == null){
			throw new BaseException("client-400", "collection not found!");
		}
		if (liveNodes.isEmpty()){
			throw new BaseException("client-401", "no service node found!");
		}
		int randomNodeIdx = RandomUtils.nextInt(liveNodes.size());
		String nodeURL = liveNodes.get(randomNodeIdx) + "/" + collection;
		return nodeURL;

	}


	public com.meiliwan.emall.search.common.CollectionConfig.Strategy randomStrategyByCollection(String collection){
		CollectionConfig collconf = collectionConfigs.get(collection);
		if (collconf == null)
			return null;
		return collconf.getStratagy();
	}

	public boolean isServing(){
		return isServing;
	}

	public List<String> getLiveNodes(){
		return ImmutableList.copyOf(liveNodes);
	}

	public String getCollectionStatus(){
		return collectionConfigs.toString();
	}

}
