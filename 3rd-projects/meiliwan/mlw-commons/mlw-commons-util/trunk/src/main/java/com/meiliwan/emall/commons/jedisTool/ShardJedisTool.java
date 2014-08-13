package com.meiliwan.emall.commons.jedisTool;


import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.JedisClientExceptionCode;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.mongodb.MongoClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.KeeperException;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jiawu.wu
 */
public class ShardJedisTool {

    private final static MLWLogger LOGGER = MLWLoggerFactory.getLogger(ShardJedisTool.class);

    private static ShardJedisTool instance;
    private static JedisExecutor executor;
   
    private ShardJedisTool() {
	}

    public static ShardJedisTool getInstance() throws JedisClientException {
        if (instance == null) {
            synchronized (ShardJedisTool.class) {
                if (instance == null) {
                    try {
                        instance = new ShardJedisTool();
                        executor = JedisExecutor.initInstance(JedisCommon.GROUP_NODE);
                    } catch (KeeperException e) {
                    	LOGGER.error(e, "init jedis client from zk config path '" + JedisCommon.GROUP_NODE + "'", "");
                    } catch (InterruptedException e) {
                    	LOGGER.error(e, "init jedis client from zk config path '" + JedisCommon.GROUP_NODE + "'", "");
                    }
                }
            }
        }
        return instance;
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    private Map<String,String> ckAndFatMap(Map map) throws JedisClientException {
        if(map==null || map.isEmpty()){
            throw  new JedisClientException("401","map is not allow null");
        }
        Set<Map.Entry> entries = map.entrySet();
        for(Map.Entry entry:entries){
            Object key = entry.getKey();
            if(key ==null ||key.toString().trim().equals("")){
                throw  new JedisClientException("402","map's is not allow null");
            }
            Object value = entry.getValue();
            value = value==null ? "" : value.toString();
            map.put(key,value);
        }
        return map;
    }


    private void ckStringArray(String... members) throws JedisClientException {
        if(members==null || members.length==0){
            throw  new JedisClientException("401","members is not allow null");
        }
        for(String member:members){
            if(member==null){
                throw  new JedisClientException("401","members is not allow null");
            }
        }
    }




    /**
     * 设置过期时间
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param seconds 过期时间，单位秒
     * @return 布尔值 true为设置成功
     * @throws JedisClientException
     */
    public boolean expire(JedisKey key, Serializable id, int seconds) throws JedisClientException {

        RedisReturn<Long> rs = new RedisReturn<Long>();

        try {
            rs  = executor.expire(key, id, seconds);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.expire: {jedisKey:"+key+",id:"+id+",seconds:"+seconds+"}",null);
        }


        if(key.isPersistNeeded()){
            try {
                rs.setOperRs(MongoClient.getInstance().expire(key, id, seconds));
            } catch (Exception e) {
                LOGGER.error(e, "MongoClient.expire: {jedisKey:" + key + ",id:" + id + ",seconds:" + seconds + "}", null);
                throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.expire: {jedisKey:"+key+",id:"+id+",seconds:"+seconds+"}",e);
            }
        }


        return !rs.isAllDown();
    }

    /**
     * 判断是否存在
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return 布尔值 true为存在
     * @throws JedisClientException
     */
    public boolean exists(JedisKey key, Serializable id) throws JedisClientException {

        RedisReturn<Boolean> rs = new RedisReturn<Boolean>();
        try {
            rs = executor.exists(key, id);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.exists: {jedisKey:"+key+",id:"+id+"}",null);
        }

        if( key.isPersistNeeded()){
            if(rs.isAllDown() || (rs.getOperRs()==null && key.isPersistQuery()) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().exists(key,id.toString()));
                } catch (Exception e) {
                    throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.exists: {jedisKey:"+key+",id:"+id+"}",e);
                }
            }
        }

