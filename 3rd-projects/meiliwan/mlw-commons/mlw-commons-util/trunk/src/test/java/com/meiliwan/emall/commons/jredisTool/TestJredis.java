package com.meiliwan.emall.commons.jredisTool;

import java.io.UnsupportedEncodingException;

import javax.script.ScriptException;

import org.apache.zookeeper.KeeperException;
import org.testng.annotations.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.meiliwan.emall.commons.BaseTest;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

public class TestJredis extends BaseTest {

	@Test
	public void testSet() throws KeeperException, InterruptedException, JedisClientException, UnsupportedEncodingException, ScriptException{
		
//		String a = ZKClient.get().getStringData("/mlwconf/jredis");
//		System.out.println(a);
//		JedisPoolConfig config = new JedisPoolConfig();
//
//	    config.setMaxActive(100);
//	    config.setMaxIdle(20);
//	    config.setMaxWait(1000l);
//	    
//	    JedisPool pool = new JedisPool(config, "10.249.15.196", 6378);
//	    
//	    Jedis jedis = pool.getResource();  //JedisConnectionException
//	    
//	    for(int i=0; i<100; i++){
//	    	try{
////	    	    jedis.set("sjdflj" + i, "917394270rwfosdhfu902732342_" + i);  //JedisConnectionException
//	    		jedis.set("sjdflj" + i, null);// JedisDataException   或者 JedisException
//	    	    System.out.println(jedis.get("sjdflj" + i));
//	    	}catch(JedisConnectionException ex){
//	    		pool.returnResource(jedis);
//	    		ex.printStackTrace();
//	    		Thread.sleep(100);
//	    		try{
////	    		    pool = new JedisPool(config, "10.249.15.196", 6378);
//	    		    jedis = pool.getResource();  //JedisConnectionException
//	    		}catch(JedisConnectionException e){
//	    			e.printStackTrace();
//	    		}
//	    	}
//	    	Thread.sleep(3000);
//	    }
//	    
//	    pool.returnResource(jedis);
		
//		ExecutorService
//		long totalTime = 0;
//		
//		for(int i=0; i<10000; i++){
//			String key = "test-" + i;
//			String value = "aaabdufouwoeflsjdfljsdoifiuoiwuefojsldfjsdoufoiwueoifjsojdfoisdjfosuoeifuweojfosdjfo" + i;
//			long start = System.currentTimeMillis();
//			ShardJedisTool.getInstance().set(JedisKey.base$validcode, key, value);
//			totalTime += (System.currentTimeMillis() - start);
//			String a = ShardJedisTool.getInstance().get(JedisKey.base$validcode, "test-");
//			System.out.println(a);
////			Jedis jedis = pool.getResource();
////			jedis.set("base$validcode_" + "test-" + i, "aaab" + i);
////			jedis.get("base$validcode_" + "test-" + i);
////			pool.returnResource(jedis);
//		}
//		
//		System.out.println("total time : " + totalTime);
		
//		System.out.println(URLDecoder.decode("\ue583\ue583","utf-8"));
//		
//		System.out.println("\\u");
//		String province = "\\u5e7f\\u897f";
//		ScriptEngineManager manager = new ScriptEngineManager();
//		ScriptEngine engine = manager.getEngineByName("javascript");
//		engine.eval("var provice='" + province + "';");
//		System.out.println(engine.get("provice"));
		
//		List<ScriptEngineFactory> factories = manager.getEngineFactories();
//		for(ScriptEngineFactory factory: factories){
//		System.out.println(factory.getEngineName()+","+
//		factory.getEngineVersion()+","+
//		factory.getLanguageName()+"," +
//		factory.getLanguageVersion()+","+
//		factory.getExtensions()+","+
//		factory.getMimeTypes()+","+
//		factory.getNames());
//		// 得到当前的脚本引擎
////	     ScriptEngine engine = factory.getScriptEngine();
//	   }
		
	   ShardJedisTool.getInstance().set(JedisKey.test$test, "djflsjf", "sdfjosjdfosu");
	   String result = ShardJedisTool.getInstance().get(JedisKey.test$test, "djflsjf");
	   System.out.println(result);
		
	   ShardJedisTool.getInstance().hset(JedisKey.session, "osudfousdof", "sid", "sjdofsodjflweljf23rueur");
	   String value = ShardJedisTool.getInstance().hget(JedisKey.session, "osudfousdof", "sid");
	   System.out.println(value);
	}

	// @Test
	public void testSadd() throws JedisClientException {
		String[] s = new String[3];
		s[0] = 0 + "";
		s[1] = 1 + "";
		s[2] = 2 + "";
		ShardJedisTool.getInstance().del(JedisKey.mms$vUser, "uid");
		// ShardJedisTool.getInstance().sadd(JedisKey.mms$vUser,"uid",s);
		System.out.println(ShardJedisTool.getInstance().smembers(
				JedisKey.mms$vUser, "uid"));
	}

}
