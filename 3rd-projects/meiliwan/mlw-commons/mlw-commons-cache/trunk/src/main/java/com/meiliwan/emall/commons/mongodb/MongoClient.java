package com.meiliwan.emall.commons.mongodb;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.jedisTool.*;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.plugin.zk.ZKClient;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.mongodb.*;

import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.net.UnknownHostException;
import java.util.*;

/**
 * 
 * @author lsf
 * 
 */
public class MongoClient {

	private final static MLWLogger LOGGER = MLWLoggerFactory
			.getLogger(MongoClient.class);

	public final static String SPLITER = "#@";
	private final static String DB_NAME = "redis-persist";
	private final static String COLL_NAME = "baseServInfo";
	private final static String ZK_NAME = "conf.properties";
	private final static String CONF_PATH = JedisCommon.GROUP_NODE + "/"
			+ ZK_NAME;
	private DB db;
	private static MongoClient mongoClient = null;
	private Mongo mongo;
	private DBCollection coll;

	public Properties parsePropertiesString(String s) throws IOException {
		final Properties p = new Properties();
		try {
			p.load(new StringReader(s));
		} catch (IOException e) {
			LOGGER.error(e, "new Properties().load(new StringReader(s)): {s:"
					+ s + "}", "");
			throw e;
		}
		return p;
	}

	private MongoClient() throws KeeperException, InterruptedException,
			IOException {
		String conf_cont = null;
		try {
			conf_cont = ZKClient.get().getStringData(CONF_PATH);
		} catch (KeeperException e) {
			LOGGER.error(e,
					"ZKClient.get().getStringData: {" + CONF_PATH + "}", "");
			throw e;
		} catch (InterruptedException e) {
			LOGGER.error(e,
					"ZKClient.get().getStringData: {" + CONF_PATH + "}", "");
			throw e;
		}
		Properties p = parsePropertiesString(conf_cont);
		String mongoUser = p.getProperty("mongoUser");
		String mongoPwd = p.getProperty("mongoPwd");
		ConfigUtil.initList(JedisCommon.GROUP_NODE);

		db = getMongo().getDB(DB_NAME);
		db.authenticate(mongoUser, mongoPwd.toCharArray());

		coll = db.getCollection(COLL_NAME);
	}

	private Mongo getMongo() throws UnknownHostException {
		if (mongo == null || db == null || coll == null) {
			synchronized (Mongo.class) {
				if (mongo == null || db == null || coll == null) {
					List<ServerInfo> presistNodes = ConfigUtil
							.getPresistNodes();
					int length = presistNodes.size();
					if (length > 0) {
						List<ServerAddress> seeds = new ArrayList<ServerAddress>(
								length);
						for (ServerInfo s : presistNodes) {
							ServerAddress sa = null;
							try {
								sa = new ServerAddress(s.getHost(), s.getPort());
							} catch (UnknownHostException e) {
								LOGGER.error(e,
										"new ServerAddress: {ip:" + s.getHost()
												+ ",port:" + s.getPort() + "}",
										null);
								throw e;
							}
							seeds.add(sa);
						}
						mongo = new Mongo(seeds);
						mongo.setWriteConcern(WriteConcern.SAFE);
					}
				}
			}
		}
		return mongo;
	}

	public static MongoClient getInstance() throws InterruptedException,
			IOException, KeeperException {
		if (mongoClient == null) {
			synchronized (MongoClient.class) {
				if (mongoClient == null) {
					mongoClient = new MongoClient();
				}
			}
		}
		return mongoClient;
	}

	private String buildKey(JedisKey key, Serializable id) {
		return key.name() + SPLITER + id;
	}

	private boolean updateCommon(DBObject queryObj, JedisKey key,
			Serializable id) throws BaseException {

		DBObject saveObj = new BasicDBObject();
		saveObj.put("priority", key.getPriority());
		saveObj.put("type", key.getKeyType());
		if (!key.isPersist()) {
			saveObj.put("expireTime",
					System.currentTimeMillis() + 1000l * key.getExpTime());
		}
		saveObj.put("upTime", System.currentTimeMillis());
		BasicDBObject updateObj = new BasicDBObject("$set", saveObj);
		WriteResult result = coll.update(queryObj, updateObj, true, true);
		boolean rs = result.getN() > 0;
		if (!rs) {
			LOGGER.error(null,
					"coll.update(queryObj,updateObj,true,true): {queryObj:"
							+ queryObj + ",updateObj:" + updateObj + "}", null);
			throw new BaseException(result.getError());
		}
		return rs;
	}

