package com.meiliwan.emall.bkstage.web;

import java.util.TreeMap;

/**
 * 用于定义相关常量
 * User: wuzixin
 * Date: 13-6-16
 * Time: 上午11:28
 */
public class CmsHelper {
    public static TreeMap<Integer,String> getCmsIndex(){
        TreeMap<Integer,String> map = new TreeMap<Integer, String>();
        map.put(1,"首页");
        map.put(2,"舌尖东盟");
        map.put(3,"珠宝美饰");
        map.put(4,"特惠区");

        return map;
    }
}
