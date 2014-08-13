package com.meiliwan.emall.commons.constant.order;

/**
 * 退换货类型
 * Created by guangdetang on 13-10-4.
 */
public enum RetType {


    CHG("CHG","换货"),
    RET("RET","退货");

    private String code;
    private String desc;
    private RetType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    public String getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}
