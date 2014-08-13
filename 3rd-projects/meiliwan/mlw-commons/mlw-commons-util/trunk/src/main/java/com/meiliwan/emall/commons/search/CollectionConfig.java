package com.meiliwan.emall.commons.search;

import java.math.BigDecimal;
import java.util.Map.Entry;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.zookeeper.KeeperException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.commons.constant.GlobalNames;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ZKClient;
import com.meiliwan.emall.commons.util.ZKClient.StringValueWatcher;

/**
 * 搜索相关的服务的统一配置
 * @author lgn-mop
 *
 */
public class CollectionConfig {

//	private final static Logger logger =LoggerFactory.getLogger(CollectionConfig.class);
	static MLWLogger logger = MLWLoggerFactory.getLogger(CollectionConfig.class);
	private static class ABTest{

		double[] boundaries ;
		Strategy[] stratagies ;
		
		public ABTest(JsonObject ab){
			boundaries = new double[ab.entrySet().size()];
			stratagies = new Strategy[ab.entrySet().size()];
			int i = 0;
			double ratioSum = 0;
			for(Entry<String, JsonElement> kv: ab.entrySet()){
				//
				JsonObject values = kv.getValue().getAsJsonObject();
				ratioSum += values.get(GlobalNames.ABTEST_RATIO).getAsDouble();
				JsonObject paraMap = values.getAsJsonObject(GlobalNames.ABTEST_PARAM);
                String abbr = values.get(GlobalNames.ABTEST_ABBR).getAsString();
				stratagies[i] = new Strategy(kv.getKey(), abbr, paraMap);
				boundaries[i] = values.get("ratio").getAsDouble();
                i+=1;
			}
			for(int j = 0 ; j < boundaries.length; j++){
				boundaries[j] /= ratioSum;
			}
			boundaries[boundaries.length-1] += 1.0;
		}
		
		public Strategy randomStratagy(){
			double d = RandomUtils.nextDouble();
			for(int i = 0 ; i < boundaries.length -1; i++){
				if (d < boundaries[i]){
					return stratagies[i];
				}
			}
			return stratagies[stratagies.length-1];
		}
		
		public String toString(){
			JsonObject jo = new JsonObject();
			for(Strategy s : stratagies){
				jo.addProperty(s.stratagyName, s.simpleFormat);
			}
			return jo.toString();
		}
		
	}
	
	
	/**
	 * 相当于一个配置文件
	 * @author lgn-mop
	 *
	 */
	public static class Strategy{
		
		String stratagyName;
		String simpleFormat;
		JsonObject parameters ;
		
		public Strategy(String name, String simpleFormat,JsonObject para){
			this.stratagyName = name;
			this.simpleFormat = simpleFormat;
			this.parameters = para;
		}

		
		public String getString(String key, String defaultValue){
			String v = parameters.get(key).getAsString();
			if (v == null){
				return defaultValue;
			}
			return v;
		}
		
		public String getString(String key){
			return getString(key, null);
		}
		
		public BigDecimal getNumber(String key, BigDecimal defaultValue){
			BigDecimal d = parameters.get(key).getAsBigDecimal();
			if (d == null)
				return defaultValue;
			return d;
		}
		
		public BigDecimal getNumber(String key){
			return getNumber(key, null);
		}
		
		
		/**
		 * default false
		 * @return
		 */
		public boolean getBoolean(String key){
			return parameters.get(key).getAsBoolean();
		}
		
		public Boolean getBoolean(String key, Boolean defaultValue){
			Boolean b = parameters.get(key).getAsBoolean();
			if (b == null){
				return defaultValue;
			}
			return b;
		}
		
		
		public String toString(){
//			JsonObject jo = new JsonObject();
			return new Gson().toJson(this);
//			jo.add("name", this.stratagyName);
//			jo.put("short", this.simpleFormat);
//			jo.put("para", parameters);
//			return jo.toJSONString();
		}
		
		
	}
	
	private volatile JsonObject basicConfig;
	private ABTest abTest;
	private StringValueWatcher watcher;
	
	public CollectionConfig(final String zkFullPath) throws KeeperException, InterruptedException{
		String content = ZKClient.get().getStringData(zkFullPath);
		if (content != null && !content.isEmpty()){
			load(content);
			watcher = new StringValueWatcher() {
				public void valueChaned(String l) {
					load(l);				
				}
			};
			ZKClient.get().watchStrValueNode(zkFullPath, watcher);
		}else{
			logger.warn("PATH NOT FOUND ON ZK", "", "");
//			logger.warn("PATH NOT FOUND ON ZK");
		}

	}
	
	
	
	
	public boolean load(String json){
		JsonParser jp = new JsonParser();
		JsonObject jo = jp.parse(json).getAsJsonObject();
		JsonObject ab = jo.get(GlobalNames.ABTEST_STRING).getAsJsonObject();

		basicConfig = jo;
		
		ABTest abTmp = new ABTest(ab);
		abTest = abTmp;
		
		return false;
	}
	
	/**
	 * produce <b>Random</b> strategy
	 * @return
	 */
	public Strategy getStratagy(){
		return abTest.randomStratagy();
	}
	
	/**
	 * 获取的是JsonElement， 如果是字符串类型，别用toString
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Object getValue(String key, Object defaultValue){
		Object value = basicConfig.get(key);
		if (value == null){
			return defaultValue;
		}else{
			return value;
		}
	}
	
	public String toString(){
		JsonObject result = new JsonObject();
		for(Entry<String, JsonElement> kv : basicConfig.entrySet()){
			result.add(kv.getKey(), kv.getValue());
		}

		result.addProperty(GlobalNames.ABTEST_STRING, abTest.toString());
		
		return result.toString();
	}
	
	StringValueWatcher getWatcher(){
		return watcher;
	}
	
	
	public JsonObject getConfiguration(){
		return this.basicConfig;
	}
	
}
