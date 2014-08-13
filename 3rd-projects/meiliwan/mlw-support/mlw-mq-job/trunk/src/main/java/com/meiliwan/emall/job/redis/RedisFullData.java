package com.meiliwan.emall.job.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-9-29
 * Time: 上午9:30
 * To change this template use File | Settings | File Templates.
 */
public class RedisFullData {

    private RedisFullData(){

    }


    public static  void main(String[] args) throws JedisClientException, InterruptedException {

        String prx = System.currentTimeMillis()+"";

        long t = 4000;

        if(args.length>0){
            t = Long.parseLong(args[0]);
        }

        int idx = 0;

        Random s = new Random();

        int num = 1000;


        while (true){
            String key = prx+"_"+idx;
            System.out.println(" ======> id  "+key);
            ShardJedisTool.getInstance().set(JedisKey.vu$testNot, key, s.nextInt(100));
            Thread.sleep(t);
            if((++idx)>=num){
                break;
            }
        }


    }
}
