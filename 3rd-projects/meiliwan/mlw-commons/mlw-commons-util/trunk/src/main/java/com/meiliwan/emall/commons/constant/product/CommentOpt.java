package com.meiliwan.emall.commons.constant.product;

/**
 * 用户评论相关 有用或者无用
 * User: wuzixin
 * Date: 13-7-25
 * Time: 下午4:37
 */
public enum CommentOpt {

    USEFULCOUNT("useful_count"), // 有用
    USELESSCOUNT("useless_count");// 无用

    private String code;
    public String getCode() {
        return code;
    }
    private CommentOpt(String code) {
        this.code = code;
    }
}