	public Long expire(JedisKey key, Serializable id, int seconds)
			throws BaseException {

		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		DBObject updateObj = new BasicDBObject();
		updateObj.put("$set",
				new BasicDBObject("expireTime", System.currentTimeMillis()
						+ 1000l * seconds));

		WriteResult rs = coll.update(queryObj, updateObj, false, true);

		long returnVal = rs.getN();

		LOGGER.debug(" =====> set to mongo. key=" + key.name() + " id=" + id
				+ " seconds=" + seconds + " result=" + returnVal);

		return returnVal;

	}

	public boolean set(JedisKey key, String id, String value)
			throws BaseException {

		DBObject obj = new BasicDBObject(5);
		obj.put("_id", buildKey(key, id));
		obj.put("priority", key.getPriority());
		obj.put("type", key.getKeyType());
		if (!key.isPersist()) {
			obj.put("expireTime",
					System.currentTimeMillis() + 1000l * key.getExpTime());
		}
		obj.put("upTime", System.currentTimeMillis());
		obj.put("val", value);

		WriteResult rs = coll.save(obj);
		boolean returnVal = rs.getN() > 0;

		if (!returnVal) {
//			LOGGER.error(null, "coll.save(obj): {obj:" + obj + "}", null);
			throw new BaseException("coll.save(obj): {obj:" + obj + "} failure");
		}

		LOGGER.debug(" =====> set to mongo. key=" + key.name() + " id=" + id
				+ " value=" + value + " result=" + returnVal);

		return returnVal;
	}

	public String get(JedisKey key, String id) {

		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		DBObject fieldObj = new BasicDBObject(1);
		fieldObj.put("val", 1);

		DBObject persistObj = coll.findOne(queryObj, fieldObj);
		String rs = persistObj == null || persistObj.get("val") == null ? null
				: persistObj.get("val").toString();
		LOGGER.debug(" =====> get from mongo. key=" + key.name() + " id=" + id
				+ " result=" + rs);

		return rs;
	}

	public String getSet(JedisKey key, String id, String newValue)
			throws BaseException {

		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		DBObject updateObj = new BasicDBObject();
		updateObj.put("$set", new BasicDBObject("val", newValue));

		DBObject rsObj = coll.findAndModify(queryObj, null, null, false,
				updateObj, false, true);
		return rsObj == null || rsObj.get("val") == null ? null : rsObj.get(
				"val").toString();
	}

	public boolean del(JedisKey key, String id) throws BaseException {
		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		coll.remove(queryObj);
		/*
		 * boolean rs= result.getN() > 0;
		 * 
		 * if(!rs){ Map<String,Object> para = new HashMap<String, Object>();
		 * para.put("error",result.getError());
		 * para.put("callFunc","coll.remove(queryObj)");
		 * para.put("queryObj",queryObj); LOGGER.error(null,para,null); throw
		 * new BaseException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT); }
		 * LOGGER.debug
		 * (" =====> del from mongo. key="+key.name()+" id="+id+" result="+rs);
		 * 
		 * return rs;
		 */
		return true;

	}

	public boolean exists(JedisKey key, String id) {

		DBObject obj = new BasicDBObject(1);
		obj.put("_id", buildKey(key, id));
		DBCursor dbCursors = coll.find(obj);
		boolean rs = dbCursors == null ? false : dbCursors.size() > 0 ? true
				: false;
		LOGGER.debug(" =====> get from mongo. key=" + key.name() + " id=" + id
				+ " result=" + rs);
		return rs;
	}

