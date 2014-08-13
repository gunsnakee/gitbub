package com.meiliwan.emall.commons.mongodb;

import com.meiliwan.emall.commons.jedisTool.JedisKey;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.net.UnknownHostException;

public class MongoTest {

	//db.mycoll.ensureIndex({"key":1,"id":1}, {unique:true});
	//db.mycoll.ensureIndex({"priority":-1,"upTime":-1});
	/**
	 * 
	 * @param args
	 * @throws java.net.UnknownHostException
	 */
	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
//		Mongo mongoClient = new Mongo();
		// or
//		MongoClient mongoClient = new MongoClient( "10.249.15.195" );
		// or
//		Mongo mongoClient = new Mongo( "10.249.15.195" , 27017 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
//		MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//		                                      new ServerAddress("localhost", 27018),
//		                                      new ServerAddress("localhost", 27019)));

//		DB db = mongoClient.getDB( "redis-persist" );
//		DBCollection coll = db.getCollection("cacheGroupInfo");
//		
//		DBObject obj = new BasicDBObject(3);
//		obj.put("_id", JedisKey.base$sys.name() + "_" + "12345");
////		obj.put("key", JedisKey.base$sys.name());
////		obj.put("id", "12345");
//		obj.put("priority", 10);
//		obj.put("type", "set");
//		obj.put("val", "lsflsf524@126.com");
//		obj.put("upTime", System.currentTimeMillis());
//		
////		coll.insert(obj);
//		
//		coll.save(obj);
//
//		DBObject queryObj = new BasicDBObject(3);
//		queryObj.put("_id", JedisKey.base$sys.name() + "_" + "12345");
//		
//		DBObject persistObj = coll.findOne(queryObj);
//		System.out.println(persistObj);
		
		//MongoClient.getInstance().set(JedisKey.test$test, "12345", "hahaha");
		
		//String val = MongoClient.getInstance().get(JedisKey.test$test, "12345");
		//System.out.println(val);

        //MongoClient.getInstance().hset(JedisKey.vu$hash,"12345678","123","0");
        //MongoClient.getInstance().hset(JedisKey.vu$hash,"12345678","234","0");
        //MongoClient.getInstance().hset(JedisKey.vu$hash,"12345678","345","0");


        String val = MongoClient.getInstance().get(JedisKey.global$inc,"1");

        System.out.println("val="+val);


	}

}
