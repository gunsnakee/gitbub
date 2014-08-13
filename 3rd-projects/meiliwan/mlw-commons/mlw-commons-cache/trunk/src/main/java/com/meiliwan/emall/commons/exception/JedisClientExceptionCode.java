package com.meiliwan.emall.commons.exception;

/**
 * redis client 错误代码
 * User: jiawuwu
 * Date: 13-10-13
 * Time: 下午12:48
 * To change this template use File | Settings | File Templates.
 */
public enum JedisClientExceptionCode {

    ERROR_PARAM("参数错误"),
    ERROR_ZK("Zookeeper异常"),
    ERROR_CONNECT("Redis连接异常"),
    ERROR_TIMEOUT("Redis连接超时"),
    ERROR_500("500")
    ;

    private String descr;

    public String getDescr() {
        return descr;
    }

    JedisClientExceptionCode(String descr) {
       this.descr = descr;
    }
}
