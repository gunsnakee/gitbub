package com.meiliwan.search.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.zookeeper.KeeperException;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.plugin.zk.ZKClient.ChildrenWatcher;
import com.meiliwan.emall.commons.plugin.zk.ZKClient.StringValueWatcher;
import com.meiliwan.emall.commons.util.BaseConfig;
import com.meiliwan.search.util.Strings;
//import com.meiliwan.search.util.ZKClient;
//import com.meiliwan.search.util.ZKClient.ChildrenWatcher;
//import com.meiliwan.search.util.ZKClient.StringValueWatcher;
import com.meiliwan.search.util.io.DiretoryDownloader;

/**
 * 更新同名的collection的solr路径的办法: 修改zk路径下的节点值
 * 添加新collection的方法：添加zk路径下的节点值(注意不能添加一个空内容的节点，必须符合配置格式)
 * 
 * @author lgn-mop
 * 
 */
public abstract class RequestProcessor extends Thread {

	// static ESLogger log = Loggers.getLogger(RequestProcessor.class);
	static MLWLogger log = MLWLoggerFactory.getLogger(RequestProcessor.class);
	protected volatile BlockingQueue<ProcessorModule> unregistered;
	protected volatile Map<String, ProcessorModule> modules; // core
	protected volatile Map<String, StringValueWatcher> collWatchers; // watch
																		// Constants.ZK_SEARCH_PATH,
																		// 与core同步

	protected Class<? extends ProcessorModule> clz;
	protected String zkServicePath; // /search/xxx
	protected String serviceAddress;
	protected String zkServiceAlivePath;

	protected long deleteWatchInteval = 30 * 1000;
	protected long reNewWatchInteval = -1;

	protected ServiceWatcher serviceWatcher;

	protected class CollectionWatcher implements StringValueWatcher {
		String serveName;
		String realColl;

		public CollectionWatcher(String servrName, String realColl) {
			this.serveName = servrName;
			this.realColl = realColl;
		}

		/**
		 * 如果indexName没变就不换
		 */
		public void valueChaned(String newValue) {
			try {
				// if the 'indexName' not found. there's no way to go into
				// processing step
				// String newRealCollection =
				// Strings.extractRealCollectionFromJson(newValue);
				JSONObject newConfigJson = JSONObject.fromObject(newValue);
				String newRealCollection = newConfigJson.getString("indexName");
				String updateSource = newConfigJson.getString("update.source");

				if (realColl.equals(newRealCollection)) { // using
															// realColl.equals,
															// DO NOT use
															// newRealCollection.equals
					modules.get(serveName).loadConfig(newValue);
				} else {
					if (canBeMountedOnToServer(newRealCollection, updateSource)) {
						int status = swapToNew(serveName, newValue);
						this.realColl = newRealCollection; // change name
						// log.info("swap index {} in local path , status: {}",
						// newRealCollection, status);
						log.info(String.format(
								"swap index  %s in local path , status: %s",
								newRealCollection, status), newValue, "");
					} else {
						log.warn(String.format(
								"not found index %s in local path ",
								newRealCollection), newValue, "");
						// log.info("not found index {} in local path",
						// newRealCollection);
					}
				}
			} catch (Throwable e) {
				// log.error("zk value change swap to new caure exception", e);
				log.error(e, "zk value change swap to new caure exception",
						newValue);
			}
		}

		public String toString() {
			return String.format("\"%s =>%s\"", this.serveName, this.realColl);
		}
	}

	/**
	 * 监控ZK在服务的有哪些collection, 增加、删除节点时触发
	 * 
	 * @author lgn-mop
	 * 
	 */
	protected class ServiceWatcher extends ChildrenWatcher {

		public ServiceWatcher() {

		}

