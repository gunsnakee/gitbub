package com.meiliwan.emall.commons.constant.pay;

/**
 * Created by Sean on 13-6-16.
 */
public enum PayStatus {

	//主支付状态
    CHECK_NOPASS("10", "支付参数校验失败"),
    PAY_WAIT("3", "等待支付状态，支付方式的初始状态"),
    PAY_FAILURE("20", "支付失败"),
    PAY_PARTIAL("25", "部分已支付"),
    PAY_FINISHED("40", "支付成功"),
    
    CHECK_SUCCESS("0", "支付参数校验成功"),
    
    MONEY_INVALID("-1", "金额不合法或多种支付方式的支付金额之和不等于总金额"),
    ORDERID_INVALID("-2", "非法的订单号"),
    PAY_METHOD_NOT_EXISTS("-3", "支付方式不存在"),
    NULL_PAYCODE("-4", "支付方式不能为空"),
    NULL_PAYTYPE("-5", "支付类型不能为空"),
    MULTI_THIRD_PAYMETHOD("-6", "不能同时使用多种第三方支付方式"),
    UID_INVALID("-7", "不能同时由多个用户支付该订单"),
    BALANCE_NOT_ENOUGH("-8", "余额不足"),
    ACCOUNT_LOCK("-9", "账户已被锁定"),
    NO_PAY_PARAM("-10", "未指定支付参数"),
    INVALID_PAYCODE("-11", "该支付方式不存在"),
    NOT_ONE_THIRD_PAYMETHOD("-12", "第三方支付方式有且只能有1种"),
    CHINA_PAY_BUILD_KEY_ERROR("-13", "银联支付回调过程中buildkey失败"),
    CHINA_PAY_CHECK_FAIL("-14", "银联支付回调过程中，返回参数校验失败"),
    ORDER_EXCEPTION("-15", "订单不存在或发生异常"),
    STOCK_LESS("-16", "商品库存不足"),
    SERVICE_EXCEPTION("-17", "服务器异常"),
    PAY_DATA_NOT_EXISTS("-18","不存在该订单对应的支付数据"),

    BACK_MONEY_NOT_CORRECT_USER("-110", "退款流程中目标用户与原来付费用户的uid不一致"),
    PAYID_NOT_EXIST("-111", "退款流程中指定的交易号不存在"),
    ;
    
    private String descp;
    private String code;
    
    private PayStatus(String code, String descp){
    	this.descp = descp;
    }

	public String getDescp() {
		return descp;
	}

	public String getCode() {
        return this.code;
    }

}
