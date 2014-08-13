package com.meiliwan.emall.commons.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.zookeeper.KeeperException;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.mongodb.MongoClient;

/**
 * 
 * @author lsf
 * 
 */
public class CacheToolTest {

	@Test
	public void testSet() {
		long start = -1;
		int index = 0, threadSize = 1000;
		String value = RandomUtil.randomStrCode(256);
		CountDownLatch signal = new CountDownLatch(threadSize);
		ExecutorService exec = Executors.newFixedThreadPool(20);
		for(; index < threadSize; index++){
			if(start == -1 && index == 1){
				start = System.currentTimeMillis();
			}
			try {
				boolean result = ShardJedisTool.getInstance().set(JedisKey.test$test,
						index, value);
//				System.out.println(result);
				
//			String val = CacheTool.getInstance().get(TestKey.test$test, 89765);
//			System.out.println(val);
			} catch (JedisClientException e) {
				e.printStackTrace();
			}
//			exec.execute(new ExecThread(index, value, signal));
		}
		
//		try {
//			signal.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		long time = System.currentTimeMillis() - start;
		System.out.println("time:" + (time) + ",avg time:" + (time/(index*1.0)));
//		while (true) {
//			try {
//				String result = MongoClient.getInstance().get(
//						TestKey.imeiliwan$cart,
//						"2fa619cef70e51821ac14903d71d74d4");
//
//				System.out.println("result:" + result);
//				Thread.sleep(3000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} catch (KeeperException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	static class ExecThread implements Runnable{
		private int index;
		private String str;
		private CountDownLatch signal;
		
		public ExecThread(int index, String str, CountDownLatch signal){
			this.index = index;
			this.str = str;
			this.signal = signal;
		}
		
		@Override
		public void run() {
			try {
				ShardJedisTool.getInstance().set(JedisKey.test$test,
						index, this.str);
//				ShardJedisTool.getInstance().get(JedisKey.test$test, index);
			} catch (JedisClientException e) {
				e.printStackTrace();
			}
//			System.out.println("run...");
			signal.countDown();
		}
		
	}
	
	public static void main(String[] args) throws BaseException, InterruptedException, IOException, KeeperException {
		Map<String,Object> valueMap = new HashMap<String,Object>();
		valueMap.put("name", "lsf");
		valueMap.put("age", "25");
		valueMap.put("height", "170");
		MongoClient.getInstance().hmset(JedisKey.account$paypwd, "abcd", valueMap);
	}
}
