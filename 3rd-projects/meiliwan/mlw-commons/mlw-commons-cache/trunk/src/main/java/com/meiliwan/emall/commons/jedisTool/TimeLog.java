package com.meiliwan.emall.commons.jedisTool;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-9
 * Time: 下午5:13
 * To change this template use File | Settings | File Templates.
 */
public class TimeLog {

    private TimeLog(){

    }

    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(TimeLog.class);

    public static  void log(JedisKey key,Serializable id,long beginTime,String append){
        long nano = System.nanoTime()-beginTime;
        LOGGER.debug(key+" "+id+" useTime="+nano+"(ns),"+(nano/1000000L)+"(ms) "+append );
    }

}
