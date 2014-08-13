package com.meiliwan.emall.cms2.vo;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 14-4-17
 * Time: 上午11:43
 * 存储
 * 设置时间
 * 生成的专题页内容
 */
public class ThematicKV {
    private long setTime;
    private String content;
    private short pageState;

    public short getPageState() {
        return pageState;
    }

    public void setPageState(short pageState) {
        this.pageState = pageState;
    }

    public long getSetTime() {
        return setTime;
    }

    public void setSetTime(long setTime) {
        this.setTime = setTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
