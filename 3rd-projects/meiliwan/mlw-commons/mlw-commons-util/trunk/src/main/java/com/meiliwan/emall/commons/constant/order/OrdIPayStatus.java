package com.meiliwan.emall.commons.constant.order;

/**
 * 定义订单行的支付状态
 * 正向： 未支付 部分支付 已支付 取消支付,  逆向： 待退款 退款失败 已退款
 * @author yuxiong
 *
 */
public enum OrdIPayStatus{

	/**
     * 正向-未支付
     */
    PAY_WAIT("10","待支付"),

    /**
     * 正向-冻结
     */
    PAY_FREEZE("11","冻结"),

    /**
     * 正向-支付失败
     */
    PAY_FAILURE("20","支付失败"),

    /**
     * 正向-部分支付
     */
    PAY_PARTIAL("25","部分支付"),

    /**
     * 正向-已支付
     */
    PAY_FINISHED("30","已支付"),

    /**
     * 正向-取消支付
     */
    PAY_CANCEL("40","取消支付"),

    /**
     * 正向-取消订单-从冻结款退款
     */
    PAY_CANCEL_FREEZE("41","取消订单-从冻结款退款"),

    /**
     * 正向-取消订单-从部分已支付退款
     */
    PAY_CANCEL_PARTIAL("42","取消订单-从部分已支付退款"),

    /**
     * 逆向-待退款
     */
    PAY_REFUND_WAIT("50","待退款"),
    /**
     * 逆向-已退款
     */
    PAY_RETUND_FINISHED("60","已退款"),

    /**
     * 逆向-退款失败
     */
    PAY_REFUND_FAILURE("70","退款失败");

    private String code;
    private String desc;

    private OrdIPayStatus(String code,String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

	public String getDesc() {
		return desc;
	}   
	
}
