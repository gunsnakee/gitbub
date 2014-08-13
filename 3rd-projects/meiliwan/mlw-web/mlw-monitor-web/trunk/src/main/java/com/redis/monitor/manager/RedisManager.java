package com.redis.monitor.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.redis.monitor.RedisConfig;
import com.redis.monitor.RedisInfoDetail;
import com.redis.monitor.RedisServer;
import com.redis.monitor.entity.Operate;

public interface RedisManager {

	List<RedisServer> redisServerList();

	List<RedisInfoDetail> getRedisInfo();

	List<RedisConfig> getRedisConfigXmlDetail();

	Map<String, String> getRedisConfigByPattern(String pattern);

	String configRedisConfigXml(String key, String value);

	Long getRedisDbSize();

	String ping();

	String flushAll();

	String flushDb();

	List<Operate> findAllOperateDetail();

	void startMonitor(String uuid);

	void stopMonitor(String uuid);

	Set<String> getKeysByPattern(String uuid, String patternKey);

	String get(String key);

	Map<String, String> getMap(String key);

	List<String> getList(String key);

	Set<String> getSet(String key);

	Map<String, Object> getMemeryInfo();

	Map<String, Object> getKeysSize();

	String set(String key, Object value);

	void delete(String... key);

	long ttl(String key);
}
