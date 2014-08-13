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
 * Time: 下午12:49
 * To change this template use File | Settings | File Templates.
 */
public class RedisDelData {


    private RedisDelData(){

    }

    public static  void main(String[] args) throws JedisClientException {

        if(args.length==0){
            System.out.println(" args leng must >0 ");
            return;
        }

        boolean result = false;
        if(args.length==1){
            result = ShardJedisTool.getInstance().del(JedisKey.vu$test,args[0]);
        }else if(args.length==2){
            result = ShardJedisTool.getInstance().del(JedisKey.valueOf(args[0]),args[1]);
        }else if(args.length==3){
            result = JedisExecutor.initInstance(new ServerInfo(args[1],Integer.parseInt(args[2]))).del(JedisKey.vu$test,args[0]).getOperRs()>0;
        }else if(args.length==4){
            result = JedisExecutor.initInstance(new ServerInfo(args[2],Integer.parseInt(args[3]))).del(JedisKey.valueOf(args[0]),args[1]).getOperRs()>0;
        }

        System.out.println("~~~ result="+result+" args="+ Arrays.toString(args));



    }

}
