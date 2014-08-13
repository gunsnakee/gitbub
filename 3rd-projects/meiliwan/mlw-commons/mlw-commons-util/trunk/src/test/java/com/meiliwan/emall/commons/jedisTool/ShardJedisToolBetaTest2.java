package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.util.RandomCode;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-4
 * Time: 下午1:26
 * To change this template use File | Settings | File Templates.
 */
public class ShardJedisToolBetaTest2 {




    @Test
    public void testExpire() throws Exception {
        ShardJedisTool.getInstance().expire(JedisKey.test$test,"123",24);
    }

    @Test
    public void testExists() throws Exception {
       System.out.println("~~~~ "+ShardJedisTool.getInstance().exists(JedisKey.vu$testNot,"123dfsdfsdfs")+"");
    }

    @Test
    public void testHexists() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hexists(JedisKey.vu$hash, "123", "dd")+"");
    }

    @Test
    public void testAppend() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().append(JedisKey.test$test, "123", "a")+"");
    }

    @Test
    public void testSet() throws Exception {
        for(int i=0;i<1000;i++){
            Thread.sleep(2000);
            System.out.println("~~~~ "+ShardJedisTool.getInstance().set(JedisKey.vu$testNot,"wujw123"+i,"123488999999999999999")+"");
        }
    }

    @Test
    public void testGet() throws Exception {
        String id = "wujw123";
        System.out.println(ShardJedisTool.getInstance().get(JedisKey.vu$test, id));

    }

    @Test
    public void testGetSet() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().getSet(JedisKey.test$test, "wujiawu123", "5")+"");
    }

    @Test
    public void testDel() throws Exception {
        for(int i=0;i<1000;i++){
            System.out.println("~~~~ "+ShardJedisTool.getInstance().del(JedisKey.vu$testNot,"wujw123"+i)+"");
        }
    }

    @Test
    public void testHset() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hset(JedisKey.vu$hash, "123", "dd", null)+"");
    }

    @Test
    public void testHget() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hget(JedisKey.vu$hash,"12355555","wewe")+"");
    }

    @Test
    public void testHgetAll() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hgetAll(JedisKey.vu$hash,"123")+"");
    }

    @Test
    public void testHmset() throws Exception {
         Map<String,Object> ss = new HashMap<String,Object>();
        ss.put("aa","aa");
        ss.put("bb","bb");
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hmset(JedisKey.vu$hashNot,"123",ss)+"");
    }

    @Test
    public void testHmget() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().hmget(JedisKey.vu$hashNot,"123","aa")+"");
    }

    @Test
    public void testSadd() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().sadd(JedisKey.vu$set,"234","7","8","9")+"");
    }

    @Test
    public void testSmembers() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().smembers(JedisKey.vu$set,"234")+"");
    }

    @Test
    public void testSrem() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().srem(JedisKey.vu$set,"234","7","")+"");
    }

    @Test
    public void testSort() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().sort(JedisKey.vu$set,"234")+"");
    }

    @Test
    public void testSort2() throws Exception {

    }

    @Test
    public void testZadd() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().zadd(JedisKey.vu$sortset,"234",123,"122")+"");
    }

    @Test
    public void testZadd2() throws Exception {
        Map<Double,String> ss = new HashMap<Double,String>();
        ss.put(110d,"");
        ss.put(180d,"");
        ss.put(70d,"");

        System.out.println("~~~~ "+ShardJedisTool.getInstance().zadd(JedisKey.vu$sortset,"123",ss)+"");
    }

    @Test
    public void testZrem() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().zrem(JedisKey.vu$sortset,"123","110")+"");
    }

    @Test
    public void testDecr() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().decr(JedisKey.vu$testNot,"123")+"");
    }

    @Test
    public void testDecrBy() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().decrBy(JedisKey.vu$testNot,"123",10)+"");
    }

    @Test
    public void testIncr() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().incr(JedisKey.vu$testNot,"123")+"");
    }

    @Test
    public void testIncrBy() throws Exception {
        System.out.println("~~~~ "+ShardJedisTool.getInstance().incrBy(JedisKey.vu$testNot,"123",90)+"");
    }

    public static void main(String[] args) throws JedisClientException {

        int times = 1000;
        long[] conts = new long[times];

        for(int i=0;i<times;i++){
            String key = RandomCode.randomStrCode(8);
            String value = RandomCode.randomStrCode(256);
            long start = System.currentTimeMillis();
            ShardJedisTool.getInstance().set(JedisKey.vu$testNot,key,value);
            long end =System.currentTimeMillis();
            conts[i] = end-start;
        }

        long total = 0;

        for(long s:conts){
            total+=s;
        }

        System.out.println(" ===> 总次数: "+times);
        System.out.println(" ===> 总时长: "+total+" ms");



    }

}
