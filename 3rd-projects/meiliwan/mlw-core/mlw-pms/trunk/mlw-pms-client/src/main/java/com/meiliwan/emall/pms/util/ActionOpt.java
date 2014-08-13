package com.meiliwan.emall.pms.util;

/**
 * 定义 修改商品行为相关信息的操作 比如增加喜欢操作
 * User: wuzixin
 * Date: 13-7-3
 * Time: 下午4:17
 */
public enum ActionOpt {
    LOVE("love"),//修改商品用户行为表的喜欢数
    ONE_SOCRE_NUN("one_score_num"),//修改商品1分评论数
    TWO_SOCRE_NUM("two_score_num"), // 修改商品2分评论数
    THREE_SOCRE_NUM("three_score_num"),// 修改商品3分评论数
    FOUR_SOCRE_NUM("four_score_num"), // 修改商品4分评论数
    FIVE_SOCRE_NUM("five_score_num"),// 修改商品5分评论数
    REAL_SALE_NUM("real_sale_num"),// 修改商品真实销量数
    SHOW_SALE_NUM("show_sale_num"), // 修改商品显示销量数
    REAL_VIEW_NUM("real_view_num"), // 修改商品真实浏览数
    SHOW_VIEW_NUM("show_view_num");// 修改商品显示浏览数

    private String code;
    public String getCode() {
        return code;
    }
    private ActionOpt(String code) {
        this.code = code;
    }
}
