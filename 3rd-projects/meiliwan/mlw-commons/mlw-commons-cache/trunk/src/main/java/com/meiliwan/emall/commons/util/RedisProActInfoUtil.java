package com.meiliwan.emall.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.bean.ProActInfo;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;



/**
 * 
 * @author lsf
 *
 */
public class RedisProActInfoUtil {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(RedisProActInfoUtil.class);
	
	/**
	 * 
	 * @param removeAfterFetch
	 * @return 返回最近修改过的活动Id数组
	 */
	public static int[] getUpdatedActIds(boolean removeAfterFetch){
		int[] actIdArr = new int[]{};
		try {
			Set<String> actIdStrs = ShardJedisTool.getInstance().smembers(JedisKey.search$actIds, "");
			if(actIdStrs != null && actIdStrs.size() > 0){
				actIdArr = new int[actIdStrs.size()];
				int index = 0;
				for(String actIdStr : actIdStrs){
					actIdArr[index] = Integer.valueOf(actIdStr);
					
					++index;
				}
			}
			
			if(removeAfterFetch){
				removeActIds();
			}
		} catch (JedisClientException e) {
			LOG.error(e, "call method RedisProActInfoUtil.getUpdatedActIds(" + removeAfterFetch + ") error", null);
		}
		
		return actIdArr;
	}
	
	/**
	 * 删除最近修改过的活动Id数组
	 */
	public static void removeActIds(){
		 try {
			ShardJedisTool.getInstance().del(JedisKey.search$actIds, "");
		} catch (JedisClientException e) {
			LOG.error(e, "call method RedisProActInfoUtil.removeActIds() error", null);
		}
	}
	
	/**
	 * 
	 * @param removeAfterFetch
	 * @return 返回最近从活动中删除过的商品对应的信息
	 */
	public static List<ProActInfo> getDelProActInfos(boolean removeAfterFetch){
		List<ProActInfo> proActInfos = new ArrayList<ProActInfo>();
		try {
			Set<String> proInfoStrs = ShardJedisTool.getInstance().smembers(JedisKey.search$delProInfos, "");
			if(proInfoStrs != null && proInfoStrs.size() > 0){
				for(String proInfo : proInfoStrs){
					ProActInfo actInfo = new Gson().fromJson(proInfo, ProActInfo.class);
					
					proActInfos.add(actInfo);
				}
			}
			
			if(removeAfterFetch){
				removeDelProActInfos();
			}
		} catch (JedisClientException e) {
			LOG.error(e, "call method RedisProActInfoUtil.getDelProActInfos(" + removeAfterFetch + ") error", null);
		}
		
		return proActInfos;
	}

	/**
	 * 删除最近从活动中删除过的商品对应的信息
	 */
	public static void removeDelProActInfos(){
		try {
			ShardJedisTool.getInstance().del(JedisKey.search$delProInfos, "");
		} catch (JedisClientException e) {
			LOG.error(e, "call method RedisProActInfoUtil.removeActIds() error", null);
		}
	}
}
