package com.meiliwan.emall.job.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisExecutor;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ServerInfo;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-9-29
 * Time: 上午11:16
 * To change this template use File | Settings | File Templates.
 */
public class RedisSetData {

    private RedisSetData(){

    }

    public static  void main(String[] args) throws JedisClientException {


        args = new String[]{"aaa","bbb"};

        if(args.length==0){
            System.out.println(" args leng must >0 ");
            return;
        }

        boolean result = false;
        if(args.length==2){
            result = ShardJedisTool.getInstance().set(JedisKey.vu$test,args[0],args[1]);
        }else if(args.length==3){
            result = ShardJedisTool.getInstance().set(JedisKey.valueOf(args[0]),args[1],args[2]);
        }else if(args.length==4){
            result = "OK".equals(JedisExecutor.initInstance(new ServerInfo(args[2], Integer.parseInt(args[3]))).set(JedisKey.vu$test, args[0], args[1]).getOperRs());
        }else if(args.length==5){
            result = "OK".equals(JedisExecutor.initInstance(new ServerInfo(args[3], Integer.parseInt(args[4]))).set(JedisKey.valueOf(args[0]),args[1],args[2]).getOperRs());
        }

        System.out.println("~~~ result="+result+" args="+ Arrays.toString(args));


    }

}
