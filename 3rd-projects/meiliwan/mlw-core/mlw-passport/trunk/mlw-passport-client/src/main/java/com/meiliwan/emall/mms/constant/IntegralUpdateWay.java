package com.meiliwan.emall.mms.constant;

/**
 * 积分（增加、消耗）方式
 *
 * @author xiong.yu
 */
public enum IntegralUpdateWay {
    /**
     * 积分（增加、消耗）方式-0购买商品奖励积分（即：类目积分）
     */
    CATEGORY_ADD_INTEGRAL( 0,"购买商品奖励积分"),

    /**
     * 积分（增加、消耗）方式-1商品额外获得积分
     */
    EXTRA_ADD_INTEGRAL( 1,"商品额外获得积分");

    private Integer code;
    private String desc;

    private IntegralUpdateWay(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }
    public String getDesc() {
        return this.desc;
    }
    public static void main(String[] args) {
	}
}
