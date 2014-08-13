package com.meiliwan.emall.sp2.util;

/**
 * User: wuzixin
 * Date: 14-5-29
 * Time: 上午10:16
 */
public enum TicketBatchOptFiled {
    SELLNNUM("sell_num"),//销售数量
    ACTIVENUM("active_num"),// 激活数量
    USENUM("use_num"),// 使用数量
    REALNUM("real_num");// 优惠券实际数量

    private String code;

    public String getCode() {
        return code;
    }

    private TicketBatchOptFiled(String code) {
        this.code = code;
    }
}
