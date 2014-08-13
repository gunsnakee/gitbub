package com.meiliwan.emall.commons.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
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
public class RedisSearchInfoUtil {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(RedisSearchInfoUtil.class);
	
	public final static String COMMON_PRO_MSG = "basePro";
	
	/**
	 * 商品改动信息
	 * @param proId
	 * @return
	 */
	public static boolean addSearchInfo(int... proIds){
		if(proIds == null || proIds.length <=0){
			return false;
		}
		
		RedisSearchInfo[] searchInfos = new RedisSearchInfo[proIds.length];
		int index = 0;
		for(int proId : proIds){
			RedisSearchInfo searchInfo = new RedisSearchInfo();
			searchInfo.setObjId(proId + "");
			searchInfo.setMsgType(COMMON_PRO_MSG);
			
			searchInfos[index++] = searchInfo;
		}
		return addSearchInfo(searchInfos);
	}
	
	
	
	public static boolean addSearchInfo(Collection<Integer> proIds){
		if(proIds == null || proIds.isEmpty()){
			return false;
		}
		
		RedisSearchInfo[] searchInfos = new RedisSearchInfo[proIds.size()];
		int index = 0;
		for(int proId : proIds){
			RedisSearchInfo searchInfo = new RedisSearchInfo();
			searchInfo.setObjId(proId + "");
			searchInfo.setMsgType(COMMON_PRO_MSG);
			
			searchInfos[index++] = searchInfo;
		}
		return addSearchInfo(searchInfos);
	}
	
	/**
	 * 
	 * @param objId
	 * @param msgType
	 * @return
	 */
	public static boolean addSearchInfo(String objId, String msgType){
		RedisSearchInfo searchInfo = new RedisSearchInfo();
		searchInfo.setObjId(objId);
		searchInfo.setMsgType(msgType);
		
		return addSearchInfo(searchInfo);
	}
	
	/**
	 * 
	 * @param searchInfo
	 * @return
	 */
	public static boolean addSearchInfo(RedisSearchInfo... searchInfos){
		if(searchInfos == null || searchInfos.length <= 0){
			return false;
		}
		
		try {
			String[] infoArr = new String[searchInfos.length];
			for(int i = 0; i < searchInfos.length; i++){
				infoArr[i] = searchInfos[i].toString();
			}
			return ShardJedisTool.getInstance().sadd(JedisKey.searchInfo, "", infoArr);
		} catch (JedisClientException e) {
			LOG.error(e, "modify search info fail:" + searchInfos, "");
		}
		
		return false;
	}
	
	/**
	 * 获取最近修改过的对象的id信息
	 * @return
	 */
	public static Set<Integer> getModifiedProIds(){
		Set<Integer> rsiSet = new HashSet<Integer>();
		try {
			Set<String> searchInfos = ShardJedisTool.getInstance().smembers(JedisKey.searchInfo, "");
			
			if(searchInfos != null && searchInfos.size() > 0){
				for(String infoStr : searchInfos){
					RedisSearchInfo rsInfo = new Gson().fromJson(infoStr, RedisSearchInfo.class);
					if(rsInfo != null && StringUtils.isNotBlank(rsInfo.getObjId())){
						rsiSet.add(Integer.parseInt(rsInfo.getObjId()));
					}
				}
			}
		} catch (JedisClientException e) {
			LOG.error(e, "getModifiedProIds for search info fail", "");
		}
		
		return rsiSet;
	}
	
	/**
	 * 
	 * @return
	 */
	public static Set<RedisSearchInfo> getSearchInfos(){
		Set<RedisSearchInfo> rsiSet = new HashSet<RedisSearchInfo>();
		try {
			Set<String> searchInfos = ShardJedisTool.getInstance().smembers(JedisKey.searchInfo, "");
			
			if(searchInfos != null && searchInfos.size() > 0){
				for(String infoStr : searchInfos){
					RedisSearchInfo rsInfo = new Gson().fromJson(infoStr, RedisSearchInfo.class);
					
					rsiSet.add(rsInfo);
				}
			}
		} catch (JedisClientException e) {
			LOG.error(e, "getSearchInfos for search info fail", "");
		}
		
		return rsiSet;
	}
	
	/**
	 * 
	 * @param objIds
	 * @return
	 */
	public static boolean removeSearchInfos(int... objIds){
		if(objIds == null || objIds.length <= 0){
			return false;
		}
		
		RedisSearchInfo[] searchInfos = new RedisSearchInfo[objIds.length];
		int index = 0;
		for(int proId : objIds){
			RedisSearchInfo searchInfo = new RedisSearchInfo();
			searchInfo.setObjId(proId + "");
			searchInfo.setMsgType(COMMON_PRO_MSG);
			
			searchInfos[index++] = searchInfo;
		}
		
		return removeSearchInfos(searchInfos);
	}
	
	
	public static boolean removeSearchInfos(Collection<Integer> objIds){
		if(objIds == null || objIds.isEmpty()){
			return false;
		}
		
		RedisSearchInfo[] searchInfos = new RedisSearchInfo[objIds.size()];
		int index = 0;
		for(int proId : objIds){
			RedisSearchInfo searchInfo = new RedisSearchInfo();
			searchInfo.setObjId(proId + "");
			searchInfo.setMsgType(COMMON_PRO_MSG);
			
			searchInfos[index++] = searchInfo;
		}
		
		return removeSearchInfos(searchInfos);
	}
	
	/**
	 * 
	 * @param searchInfo
	 * @return
	 */
	public static boolean removeSearchInfos(RedisSearchInfo... searchInfos){
		if(searchInfos == null || searchInfos.length <= 0){
			return false;
		}
		
		try {
			String[] infoArr = new String[searchInfos.length];
			for(int i = 0; i < searchInfos.length; i++){
				infoArr[i] = searchInfos[i].toString();
			}
			return ShardJedisTool.getInstance().srem(JedisKey.searchInfo, "", infoArr);
		} catch (JedisClientException e) {
			LOG.error(e, "remove search info fail:" + searchInfos, "");
		}
		
		return false;
	}
	
}
