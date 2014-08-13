package com.meiliwan.emall.commons.util;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.zookeeper.KeeperException;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.mongodb.MongoClient;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.WriteResult;

public class MongoClientTest {
	
	@Test
	public void testSIsmember(){
//		try {
//			boolean result = MongoClient.getInstance().sismember(JedisKey.search$actIds, "", String.valueOf(12345));
//			
//			System.out.println(result);
//			
//			long num = MongoClient.getInstance().scard(JedisKey.search$actIds, "");
//			System.out.println("num:" +num);
//			
//			String actId = MongoClient.getInstance().srandmember(JedisKey.search$actIds, "");
//			System.out.println("actId:" + actId);
			
//			MongoClient.getInstance().hset(JedisKey.mms$emailRSPwd, "123", "vcwww", "wewee");
			
//			MongoClient.getInstance().hdel(JedisKey.mms$emailRSPwd, "123", "validateCode","vcwww");
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (KeeperException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			Mongo mongo = new Mongo("10.249.15.196");
			DB db = mongo.getDB("myTest");
			db.authenticate("mlw", "mlwhappy".toCharArray());
			DBCollection coll = db.getCollection("testColl");
			WriteResult result = coll.insert(new BasicDBObject("name", "myself"));
			
			System.out.println(result.getError());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	

}
