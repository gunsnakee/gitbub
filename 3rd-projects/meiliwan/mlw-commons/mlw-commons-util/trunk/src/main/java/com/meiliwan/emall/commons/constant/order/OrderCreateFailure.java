package com.meiliwan.emall.commons.constant.order;

/**
 * 创建订单失败情况枚举
 * Created by guangdetang on 13-10-25.
 */
public enum OrderCreateFailure {

    NO_DELIVERY("100", "温馨提示：您的收货地址不支持货到付款。"),
    PAY_CODE_ERROR("110", "温馨提示：非法的支付方式。"),
    PACK_NO_EFFECTIVE("120", "温馨提示：您购买的部分套餐商品已失效。"),
    ACT_NO_EFFECTIVE("130", "温馨提示：您购买的部分活动商品已失效。"),
    PP_ERROR("140", "温馨提示：支付密码输入有误。"),
    NE_WALLET("150", "温馨提示：钱包余额不足。"),
    NO_TRANSPORT("160", "温馨提示：该订单不支持货到付款。"),
    FREEZE_ERROR("170", "温馨提示：使用美丽湾余额支付遇到未知错误，支付失败！"),
    NORMAL_NO_EFFECTIVE("180", "温馨提示：您购买的部分商品已失效。"),
    PAY_WAY_NULL("190", "温馨提示：提交订单时，支付方式为空。"),
    NO_COD("200", "温馨提示：您购买的部分商品不支持货到付款。"),
    PARA_ERROR("210", "温馨提示：您提交的订单基本参数有误。"),
    ORDER_TYPE_ERROR("220", "温馨提示：您提交的订单类型有误。"),
    PRICE_ERROR("230", "温馨提示：您提交的订单价格有误。"),
    REPEAT_SUBMIT("240", "您的订单已提交成功，不可重复提交。"),
    SECURITY_ERROR("250", "对不起，网络不太稳定，您的订单提交失败。"),
    TF_ERROR("260", "温馨提示：您提交的订单运费有误。"),
    NO_STOCK("270", "温馨提示：您购买的部分商品库存不足。");


    private String code;
    private String desc;

    private OrderCreateFailure(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
