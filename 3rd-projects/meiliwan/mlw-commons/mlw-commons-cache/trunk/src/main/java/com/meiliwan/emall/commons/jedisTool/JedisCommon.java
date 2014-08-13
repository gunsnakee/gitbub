package com.meiliwan.emall.commons.jedisTool;

/**
 * Created with IntelliJ IDEA.
 * User: jiawuwu
 * Date: 13-10-4
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class JedisCommon {

    private JedisCommon(){

    }

    public static final  String GROUP_NODE = "/redis/cacheGroupInfo";
    public static final  int DEFAULT_TIME_WAIT = 2000; // 默认jedis操作超时时间，2秒
    public static final  int RETRY_TIMES = 3;//读写失败的重试次数
    public static final  long SLEEP_TIME = 200;//读写失败时重试时间间隔 单位 毫秒
    public static final  String RDWNM_CODE = "RDWNM-500";

}
