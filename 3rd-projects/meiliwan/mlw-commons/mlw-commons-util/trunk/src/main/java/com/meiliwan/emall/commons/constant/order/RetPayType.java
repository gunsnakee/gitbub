package com.meiliwan.emall.commons.constant.order;

/**
 * 退换货退款方式
 * Created by guangdetang on 13-10-5.
 */
public enum RetPayType {

    THIRD_BANK("THIRD_BANK", "钱退回银行卡"),
    THIRD_ALIPAY("THIRD_ALIPAY", "退钱回支付宝"),
    MLW_WALLET("MLW_WALLET", "钱退回美丽湾钱包");

    private String code;
    private String desc;

    private RetPayType(String code, String desc) {
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
