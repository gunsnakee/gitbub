package com.meiliwan.emall.monitor.redis;

import java.io.Serializable;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.constant.Constant;

public class RequestCountRedis {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(RequestCountRedis.class);
	private ShardJedisTool redis;
	private static RequestCountRedis requestCountRedis = new RequestCountRedis();
	
	public static RequestCountRedis getInstance() {
		return requestCountRedis;
	}

	private RequestCountRedis() {
		try {
			redis = ShardJedisTool.getInstance();
		} catch (JedisClientException e) {
			logger.error(e, null, null);
		}
	}

	private boolean hset( Serializable id,String key,String value){
		try {
			return redis.hset(JedisKey.monitor$statistics,id,key, value);
		} catch (JedisClientException e) {
			logger.error(e, null, null);
		}
		return false;
	}
	
	private String hget(Serializable id,String key) {

		try {
			return redis.hget(JedisKey.monitor$statistics,id, key);
		} catch (JedisClientException e) {
			logger.error(e, null, null);
		}
		return null;
	}
	
	private boolean set( Serializable id, String value){
		try {
			return redis.set( JedisKey.monitor$statistics,id, value);
		} catch (JedisClientException e) {
			logger.error(e, null, null);
		}
		return false;
	}
	
	private String get(Serializable id) {

		try {
			return redis.get(JedisKey.monitor$statistics,id);
		} catch (JedisClientException e) {
			logger.error(e, null, null);
		}
		return null;
	}
	
	
	public boolean setRequestDaoCount(String value) {
		
		return hset(	Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.DAO, value);
	}
	
	public boolean setRequestIceCCount(String value) {
		return hset(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.ICEC, value);
	}
	
	public boolean setRequestIceSCount(String value) {
		return hset(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.ICES, value);
	}
	
	public boolean setRequestIntrCount(String value) {
		return hset(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.INTR, value);
	}
	
	//------get
	public String getRequestDaoCount() {
		return hget(	Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.DAO);
	}
	
	public String getRequestIceCCount() {
		return hget(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.ICEC);
	}
	
	public String getRequestIceSCount() {
		return hget(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.ICES);
	}
	
	public String getRequestIntrCount() {
		return hget(Constant.REDIS_REQUEST_TYPE_COUNT,RequestLog.INTR);
	}

	
}
