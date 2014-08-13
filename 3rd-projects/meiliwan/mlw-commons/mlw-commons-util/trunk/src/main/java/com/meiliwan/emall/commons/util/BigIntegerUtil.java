package com.meiliwan.emall.commons.util;

import java.math.BigInteger;

/**
 * BigInteger工具类
 * User: jiawu.wu
 * Date: 13-7-29
 * Time: 上午11:18
 * To change this template use File | Settings | File Templates.
 */
public class BigIntegerUtil {

    private BigIntegerUtil(){

    }

    /**
     * 实现一个BigInteger和int的求和
     * @param a
     * @param b
     * @return
     */
    public static BigInteger add(BigInteger a,int b){
       return  a.add(new BigInteger(b+""));
    }

    /**
     * 实现一个BigInteger的自增1
     * @param a
     * @return
     */
    public static BigInteger autoIncrement(BigInteger a){
       return  add(a,1);
    }

    /**
     * 将指定基数(radix)的 BigInteger 的字符串自增1
     * @param a
     * @param radix
     * @return
     */
    public static BigInteger autoIncrement(String a,int radix){
        return  autoIncrement(new BigInteger(a,radix));
    }

    /**
     * 将十进制的BigInteger 的字符串自增1
     * @param a
     * @return
     */
    public static BigInteger autoIncrement(String a){
        return  autoIncrement(new BigInteger(a));
    }

    /**
     * 将十六进制的BigInteger 的字符串自增1
     * @param a
     * @return
     */
    public static BigInteger autoIncrementHex(String a){
        return  autoIncrement(new BigInteger(a,16));
    }


}
