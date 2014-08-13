package com.meiliwan.emall.mms.constant;

/**
 * 声明积分变动类型
 *
 * @author xiong.yu
 */
public enum IntegralUpdateType {
    /**
     * 积分变动-获得积分
     */
    ADD_INTEGRAL( 0,"获得积分"),

    /**
     * 积分变动-消耗积分
     */
    USE_INTEGRAL( 1,"消耗积分");

    private Integer code;
    private String desc;

    private IntegralUpdateType(Integer code, String desc) {
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
