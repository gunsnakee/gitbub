package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.BaseTest;
import org.testng.annotations.Test;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-23
 * Time: 上午11:10
 * To change this template use File | Settings | File Templates.
 */
public class JedisExecutorTest extends BaseTest {

    @Test
    public void testExists() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).exists(JedisKey.vu$test,"123").getOperRs());
        System.out.println("~~~ "+JedisExecutor.initInstance(new ServerInfo("10.249.15.195",6379)).exists(JedisKey.vu$test,"123").getOperRs());
    }

    @Test
    public void testHexists() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hexists(JedisKey.vu$hash, "123", "12").getOperRs());
    }

    @Test
    public void testExpire() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).expire(JedisKey.vu$test,"123",10).getOperRs());
    }

    @Test
    public void testAppend() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).append(JedisKey.vu$test, "123", 10).getOperRs());
    }

    @Test
    public void testSet() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).set(JedisKey.vu$test,"123","123").getOperRs());
        //System.out.println("~~~ "+JedisExecutor.initInstance(new ServerInfo("10.249.15.195",6379)).set(JedisKey.vu$test,"123","123").getOperRs());
    }

    @Test
    public void testGet() throws Exception {
        //System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).get(JedisKey.vu$test,"123").getOperRs());
        System.out.println("~~~ "+JedisExecutor.initInstance(new ServerInfo("10.249.15.195",6379)).get(JedisKey.vu$test,"123").getOperRs());
    }

    @Test
    public void testGetSet() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).getSet(JedisKey.vu$test, "123", "124").getOperRs());
    }

    @Test
    public void testDel() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).del(JedisKey.vu$test,"123").getOperRs());
        //System.out.println("~~~ "+JedisExecutor.initInstance(new ServerInfo("10.249.15.195",6379)).del(JedisKey.vu$test,"123").getOperRs());
    }

    @Test
    public void testHset() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hset(JedisKey.vu$hash, "123", "234", "df").getOperRs());
    }

    @Test
    public void testHget() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hget(JedisKey.vu$hash, "123", "123").getOperRs());
    }

    @Test
    public void testHgetAll() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hgetAll(JedisKey.vu$hash, "123").getOperRs());
    }

    @Test
    public void testHdel() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hdel(JedisKey.vu$hash, "123", "123").getOperRs());
    }

    @Test
    public void testHmset() throws Exception {
        HashMap<String,String> s = new HashMap<String, String>();
        s.put("123","123");
        s.put("234","234");
        s.put("345","345");
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hmset(JedisKey.vu$hash, "123", s).getOperRs());
    }

    @Test
    public void testHmget() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).hmget(JedisKey.vu$hash, "123","123","234").getOperRs());
    }

    @Test
    public void testSadd() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).sadd(JedisKey.vu$set, "123","123","234").getOperRs());
    }

    @Test
    public void testSmembers() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).smembers(JedisKey.vu$set, "123").getOperRs());
    }

    @Test
    public void testSrem() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).srem(JedisKey.vu$set, "123","123").getOperRs());
    }

    @Test
    public void testSort() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).sort(JedisKey.vu$sortset, "123").getOperRs());
    }

    @Test
    public void testSort2() throws Exception {

    }

    @Test
    public void testZadd() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).zadd(JedisKey.vu$sortset, "123",2.0,"24").getOperRs());
    }

    @Test
    public void testZadd2() throws Exception {

    }

    @Test
    public void testZrem() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).zrem(JedisKey.vu$sortset, "123","24").getOperRs());
    }

    @Test
    public void testDecr() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).decr(JedisKey.vu$test,"123").getOperRs());
    }

    @Test
    public void testDecrBy() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).decrBy(JedisKey.vu$test,"123",10).getOperRs());
    }

    @Test
    public void testIncr() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).incr(JedisKey.vu$test,"123").getOperRs());
    }

    @Test
    public void testIncrBy() throws Exception {
        System.out.println("~~~ "+JedisExecutor.initInstance(JedisCommon.GROUP_NODE).incrBy(JedisKey.vu$test,"123",10).getOperRs());
    }
}
