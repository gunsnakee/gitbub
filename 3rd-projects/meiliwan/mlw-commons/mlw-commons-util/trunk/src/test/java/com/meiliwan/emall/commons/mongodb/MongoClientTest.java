package com.meiliwan.emall.commons.mongodb;

import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RandomCode;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-5
 * Time: 下午5:09
 * To change this template use File | Settings | File Templates.
 */
public class MongoClientTest{

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    @Test(invocationCount = 1000000, threadPoolSize = 4)
    public void testSet() throws Exception {
        String id = System.currentTimeMillis()+ RandomCode.randomNumCode(5);
        MongoClient.getInstance().set(JedisKey.vu$test,id,RandomCode.randomNumCode(5));
    }

    @Test
    public void testHset() throws Exception {

    }

    @Test
    public void testHmset() throws Exception {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("a","123");
        map.put("b","124");

        MongoClient.getInstance().hmset(JedisKey.vu$hash,"1234abcd",map);

    }

    @Test
    public void testExists() throws Exception {
        logger.debug(" ~~~ "+MongoClient.getInstance().exists(JedisKey.pms$detail,"10235583Q"));
    }

    @Test
    public void testHexists() throws Exception {
        logger.debug(" ~~~ "+MongoClient.getInstance().hexists(JedisKey.pms$detail,"10235583Q","totalCmtNum"));
    }

    @Test
    public void testHgetAll() throws Exception {
        MongoClient.getInstance().hgetAll(JedisKey.vu$hash,"1234abcd");
    }

    @Test
    public void testHget() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testGetSet() throws Exception {
        System.out.println("~~~> "+MongoClient.getInstance().getSet(JedisKey.vu$test,"wujiawu123456789","2"));
    }

    @Test
    public void testDel() throws Exception {

    }


    @Test
    public void testSadd() throws Exception {
        logger.debug("~~~ "+MongoClient.getInstance().sadd(JedisKey.vu$set,"2","8","9","10"));
    }

    @Test
    public void testSmembers() throws Exception {
        logger.debug("~~~ "+MongoClient.getInstance().smembers(JedisKey.vu$set,"1"));
    }

    @Test
    public void testSrem() throws Exception {
        logger.debug("~~~ "+MongoClient.getInstance().srem(JedisKey.vu$set,"2","7","10"));
    }




}