		/**
		 * !make sure you didn't add a node with empty content
		 */
		public void nodeAdded(String node) {
			CollectionWatcher collWatcher = (CollectionWatcher) collWatchers
					.get(node);
			if (collWatcher == null) {
				// register a watcher
				String newRealCollection = null;
				String newConfig = null;
				String updateSource = "fs";
				try {
					newConfig = ZKClient.get().getStringData(
							zkServicePath + "/" + node);
					JSONObject newConfigJson = JSONObject.fromObject(newConfig);
					newRealCollection = newConfigJson.getString("indexName");
					updateSource = newConfigJson.getString("update.source");
				} catch (KeeperException e1) {
					log.warn(" KeeperException cause in nodeAdded! ", node, "");
					// log.info(" KeeperException cause in nodeAdded! ", e1);
				} catch (InterruptedException e1) {
					log.warn(" InterruptedException cause in nodeAdded! ",
							node, "");
					// log.info(" InterruptedException cause in nodeAdded! ",
					// e1);
				}

				try {
					if (!canBeMountedOnToServer(newRealCollection, updateSource)) {
						log.warn(String.format(
								"not found index %s in local path ",
								newRealCollection), "", "");
						// log.info("not found index {} in local path",
						// newRealCollection);
						return;
					}
				} catch (IOException e1) {
					log.error(e1, "KeeperException cause in nodeAdded!", "");
					// e1.printStackTrace();
				}

				collWatcher = new CollectionWatcher(node, newRealCollection);
				try {
					ZKClient.get().watchStrValueNode(
							zkServicePath + "/" + node, collWatcher);
					collWatchers.put(collWatcher.serveName, collWatcher);
					int status = swapToNew(node, newConfig);
					// register Ephemeral
					// ZKClient.get().registerEphemeralNode(zkServicePath + "/"
					// + node, serviceAddress);
					// write some information
					// ZKClient.get().setData(zkServicePath + "/" + node +"/"+
					// serviceAddress, "".getBytes());
					// log.info("found index {} in local path , status: {}",
					// collWatcher.serveName, status);
					log.info(String.format(
							"found index  %s in local path , status: %s",
							collWatcher.serveName, status), "", "");
				} catch (KeeperException e) {
					// log.error("KeeperException ", e);
					log.error(e, "KeeperException when addnode", "");
				} catch (InterruptedException e) {
					// log.error("InterruptedException ", e);
					log.error(e, "InterruptedException when addnode", "");
				} catch (Exception e) {
					// log.error(" swapToNew Exception ", e);
					log.error(e, "error when added", "");
				}
			} else {
				// ;
			}

		}

		@Override
		public void nodeRemoved(String node) {
			CollectionWatcher deleteColl = (CollectionWatcher) collWatchers
					.remove(node);
			if (deleteColl != null) {
				// destroy watch and mark it as unregistered, 显式调用
				ZKClient.get().destoryWatch(deleteColl);
				ProcessorModule toUnload = modules.remove(deleteColl.serveName);
				unregistered.add(toUnload);
				// log.info("  index {} unloaded !", deleteColl.serveName);
				log.info(String.format(" index %s unloaded !",
						deleteColl.serveName), node, "");
			}

		}
	}

	private boolean canBeMountedOnToServer(String realCollectionName,
			String updateSource) throws IOException {
		List<String> legalList = getAllAvailableCollections();
		// check whether found
		if (legalList.contains(realCollectionName)) {
			// log.info("!found index {} in local", realCollectionName);
			log.info(
					String.format("not found %s on local", realCollectionName),
					"", "");
			return true;
		} else { // not found, start downloading
			log.info("not found on local start to download index", "", "");
			// log.info("not found in local, start to download index {}",
			// realCollectionName);
			return DiretoryDownloader.getDownloader(
					updateSource == null ? "fs" : updateSource).download(
					zkServicePath + "/" + realCollectionName,
					Constants.INDEX_DIR + "/" + realCollectionName);
			// return whether success or not?
		}
	}

	public RequestProcessor(Class<? extends ProcessorModule> clz,
			String zkServicePath, String serverAddress) throws Exception {
		this.serviceAddress = serverAddress;
		this.clz = clz;
		this.zkServicePath = zkServicePath;
		this.zkServiceAlivePath = zkServicePath + "_alive";
		ZKClient.get().createIfNotExist(zkServicePath);
		ZKClient.get().createIfNotExist(zkServiceAlivePath);
		this.init(checkCollections());

		Thread deletionGuardian = new Thread(this, "deletion Guardian ");
		deletionGuardian.setDaemon(true);
		deletionGuardian.start();

		ZKClient.get().registerEphemeralNode(zkServiceAlivePath, serverAddress);
		// write some information
		ZKClient.get().setData(zkServiceAlivePath + "/" + serverAddress,
				new byte[] {});
	}

	/**
	 * return physical collections with full names in 'dataDir', only return
	 * servingNames set
	 * 
	 * @return
	 */
	protected List<String> checkCollections() {
		File[] dirs = new File(Constants.INDEX_DIR).listFiles();
		ArrayList<String> tmp = new ArrayList<String>();
		if (dirs != null) {
			HashSet<String> toServe = new HashSet<String>();
			for (File f : dirs) {
				if (!f.isDirectory()
						|| toServe.contains(Strings.extractCollectionName(f
								.getName())))
					continue;
				tmp.add(f.getName());
				toServe.add(f.getName());
			}

		}
		return tmp;
	}

	/**
	 * return all physical collections
	 * 
	 * @return
	 */
	protected List<String> getAllAvailableCollections() {
		File[] dirs = new File(Constants.INDEX_DIR).listFiles();
		ArrayList<String> tmp = new ArrayList<String>();
		if (dirs != null) {
			for (File f : dirs) {
				if (!f.isDirectory())
					continue;
				tmp.add(f.getName());
			}
		}
		return tmp;
	}