        return rs.getOperRs();

    }

    /**
     * 判断Hash（key－value）列表中某个指定的key是否存在
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param field Hash（key－value）列表中的key
     * @return 布尔值 true－存在
     * @throws JedisClientException
     */
    public boolean hexists(JedisKey key, Serializable id, String field)throws JedisClientException {

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hexists) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<Boolean> rs = new RedisReturn<Boolean>();

        try {
            rs = executor.hexists(key, id, field);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.hexists: {jedisKey:"+key+",id:"+id+",field:"+field+"}",null);
        }



        if( key.isPersistNeeded()){
            if(rs.isAllDown() || (rs.getOperRs()==null && key.isPersistQuery()) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().hexists(key,id.toString(),field));
                } catch (Exception e) {
                    throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.hexists: {jedisKey:"+key+",id:"+id+",field:"+field+"}",e);
                }
            }
        }

        return rs.getOperRs();


    }


    /**
     * 追加字符串
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param value 需要追加的字符串
     * @return 布尔值 true－追加成功
     * @throws JedisClientException
     */
    public boolean append(JedisKey key, Serializable id, Object value) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (append) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }
        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (append) does not support Persist. key="+key.name());
        }
        RedisReturn<Long> rs = new RedisReturn<Long>();
        try {
            rs =executor.append(key, id, value);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.append: {jedisKey:"+key+",id:"+id+",value:"+value+"}",null);
        }



        if( rs.isAllDown() ){
            throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".append: {jedisKey:"+key+",id:"+id+",value:"+value+"} ");
        }

        return  ! rs.isAllDown();
    }


    /**
     * 将原值覆盖为value.toString()的结果，如果Redis里面实际的KEY不存在，则插入。
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param value 要set的值
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean set(JedisKey key, Serializable id, Object value) throws JedisClientException {

        long accessTime = System.nanoTime();

        if(value==null || value.toString().trim().equals("")){
            throw new JedisClientException("400"," value not allow null");
        }

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this (set) method does not fixed keyType. key="+key.name()+" key.keyType="+key.getKeyType());
        }

        TimeLog.log(key,id,accessTime,"past check ShardJedisTool.set params.");

        RedisReturn<String> rs = new RedisReturn<String>();

        try {
            rs = executor.set(key, id, value);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.set: {jedisKey:"+key+",id:"+id+",value:"+value+"}",null);
        }


        boolean lastResult = ! rs.isAllDown();


        //  持久化的 ， 把请求转到 mongoDb
        if( key.isPersistNeeded() ){
            try {
                lastResult = MongoClient.getInstance().set(key,id.toString(),value.toString());
            } catch (Exception e) {
                throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.set: {key:"+key+",id:"+id+",value:"+value+"}",e);
            }
        }

        TimeLog.log(key,id,accessTime,"finish execute ShardJedisTool.set function.");



        if(!lastResult){
            throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".set: {jedisKey:"+key+",id:"+id+",value:"+value+"} ");
        }

        return lastResult;


    }


    /**
     * 根据KEY取值
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return KEY关联的值
     * @throws JedisClientException
     */
    public String get(JedisKey key, Serializable id) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (get) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<String> rs = new RedisReturn<String>();

        try {
           rs = executor.get(key, id);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.get: {jedisKey:"+key+",id:"+id+"}",null);
        }


        if( key.isPersistNeeded()){
            if(rs.isAllDown() || (rs.getOperRs()==null && key.isPersistQuery()) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().get(key,id.toString()));
                } catch (Exception e) {
                    throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.get: {jedisKey:"+key+",id:"+id+"}",e);
                }
            }
        }

        return rs.getOperRs();
    }

    /**
     * 先取出值，再覆盖为newValue.toString()的结果。如果KEY不存在，则插入。
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param newValue 要覆盖为的对象
     * @return 操作前的旧值
     * @throws JedisClientException
     */
    public String getSet(JedisKey key, Serializable id,Object newValue) throws JedisClientException {


        if(newValue==null || newValue.toString().trim().equals("")){
            throw new JedisClientException("400"," newValue not allow null");
        }

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (getSet) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<String> rs = new RedisReturn<String>();

        try {
            rs = executor.getSet(key, id, newValue);
        }catch (JedisConnectionException e){
            LOGGER.error(e,"executor.getSet: {jedisKey:"+key+",id:"+id+"}",null);
        }


        String oldVal = rs.getOperRs();

        if(key.isPersistNeeded()){
            if(rs.isAllDown() || (StringUtils.isBlank(oldVal) && key.isPersistQuery())){
                try {
                    oldVal = MongoClient.getInstance().getSet(key,id.toString(),newValue.toString());
                } catch (Exception e) {
                    throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.getSet: {jedisKey:"+key+",id:"+id+",newValue:"+newValue+"}",e);
                }
            }
        }

        return oldVal;
    }


    /**
     * 删除KEY及关联的数据
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean del(JedisKey key, Serializable id) throws JedisClientException  {

        long start = System.currentTimeMillis();

        RedisReturn<Long> rs = executor.del(key, id);

        boolean lastResult = !rs.isAllDown();

        if(key.isPersistNeeded()){
            try {
                lastResult = MongoClient.getInstance().del(key,id.toString());
            } catch (Exception e) {
                throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.del: {jedisKey:"+key+",id:"+id+"}",e);
            }
        }

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".del: {jedisKey:"+key+",id:"+id+"} ");
        }

        System.out.println("===> useTime: "+(System.currentTimeMillis()-start));

        return lastResult;

    }


    /**
     * 将KEY关联的哈希表中的域 field 的值设为 value
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param field  哈希表中的key
     * @param value 哈希表中的value
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean hset(JedisKey key, Serializable id, String field,  Object value) throws JedisClientException {


        if(field==null || field.toString().trim().equals("")){
            throw new JedisClientException("400"," field not allow null");
        }

        if(value==null){
            value = "";
        }

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hset) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }


        RedisReturn<Long> rs = executor.hset(key, id, field, value);

        boolean lastResult = ! rs.isAllDown();

        // 所有redis down 的情况 以及 是 持久化的 ， 把请求转到 mongoDb
        if(key.isPersistNeeded() ){
            try {
                lastResult = MongoClient.getInstance().hset(key, id.toString(), field, value.toString());
            } catch (Exception e) {
                throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.hset: {jedisKey:"+key+",id:"+id+",field:"+field+",value:"+value+"}",e);
            }
        }

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".hset: {jedisKey:"+key+",id:"+id+",field:"+field+",value:"+value+"} ");
        }

        return lastResult;
    }




    /**
     * 获取KEY关联的哈希表中指定的域field的value
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param field 哈希表中指定的域
     * @return 哈希表field关联的value
     * @throws JedisClientException
     */
    public String hget(JedisKey key, Serializable id, String field)throws JedisClientException {


        if(field==null || field.trim().equals("")){
            throw new JedisClientException("400"," field not allow null");
        }

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hget) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<String> rs = executor.hget(key, id, field);

        if( key.isPersistNeeded()){
            if(rs.isAllDown() || (rs.getOperRs()==null && key.isPersistQuery()) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().hget(key,id.toString(),field));
                } catch (Exception e) {
                    throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.hget: {jedisKey:"+key+",id:"+id+",field:"+field+"}",e);
                }
            }
        }

        return rs.getOperRs();


    }

    /**
     * 获取KEY关联的哈希表
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return KEY关联的哈希表
     * @throws JedisClientException
     */
    public Map<String, String> hgetAll(JedisKey key, Serializable id)throws JedisClientException {

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hgetAll) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<Map<String, String>> rs = executor.hgetAll(key, id);

        if( key.isPersistNeeded()){
            if(rs.isAllDown() || ( ( rs.getOperRs()==null || rs.getOperRs().isEmpty() ) && key.isPersistQuery()) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().hgetAll(key,id.toString()));
                } catch (Exception e) {
                    throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.hgetAll: {jedisKey:"+key+",id:"+id+"}",e);
                }
            }
        }


        return rs.getOperRs();


    }

    /**
     * 删除KEY关联的哈希表中的域
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param fields 哈希表中要删除的域
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean hdel(JedisKey key, Serializable id, String... fields) throws JedisClientException{

        ckStringArray(fields);

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hdel) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if( key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (hdel) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs = executor.hdel(key, id, fields);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".hset: {jedisKey:"+key+",id:"+id+",field:"+fields+"} ");
        }

        return lastResult;


    }


    /**
     * 覆盖KEY关联的哈希表
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param map 要覆盖的哈希表
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean hmset(JedisKey key, Serializable id,Map<String, Object> map) throws JedisClientException {


        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hmset) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }


        RedisReturn<String>  rs = executor.hmset(key,id,ckAndFatMap(map));


        boolean lastResult = ! rs.isAllDown();

        if(key.isPersistNeeded()){
            try {
                lastResult = MongoClient.getInstance().hmset(key, id, map);
            } catch (Exception e) {
                throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.hmset: {jedisKey:"+key+",id:"+id+",map:"+map+"}",e);
            }
        }

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".hmset: {jedisKey:"+key+",id:"+id+",map:"+map+"} ");
        }


        return lastResult;

    }


    /**
     * 获取KEY关联的哈希表中指定的域列表对应的value列表
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param fields 哈希表中的域列表
     * @return 哈希表中的value列表中
     * @throws JedisClientException
     */
    public List<String> hmget(JedisKey key, Serializable id,
                              String... fields) throws JedisClientException{

        ckStringArray(fields);

        if( ! JedisKey.HASH.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (hmget) only can operate "+JedisKey.HASH+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (hmget) does not support Persist. key="+key.name());
        }

        RedisReturn<List<String>> rs = executor.hmget(key, id, fields);

        return rs.getOperRs();
    }


    /**
     * 往KEY关联的SET集合增加成员
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param members 要增加的成员列表
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean sadd(JedisKey key, Serializable id, String... members) throws JedisClientException{


        ckStringArray(members);

        if( ! JedisKey.SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (sadd) only can operate "+JedisKey.SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<Long> rs = executor.sadd(key, id, members);

        boolean lastResult = ! rs.isAllDown();

        if(key.isPersistNeeded()){
            try {
                lastResult = MongoClient.getInstance().sadd(key,id.toString(),members);
            } catch (Exception e) {
                throw  new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.sadd: {jedisKey:"+key+",id:"+id+",members:"+ Arrays.asList(members)+"}",e);
            }
        }

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".sadd: {jedisKey:"+key+",id:"+id+",members:"+Arrays.asList(members)+"} ");
        }

        return lastResult;
    }


    /**
     * 返回kEY关联的SET集合
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return kEY关联的SET集合
     * @throws JedisClientException
     */
    public Set<String> smembers(JedisKey key, Serializable id) throws JedisClientException{

        if( ! JedisKey.SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (smembers) only can operate "+ JedisKey.SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<Set<String>> rs =  executor.smembers(key, id);

        if(key.isPersistNeeded()){

            if(rs.isAllDown() || ( key.isPersistQuery() && ( rs.getOperRs()==null  || rs.getOperRs().size()==0 )  ) ){
                try {
                    rs.setOperRs(MongoClient.getInstance().smembersSet(key, id));
                } catch (Exception e) {
                    throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.smembersSet: {jedisKey:"+key.name()+",id:"+id+"}",e);
                }
            }
        }

        return rs.getOperRs();
    }


    /**
     * 删除KEY关联的SET集合中指定的成员members
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param members 要删除的成员
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean srem(JedisKey key, Serializable id, String... members) throws JedisClientException{

        ckStringArray(members);

        if( ! JedisKey.SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (srem) only can operate "+JedisKey.SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        RedisReturn<Long> rs =  executor.srem(key, id, members);

        boolean lastResult = ! rs.isAllDown();

        if(key.isPersistNeeded()){
            try {
                lastResult = MongoClient.getInstance().srem(key, id, members);
            } catch (Exception e) {
                throw new JedisClientException(JedisClientExceptionCode.ERROR_500,"MongoClient.srem: {jedisKey:"+key.name()+",id:"+id+",members:"+Arrays.asList(members)+"}",e);
            }
        }

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".srem: {jedisKey:"+key+",id:"+id+",members:"+Arrays.asList(members)+"} ");
        }

        return lastResult;
    }


    /**
     * 返回KEY关联的SET集合排序后的结果 （默认排序规则）
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return KEY关联的SET集合排序后的结果
     * @throws JedisClientException
     */
    public List<String> sort(JedisKey key, Serializable id) throws JedisClientException{
        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (sort) does not support Persist. key="+key.name());
        }
        RedisReturn<List<String>> rs = executor.sort(key, id);
        return rs.getOperRs();
    }


    /**
     * 返回KEY关联的SET集合排序后的结果 （params指定的排序规则）
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param params 排序规则
     * @return KEY关联的SET集合排序后的结果
     * @throws JedisClientException
     */
    public List<String> sort(JedisKey key, Serializable id,   SortingParams params) throws JedisClientException{

        if( params==null ){
            throw new JedisClientException("400"," sort params not allow null");
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (sort) does not support Persist. key="+key.name());
        }

        RedisReturn<List<String>> rs = executor.sort(key, id, params);

        return rs.getOperRs();
    }


    /**
     * 往KEY关联的有序SET集合增加value成员(字符串)
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param score 分数
     * @param value 要往SET中增加的成员
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean zadd(JedisKey key, Serializable id, double score, Object value) throws JedisClientException{

        if( value==null || value.toString().trim().equals("") ){
            throw new JedisClientException("400"," zadd value not allow null");
        }

        if( !JedisKey.SORTED_SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (zadd) only can operate "+JedisKey.SORTED_SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (zadd) does not support Persist. key="+key.name());
        }


        RedisReturn<Long> rs = executor.zadd(key, id, score, value);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".zadd: {jedisKey:"+key+",id:"+id+",score:"+score+",value="+value+"} ");
        }

        return lastResult;
    }


    /**
     * 往KEY关联的有序SET集合增加一个Map对象
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param scoreMembers 要往SET添加的map对象
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean zadd(JedisKey key, Serializable id,  Map<Double, String> scoreMembers) throws JedisClientException{

        if(scoreMembers==null || scoreMembers.size()==0 ){
            throw new JedisClientException("400"," zadd scoreMembers not allow null");
        }

        if( ! JedisKey.SORTED_SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (zadd) only can operate "+ JedisKey.SORTED_SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (zadd) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs = executor.zadd(key, id, scoreMembers);

        boolean lastResult = ! rs.isAllDown();


        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".zadd: {jedisKey:"+key+",id:"+id+",scoreMembers:"+scoreMembers+"} ");
        }

        return lastResult;
    }

    /**
     * 往KEY关联的有序SET集合删除members成员
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param members 要删除的成员
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean zrem(JedisKey key, Serializable id, String... members) throws JedisClientException{

        ckStringArray(members);

        if( ! JedisKey.SORTED_SET.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (zrem) only can operate "+JedisKey.SORTED_SET+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (zrem) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs = executor.zrem(key, id, members);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".zrem: {jedisKey:"+key+",id:"+id+",members:"+Arrays.asList(members)+"} ");
        }

        return lastResult;
    }

    /**
     * 将KEY关联的String值自减1
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean decr(JedisKey key, Serializable id) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (decr) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (decr) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs = executor.decr(key, id);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".decr: {jedisKey:"+key+",id:"+id+"} ");
        }

        return lastResult;
    }

    /**
     * 将KEY关联的String值自减byvalue值
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param byvalue 要自减的值
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean decrBy(JedisKey key, Serializable id, int byvalue) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (decrBy) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (decrBy) does not support Persist. JedisKey="+key.name());
        }

        RedisReturn<Long> rs =  executor.decrBy(key, id, byvalue);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".decr: {jedisKey:"+key+",id:"+id+"} ");
        }

        return lastResult;
    }

    /**
     * 将KEY关联的String值自减1
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean incr(JedisKey key, Serializable id) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (incr) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.getKeyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (incr) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs =executor.incr(key, id);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".incr: {jedisKey:"+key+",id:"+id+"} ");
        }

        return lastResult;
    }


    /**
     * 将KEY关联的String值自减将KEY关联的String值自减1
     *
     * @param key 跟id参数一起组成Redis里面实际的KEY
     * @param id 跟key参数一起组成Redis里面实际的KEY
     * @param byvalue 要自增的值
     * @return 布尔值 true－操作成功
     * @throws JedisClientException
     */
    public boolean incrBy(JedisKey key, Serializable id, int byvalue) throws JedisClientException {

        if( ! JedisKey.STRING.equals(key.getKeyType())){
            throw  new JedisClientException("404","this method (incrBy) only can operate "+JedisKey.STRING+". but found this -> key="+key.name()+" key.keyType="+key.getKeyType());
        }

        if(key.isPersistNeeded()){
            throw  new JedisClientException("405","this method (incrBy) does not support Persist. key="+key.name());
        }

        RedisReturn<Long> rs = executor.incrBy(key, id, byvalue);

        boolean lastResult = ! rs.isAllDown();

        if(!lastResult){
            throw new JedisClientException(JedisClientExceptionCode.ERROR_500,this.getClass().getSimpleName()+".incrBy: {jedisKey:"+key+",id:"+id+",byvalue:"+byvalue+"} ");
        }

        return lastResult;
    }

}
