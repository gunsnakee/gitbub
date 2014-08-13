package com.meiliwan.emall.commons.util;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;


/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-7-16
 * Time: 下午5:46
 */
public class TimeManager {

    private final static String  defaultPath="commons/default.time.properties";
    private final static String  configurePath="commons/time.properties";
    private final static MLWLogger logger= MLWLoggerFactory.getLogger(TimeManager.class);


    public static long getTime(String key,Integer defaultValue){
        String value;
        if(defaultValue == null){
            defaultValue=1;
        }
        try {
            value = ConfigOnZk.getInstance().getValue(configurePath,key);
        } catch (BaseException e) {
            value = null;
        }

        if(value == null){
            try {
                logger.info("[时间配置没有相应配置]",configurePath+"don't have"+key,"");
                value=ConfigOnZk.getInstance().getValue(defaultPath,key);
            } catch (BaseException e1) {
                logger.info("[时间配置没有相应配置]",defaultPath+"don't have"+key+",so set default time 10","");
                value = null;
            }
        }
        return value==null?Long.valueOf(defaultValue):Long.valueOf(value);

    }
}
