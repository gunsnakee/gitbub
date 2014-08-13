package com.meiliwan.emall.commons.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ZKClient.StringValueWatcher;

/**
 * <p>本类为单例，一旦初始化就没法改变，如果你需要指定自己的配置路径，在使用本类之前先调用BaseConfig.setConfigFrom()</p>
 * <p>异常code 类似HTTP，4XX系列是业务导致的，例如路径、格式不对；5XX是系统的问题，例如网络问题</p>
 * <p>watch的功能是带监控的，一旦改变节点的值，就能捕捉到。使用的时候还是需要知道配置路径
 * <p>本类的方法<b><i> 只能</i> </b>watch .xml或者.properties文件，如果不想用本类提供的接口， 可以自己写：</p>
 *  	ZKClient.get().watchStrValueNode( watcher ) 
 *  	watcher需要自己实现一个 new StringValueWatcher() 内部类
 * <p>如果配置参数是永远不变，也可以不用watch直接获取节点的字符串内容:</p>
 * 		ZKClient.get().getStringData( node )
 *  
 * @author guoning.liang@opi-corp.com
 *
 */
public class ConfigOnZk {

//	private static final Logger log = LoggerFactory.getLogger(ConfigOnZk.class);
	static final MLWLogger log = MLWLoggerFactory.getLogger(ConfigOnZk.class);
	private Map<String, Configuration> totalConfigs = new ConcurrentHashMap<String, Configuration>();

	private static ConfigOnZk instance = null;

	private class NetworkException extends BaseException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 5650781394821165981L;

		public NetworkException(String logMsg, Throwable cause) {
			super("503", logMsg, cause);
		}
		