	/**
	 * use reflection for initialization
	 * 
	 * @param constructorParameter
	 * @return
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 */
	private ProcessorModule initNewModule(String constructorParameter)
			throws Exception {
		Constructor<? extends ProcessorModule> constructor = (Constructor<? extends ProcessorModule>) clz
				.getConstructor(String.class);
		// String serviceCollection =
		// Strings.extractRealCollectionFromJson(constructorParameter);
		return constructor.newInstance(constructorParameter);

	}

	/**
	 * including swap and add. 1 = switch, 2 = add
	 * 
	 * @param JSON
	 * @return
	 * @throws Exception
	 */
	public int swapToNew(String collName, String JSON) throws Exception {
		String realColl = Strings.extractRealCollectionFromJson(JSON);
		// String collName = Strings.extractCollectionName(realColl);
		if (modules.containsKey(collName)) { // switch
			ProcessorModule oldModule = modules.get(collName);
			ProcessorModule newModule = initNewModule(JSON);
			modules.put(collName, newModule);
			unregistered.add(oldModule); // be careful not to overwrite old
			return 1;

		} else { // add
			ProcessorModule newModule = initNewModule(JSON);
			modules.put(collName, newModule);
			return 2;
		}
	}

	/**
	 * default mount all available collection
	 * 
	 * @param colls
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @throws java.io.IOException
	 */
	protected void init(List<String> colls) throws KeeperException,
			InterruptedException, IOException {
		unregistered = new LinkedBlockingQueue<ProcessorModule>(3);
		modules = new ConcurrentHashMap<String, ProcessorModule>(3);
		collWatchers = new ConcurrentHashMap<String, StringValueWatcher>(3);
		serviceWatcher = new ServiceWatcher();
		ZKClient.get().watchChildren(zkServicePath, this.serviceWatcher);
	}

	public Set<String> indexSet() {
		return this.modules.keySet();
	}

	public ProcessorModule getModuleByName(String name) {
		return this.modules.get(name);
	}

	@Deprecated
	protected void reNew() throws Exception {
		for (Entry<String, ProcessorModule> entry : modules.entrySet()) {
			String realName = entry.getValue().realCollection;
			ProcessorModule oldModule = entry.getValue();
			ProcessorModule newModule = initNewModule(realName);
			modules.put(entry.getKey(), newModule);
			Thread.sleep(2 * 1000);
			oldModule.close(); // not destroy
		}
	}

	public String getZkServicePath() {
		return this.zkServicePath;
	}

	public void run() {

		reNewWatchInteval = Integer.parseInt(BaseConfig.getValue(
				"renew.interval", "-1")) * 60 * 1000;
		long reNewStart = System.currentTimeMillis();
		long deleteStart = System.currentTimeMillis();
		while (true) {
			long current = System.currentTimeMillis();
			// compute time interval
			long interval = current - reNewStart;
			if (reNewWatchInteval > 0 && interval > reNewWatchInteval) {
				try {
					this.reNew();
				} catch (Exception e) {
					// log.error("renew cause exception!!!!!", e);
					log.error(e, "renew cause exception!!!!!", "");
				}
				reNewStart = current;
			}

			interval = current - deleteStart;
			if (interval > deleteWatchInteval) {
				int unsize = unregistered.size();
				for (int i = 0; i < unsize; i++) {

					ProcessorModule toRemove = unregistered.poll();
					try {
						// !!!do NOT close() before destroy()!
						toRemove.destroy();
						log.info(String.format(
								"garbage : connection of %s destroyed ",
								toRemove.getRealCollection()), "", "");
						// log.info("garbage : connection of {}  destroyed !",
						// toRemove.getRealCollection());
					} catch (Exception e) {
						log.error(e, "garbage destroy cause exception!!!!!", "");
						// log.error("garbage destroy cause exception!!!!!", e);
					}
				}

				deleteStart = current;
				// unregistered.clear();
			}
			// -----------thread sleep
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				;
			}
		}
	}

	public String showStatus() {
		JSONObject jo = new JSONObject();
		jo.put("deleteInterval", deleteWatchInteval);
		jo.put("renewInterval", reNewWatchInteval);
		JSONObject moduleJson = new JSONObject();
		for (Entry<String, ProcessorModule> runningModule : modules.entrySet()) {
			moduleJson.put(runningModule.getKey(),
					JSONObject.fromObject(runningModule.getValue().toString()));
		}
		jo.put("serving", moduleJson);
		jo.put("zkWatches", this.collWatchers.toString()); // auto converting
															// String to
															// Jsonobject
		jo.put("garbages", this.unregistered.toString());// auto converting
															// String to
															// Jsonobject

		return jo.toString();
	}

	public String toString() {
		return this.showStatus();
	}
}
