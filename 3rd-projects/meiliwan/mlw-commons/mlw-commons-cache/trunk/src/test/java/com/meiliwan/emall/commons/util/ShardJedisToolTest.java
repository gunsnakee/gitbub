package com.meiliwan.emall.commons.util;

import org.testng.annotations.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

public class ShardJedisToolTest {
	
	@Test
	public void testSRandmember(){
		try {
//			ShardJedisTool.getInstance().sadd(JedisKey.pms$cmt$score, 1234, "{1}","{2}","{3}","{4}");
//			String result = ShardJedisTool.getInstance().srandmember(JedisKey.pms$cmt$score, 1234);
//			
//			System.out.println(result);
//			
//			long num = ShardJedisTool.getInstance().scard(JedisKey.pms$cmt$score, 1234);
//			System.out.println(num);
//			ShardJedisTool.getInstance().sadd(JedisKey.pms$cardNo, 1023, "LP20131227105286", "LP20131227105287", "LP20131227105289");
//			boolean result = ShardJedisTool.getInstance().sismember(JedisKey.pms$cardNo, 1023, "LP20131227105286");
//			
//			System.out.println("result:" + result);
			
//			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxActive(40);
//			config.setMaxIdle(10);
//			config.setMaxWait(1000L);
//			
//			JedisPool pool = new JedisPool(config, "10.249.15.195", 6379);
//			Jedis jedis = pool.getResource();
//			jedis.set("test", "");
//			
//			pool.returnResource(jedis);
			
			
			ShardJedisTool.getInstance().set(JedisKey.test$test, "1234", "");
			
			String result = ShardJedisTool.getInstance().get(JedisKey.test$test, "1234");
			System.out.println("result:" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