		public NetworkException(String logMsg) {
			super("503", logMsg);
		}
	}
	
	private ConfigOnZk(){
		
	}
	
	/**
	 * <p>本类为单例，一旦调用就没法改变，如果你需要指定自己的zk配置路径，在使用本类之前先调用BaseConfig.setConfigFrom()</p>
	 * @return
	 */
	public static ConfigOnZk getInstance(){
		if (instance == null){
			synchronized (ConfigOnZk.class) {
				if (instance == null){
					instance = new ConfigOnZk();
				}
				
			}
		}
		return instance;
	}

	
	private void loadConfig(final String pathFromMlwconf, String content, String configType ){
		if (configType.equals(".xml")){
			Configuration conf = new XMLConfiguration();
			try {
				((XMLConfiguration)conf).load(new ByteArrayInputStream(content.getBytes()), "UTF-8");
			} catch (ConfigurationException e) {
				log.error(e, "parse config : " + pathFromMlwconf + " exception!", " ");
//				log.error("parse config : " + pathFromMlwconf + " exception!", e);
//				throw new BaseException("404", "parse config : " + pathFromMlwconf + " exception!");
			}
			totalConfigs.put(pathFromMlwconf, conf);
			ZKClient.get().watchStrValueNode(GlobalNames.ZKCONFIGS_CHROOT + "/" + pathFromMlwconf, 
					new StringValueWatcher() {
				public void valueChaned(String l) {
					Configuration conf = new XMLConfiguration();
					try {
						((XMLConfiguration)conf).load(new ByteArrayInputStream(l.getBytes()), "UTF-8");
					} catch (ConfigurationException e) {
						log.error(e, "parse config : " + pathFromMlwconf + " exception!", " ");
//						log.error("parse config : " + pathFromMlwconf + " exception! on value change!", e);
//						throw new BaseRuntimeException("404", "parse config : " + pathFromMlwconf +  " exception! on value change!");
					}
					totalConfigs.put(pathFromMlwconf, conf);
				}
			});
		}else if (configType.endsWith(".properties")){
			Configuration conf = new PropertiesConfiguration();
			try {
				((PropertiesConfiguration)conf).load(new ByteArrayInputStream(content.getBytes()), "UTF-8");
			} catch (ConfigurationException e) {
				log.error(e, "parse config : " + pathFromMlwconf + " exception!", " ");
//				log.error("parse config : " + pathFromMlwconf + " exception!", e);
//				throw new BaseException("404", "parse config : " + pathFromMlwconf + " exception!");
			}
			totalConfigs.put(pathFromMlwconf, conf);
			ZKClient.get().watchStrValueNode(GlobalNames.ZKCONFIGS_CHROOT + "/" + pathFromMlwconf, 
					new StringValueWatcher() {
				public void valueChaned(String l) {
					Configuration conf = new PropertiesConfiguration();
					try {
						((PropertiesConfiguration)conf).load(new ByteArrayInputStream(l.getBytes()), "UTF-8");
					} catch (ConfigurationException e) {
						log.error(e, "parse config : " + pathFromMlwconf + " exception!", " ");
//						log.error("parse config : " + pathFromMlwconf + " exception! on value change!", e);
//						throw new BaseRuntimeException("404", "parse config : " + pathFromMlwconf +  " exception! on value change!");

					}
					totalConfigs.put(pathFromMlwconf, conf);
				}
			});
		}else{
//			throw new BaseException("404", " path must be XML or properties");
		}

	}

	/**
	 * 注册监控一个节点，并拿回节点的配置信息
	 * @param pathFromMlwconf, 从mlwconf下开始，也就是不带/mlwconf，必须是.xml或者.properties结尾
	 * @return
	 * @throws BaseException 出现路径错误，格式错误；或者网络链接错误
	 */
	private Configuration getConfigAndWatch(String pathFromMlwconf) throws BaseException{
		if (pathFromMlwconf.startsWith("/")){
			pathFromMlwconf = pathFromMlwconf.replaceFirst("/+", "");
		}
		String absPath = GlobalNames.ZKCONFIGS_CHROOT + "/" + pathFromMlwconf;
		try{
			String content = ZKClient.get().getStringData(absPath);
			if (content == null){
				throw new BaseRuntimeException("404", " path of :" + absPath + " not found");
			}else{
				int lastDotIndex = pathFromMlwconf.lastIndexOf(".");
				//				final String configName = pathFromMlwconf.substring(0, lastDotIndex);
				final String configType = pathFromMlwconf.substring( lastDotIndex );
				if (totalConfigs.containsKey(pathFromMlwconf)){
					return totalConfigs.get(pathFromMlwconf);
				}else{
					this.loadConfig(pathFromMlwconf, content, configType);
					return totalConfigs.get(pathFromMlwconf);
				}
			}
		}catch (KeeperException e) {
			log.error(e, " visit zookeeper KeeperException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new NetworkException( " visit zookeeper KeeperException!",e);
		}catch (InterruptedException e) {
			log.error(e, " visit zookeeper InterruptedException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new NetworkException(" visit zookeeper InterruptedException!");
		}
	}

	/**
	 *注册监控一个节点，并拿回节点的配置信息, 直接取配置里的Key value
	 * @param pathFromMlwconf, 从mlwconf下开始，也就是不带/mlwconf，必须是.xml或者.properties结尾
	 * @param key
	 * @return
	 * @throws BaseException
	 */
	public String getValue(String pathFromMlwconf, String key) throws BaseException{
		Configuration conf = getConfigAndWatch(pathFromMlwconf);
		return conf.getString(key);
	}
	
	/**
	 *注册监控一个节点，并拿回节点的配置信息, 直接取配置里的Key value 默认值
	 * @param pathFromMlwconf, 从mlwconf下开始，也就是不带/mlwconf，必须是.xml或者.properties结尾
	 * @param key
	 * @return
	 * @throws BaseException
	 */
	public String getValue(String pathFromMlwconf, String key, String defaultValue) throws BaseException{
		Configuration conf = getConfigAndWatch(pathFromMlwconf);
		return conf.getString(key, defaultValue);
	}
	
	/**
	 *注册监控一个节点，并拿回节点的配置信息, 直接取配置里的Key value 默认值
	 * @param pathFromMlwconf, 从mlwconf下开始，也就是不带/mlwconf，必须是.xml或者.properties结尾
	 * @param key
	 * @return
	 * @throws BaseException
	 */
	@SuppressWarnings("rawtypes")
	public int getElemSize(String pathFromMlwconf, String selector) throws BaseException{
		Configuration config = getConfigAndWatch(pathFromMlwconf);
		Object element = config.getProperty(selector);
		if(element == null){
			return 0;
		}
		
		if (element instanceof Collection) {
			Collection collec = (Collection) config.getProperty(selector);
			return collec.size();
		}

		return 1;
	}
	
	public Map<String, String> getMap(String pathFromMlwconf) throws BaseException{
		Configuration config = getConfigAndWatch(pathFromMlwconf);
		Iterator<String> itr = config.getKeys();
		Map<String, String> keyValue = new HashMap<String, String>();
		
		while(itr.hasNext()){
			String name = itr.next();
			String[] strarr = config.getStringArray(name);
			String val = null;
			if(strarr != null && strarr.length>1){
				val = Arrays.toString(strarr);
			}else {
				val = config.getString(name);
			}
			keyValue.put(name, val);
		}
		return keyValue;
	}
	
	
	/**
	 * 获取用于初始化需要，而不进行watch的配置，
	 * 如果反复调用，请用{@link #getConfigAndWatch(String)}
	 * @param pathFromMlwconf, 从mlwconf下开始，也就是不带/mlwconf，必须是.xml或者.properties结尾
	 * @return
	 * @throws BaseException 出现路径错误，格式错误；或者网络链接错误
	 * 
	 */
	@Deprecated
	public Configuration getConfig(String pathFromMlwconf )throws BaseException{
		if (pathFromMlwconf.startsWith("/")){
			pathFromMlwconf = pathFromMlwconf.replaceFirst("/+", "");
		}
		String absPath = GlobalNames.ZKCONFIGS_CHROOT + "/" + pathFromMlwconf;
		try{
			String content = ZKClient.get().getStringData(absPath);
			if (content == null){
				throw new BaseException("404", " path of :" + absPath + " not found");
			}else{
				int lastDotIndex = pathFromMlwconf.lastIndexOf(".");
				String configType = pathFromMlwconf.substring( lastDotIndex );
				Configuration conf = null;
				if (configType.equals(".xml")){
					conf = new PropertiesConfiguration();
					try {
						((XMLConfiguration)conf).load(new ByteArrayInputStream(content.getBytes()), "UTF-8");
					} catch (ConfigurationException e) {
						throw new BaseException("404", "parse config : " + pathFromMlwconf +  " exception! on value change!");
					}
				}else if (configType.equals(".properties")) {
					conf = new PropertiesConfiguration();
					try {
						((PropertiesConfiguration)conf).load(new ByteArrayInputStream(content.getBytes()), "UTF-8");
					} catch (ConfigurationException e) {
						throw new NetworkException( "parse config : " + pathFromMlwconf +  " exception! on value change!");
					}
				}else{
					throw new NetworkException( "parse config : " + pathFromMlwconf +  " exception! on value change!");
				}
				return conf;
			}
		}catch (KeeperException e) {
			log.error(e, " visit zookeeper KeeperException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new NetworkException( " visit zookeeper KeeperException!",e);
		}catch (InterruptedException e) {
			log.error(e, " visit zookeeper InterruptedException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new NetworkException(" visit zookeeper InterruptedException!");
		}
	}
	

	
	
	/**
	 * 注册监控一个目录下的一批配置文件，目录不包括mlwconf不能递归深入
	 * 配置文件必须是.xml或者.properties
	 * 
	 * @param pathFromMlwconf
	 * @return 被识别解析的配置文件名
	 * @throws BaseException 
	 */
	@Deprecated
	public List<String> listConfigsAndWatch(String pathFromMlwconf) throws BaseException{
		if (pathFromMlwconf.startsWith("/")){
			pathFromMlwconf = pathFromMlwconf.replaceFirst("^/+", "").replaceFirst("/+$", "");
		}
		String absPath = GlobalNames.ZKCONFIGS_CHROOT + "/" + pathFromMlwconf;
		List<String> confsOfNode = new ArrayList<String>();
		try{
			String content = ZKClient.get().getStringData(absPath); //需要catch到网络错误
			if (content == null){
				throw new BaseException("404", " path of :" + absPath + " not found");
			}else{
				List<String> childrenList = ZKClient.get().getChildren(absPath);
				for(String child : childrenList){
					try{
						String pathToWatch = absPath.substring(GlobalNames.ZKCONFIGS_CHROOT.length());
						Configuration conf = getConfigAndWatch(pathToWatch + "/" + child);
						if (conf != null)
							confsOfNode.add(pathFromMlwconf + "/" +child);
					}catch (BaseException e) {
						log.warn( pathFromMlwconf + "/" + child + " configging error  :( discarding!", " ", "");
//						log.warn( pathFromMlwconf + "/" + child + " configging error  :( discarding!" , e);
					}
				}
			}
			return confsOfNode;
		}catch (KeeperException e) {
			log.error(e, " visit zookeeper KeeperException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new BaseException("503", " visit zookeeper KeeperException!",e);
		}catch (InterruptedException e) {
			log.error(e, " visit zookeeper InterruptedException!", " ");
//			log.error("visit zookeeper exception!", e);
			throw new BaseException("503", " visit zookeeper InterruptedException!", e);
		}
	}

	
	

	public static void main(String[] args) throws InterruptedException, BaseException {
//		Configuration conf = ConfigOnZk.get().watchNodeConfig("search/aaa.xml");
//		System.out.println();
		List<String> watchConfigsOfDir = ConfigOnZk.getInstance().listConfigsAndWatch("search");
		System.out.println(watchConfigsOfDir);
		Configuration conf = ConfigOnZk.getInstance().getConfigAndWatch("search/config.properties");
		System.out.println(conf.getString("zk.quorum"));
		while(true){
			Thread.sleep(3000);
			conf = ConfigOnZk.getInstance().getConfigAndWatch("search/config.properties");
			System.out.println(conf.getString("zk.quorum"));
		}
		
	}
}
