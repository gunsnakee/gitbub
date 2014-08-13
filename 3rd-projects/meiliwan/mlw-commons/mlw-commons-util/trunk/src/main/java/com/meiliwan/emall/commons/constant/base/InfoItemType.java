package com.meiliwan.emall.commons.constant.base;

/**
 * 资讯类目常量
 * User: wuzixin
 * Date: 13-7-22
 * Time: 下午5:11
 */
public enum  InfoItemType {
    bulletin(1,"美丽公告"),
    news(2,"美丽快报"),
    learning(3,"知识课堂"),
    promotional(4,"活动资讯");

    private int code;
    private String desc;
    private InfoItemType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }

}