	public boolean hset(JedisKey key, String id, String field, String value)
			throws BaseException {

		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));

		updateCommon(queryObj, key, id);

		DBObject operObj = new BasicDBObject(1);
		operObj.put("$set", new BasicDBObject("val." + field, value));

		WriteResult rs = coll.update(queryObj, operObj, true, true);
		boolean returnVal = rs.getN() > 0;

		if (!returnVal) {
//			LOGGER.error(null,
//					"coll.update(queryObj,operObj, true,true): {queryObj:"
//							+ queryObj + ",operObj:" + operObj + "}", null);
			throw new BaseException(
					"coll.update(queryObj,operObj, true,true): {queryObj:"
							+ queryObj + ",operObj:" + operObj + "} failure");
		}
		LOGGER.debug(" =====> hset to mongo. key=" + key.name() + " id=" + id
				+ " field=" + field + " value=" + value + " result="
				+ returnVal);

		return returnVal;

	}
	
	public boolean hdel(JedisKey key, Serializable id, String... fields) throws BaseException {
		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));
		
		updateCommon(queryObj, key, id);
		
		DBObject valObj = new BasicDBObject(fields.length);
		for(String field : fields){
			valObj.put("val." + field, 1);
		}
		
		DBObject operObj = new BasicDBObject();
		operObj.put("$unset", valObj);
		WriteResult rs = coll.update(queryObj, operObj);
		boolean returnVal = rs.getN() >= 0;

		if (!returnVal) {
//			LOGGER.error(null,
//					"mongoclient.hdel(" + key + "," + id + "," + fields, null);
			throw new BaseException(
					"mongoclient.hdel(" + key + "," + id + "," + fields);
		}
		LOGGER.debug(" =====> hset to mongo. key=" + key + " id=" + id
				+ " fields=" + fields + " result="
				+ returnVal);

		return returnVal;
	}

	public boolean hmset(JedisKey key, Serializable id, Map<String, Object> map)
			throws BaseException {

		if (map == null || map.isEmpty()) {
//			LOGGER.error(null, this.getClass().getSimpleName()
//					+ ".hmset(key,id,map): {map is null}", null);
			throw new BaseException(this.getClass().getSimpleName()
					+ ".hmset(key,id,map): {map is null} failure");
		}

		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));
		updateCommon(queryObj, key, id);
		Set<Map.Entry<String, Object>> entries = map.entrySet();
		WriteResult rs = null;

		for (Map.Entry<String, Object> entrie : entries) {
			BasicDBObject upObj = new BasicDBObject("$set", new BasicDBObject(
					"val." + entrie.getKey(), entrie.getValue()));
			rs = coll.update(queryObj, upObj, true, true);
			if (!(rs.getN() > 0)) {
//				LOGGER.error(null,
//						"coll.update(queryObj,upObj,true,true): {queryObj:"
//								+ queryObj + ",upObj:" + upObj + "}", null);
				throw new BaseException(
						"coll.update(queryObj,upObj,true,true): {queryObj:"
								+ queryObj + ",upObj:" + upObj + "} failure");
			}
		}

		return true;
	}

	public String hget(JedisKey key, String id, String field) {

		DBObject idObj = new BasicDBObject(1);
		idObj.put("_id", buildKey(key, id));

		DBObject fieldObj = new BasicDBObject(1);
		fieldObj.put("val." + field, "1");

		String val = null;

		DBObject mongoObj = coll.findOne(idObj, fieldObj);

		if (mongoObj == null) {
			val = null;
		} else {
			BasicDBObject baseMongoObj = (BasicDBObject) mongoObj.get("val");
			if (baseMongoObj == null) {
				val = null;
			} else {
				val = (String) baseMongoObj.get(field);
			}
		}

		LOGGER.debug(" =====> hget from mongo. key=" + key.name() + " id=" + id
				+ " field=" + field + "  result=" + val);

		return val;

	}

	public Map<String, String> hgetAll(JedisKey key, Serializable id) {

		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		DBObject fieldObj = new BasicDBObject(1);
		fieldObj.put("val", 1);

		Map<String, String> rs = null;

		DBObject persistObj = coll.findOne(queryObj, fieldObj);
		rs = persistObj == null || persistObj.get("val") == null ? null
				: (Map<String, String>) persistObj.get("val");
		LOGGER.debug(" =====> hgetAll from mongo. key=" + key.name() + " id="
				+ id + " result=" + rs);

		return rs;
	}

	public boolean hexists(JedisKey key, String id, String field) {

		DBObject obj = new BasicDBObject(2);
		obj.put("_id", buildKey(key, id));

		DBObject rexObj = new BasicDBObject(1);
		rexObj.put("$regex", ".*");

		obj.put("val." + field, rexObj);

		DBCursor dbCursors = coll.find(obj);
		boolean returnVal = dbCursors == null || dbCursors.size() == 0 ? false
				: true;
		LOGGER.debug(" =====> hexists from mongo. key=" + key.name() + " id="
				+ id + " field=" + field + " result=" + returnVal);
		return returnVal;
	}

	public boolean sadd(JedisKey key, String id, String... members)
			throws BaseException {

		if (members == null || members.length == 0) {
			Map<String, Object> para = new HashMap<String, Object>();
			para.put("error", "Mongo的sadd操作，参数members不能为空");
			LOGGER.error(null, para, null);
			throw new BaseException("Mongo的sadd操作，参数members不能为空");
		}

		BasicDBObject queryObj = new BasicDBObject("_id", buildKey(key, id));

		updateCommon(queryObj, key, id);

		BasicDBObject updateObj = new BasicDBObject("$addToSet",
				new BasicDBObject("val", new BasicDBObject("$each", members)));

		WriteResult result = coll.update(queryObj, updateObj, true, true);

		boolean returnVal = result.getN() > 0;

		if (!returnVal) {
			LOGGER.error(null,
					"coll.update(queryObj,updateObj,true,true): {queryObj:"
							+ queryObj + ",updateObj:" + updateObj + "}", null);
			throw new BaseException(
					"coll.update(queryObj,updateObj,true,true): {queryObj:"
							+ queryObj + ",updateObj:" + updateObj
							+ "} failure");
		}

		LOGGER.debug(" =====> sadd to mongo. key=" + key.name() + " id=" + id
				+ " members=" + Arrays.toString(members) + " result="
				+ returnVal);

		return returnVal;

	}

	public List<String> smembers(JedisKey key, Serializable id) {
		DBObject queryObj = new BasicDBObject(1);
		queryObj.put("_id", buildKey(key, id));

		DBObject fieldObj = new BasicDBObject(1);
		fieldObj.put("val", 1);
		DBObject persistObj = coll.findOne(queryObj, fieldObj);
		List<String> returnVal = persistObj == null
				|| persistObj.get("val") == null ? null
				: (List<String>) persistObj.get("val");
		LOGGER.debug(" =====> smembers from mongo. key=" + key.name() + " id="
				+ id + " result=" + returnVal);
		return returnVal;
	}

	public Set<String> smembersSet(JedisKey key, Serializable id) {
		Set<String> membersSet = new HashSet<String>();
		List<String> memberslist = smembers(key, id);
		if (memberslist != null) {
			for (String s : memberslist) {
				membersSet.add(s);
			}
		}
		return membersSet;
	}

	public boolean srem(JedisKey key, Serializable id, String... members)
			throws BaseException {

		if (members == null || members.length == 0) {
			return false;
		}

		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));

		updateCommon(queryObj, key, id);

		BasicDBObject updateObj = new BasicDBObject("$pullAll",
				new BasicDBObject("val", members));

		WriteResult result = coll.update(queryObj, updateObj, false, true);

		boolean returnVal = result.getN() > 0;

		if (!returnVal) {
//			LOGGER.error(null,
//					"coll.update(queryObj,updateObj,false,true): {queryObj:"
//							+ queryObj + ",updateObj:" + updateObj + "}", null);
			throw new BaseException(
					"coll.update(queryObj,updateObj,false,true): {queryObj:"
							+ queryObj + ",updateObj:" + updateObj
							+ "} failure");
		}

		LOGGER.debug(" =====> srem to mongo. key=" + key.name() + " id=" + id
				+ " members=" + Arrays.toString(members) + " result="
				+ returnVal);

		return returnVal;
	}

	public boolean sismember(JedisKey key, Serializable id, String value) {
		if (StringUtils.isEmpty(value)) {
			return false;
		}
		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));
		queryObj.put("val", value);

		long num = coll.count(queryObj);

		return num > 0;
	}

	/**
	 * 该方法可能会有性能问题，慎用
	 * 
	 * @param key
	 * @param id
	 * @return
	 */
	public long scard(JedisKey key, Serializable id) {
		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));

		DBObject persistObj = coll.findOne(queryObj,
				new BasicDBObject("val", 1));

		List<String> arrList = persistObj.get("val") == null ? null
				: (List<String>) persistObj.get("val");

		return arrList == null ? 0 : arrList.size();
	}

	/**
	 * 该方法可能会有性能问题，慎用
	 * @param key
	 * @param id
	 * @return
	 */
	public String srandmember(JedisKey key, Serializable id) {
		DBObject queryObj = new BasicDBObject("_id", buildKey(key, id));

		DBObject persistObj = coll.findOne(queryObj,
				new BasicDBObject("val", 1));

		List<String> arrList = persistObj.get("val") == null ? null
				: (List<String>) persistObj.get("val");
		if(arrList == null || arrList.size() <= 0) {
			return null;
		}
		
		int index = RandomUtil.rand(arrList.size());
		if(index >= arrList.size()){
			index = arrList.size() - 1;
		}
		if(index < 0){
			index = 0;
		}
		
		return arrList.get(index);
	}

	public boolean expire() throws BaseException {
		long now = System.currentTimeMillis();
		DBObject obj = new BasicDBObject("expireTime", new BasicDBObject("$lt",
				now));
		WriteResult result = coll.remove(obj);
		boolean returnVal = result.getN() >= 0;
		LOGGER.debug(" =====> expire to mongo.  result=" + returnVal
				+ ",affected rows=" + result.getN());
		if (!returnVal) {
//			LOGGER.error(null, "coll.remove(obj): {obj:" + obj + "}", null);
			throw new BaseException("coll.remove(obj): {obj:" + obj
					+ "} failure");
		}
		return returnVal;
	}

	public DBCursor getCursor(Long lastTime) throws UnknownHostException {
		DBObject sortObj = new BasicDBObject(2);
		sortObj.put("priority", -1);
		sortObj.put("upTime", -1);

		if (lastTime == null) {
			return coll.find().sort(sortObj);
		} else {
			return coll.find(
					new BasicDBObject("upTime", new BasicDBObject("$gt",
							lastTime))).sort(sortObj);
		}

	}

}
