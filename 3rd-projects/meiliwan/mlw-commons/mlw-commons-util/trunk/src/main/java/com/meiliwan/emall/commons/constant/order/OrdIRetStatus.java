package com.meiliwan.emall.commons.constant.order;

import java.util.Arrays;
import java.util.List;

/**
 * 定义订单的退换货状态
 *
 * @author xiong.yu
 */
public enum OrdIRetStatus {

    /**
     * 逆向-申请退换货
     */
    RET_APPLY("10", "等待审核"),

    /**
     * 逆向-取消,系统自动取消
     */
    RET_BUYER_OPT_CANCEL("11", "用户取消退换货"),
    /**
     * 逆向-客服拒绝退换货，审核不通过
     */
    RET_APPLY_REJECTED("12", "拒绝申请退换货"),

    /**
     * 逆向-客服退换货，审核通过
     */
    RET_APPLY_PASSED("15", "审核通过"),


    /**
     * 客服编辑
     */
    RET_SELLER_OPT_EDIT("18", "后台人员编辑"),


    /**
     * 逆向-等待仓库确认收货
     */
    RET_BUYER_WAIT_CONSIGNMENT("20", "等待仓库确认收货"),

//    /**
//     * 逆向-上门取件
//     */
//    RET_DRAGGOODS("25"),

    /**
     *
     * 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除 即将废除
     * 逆向-买家已发货
     */
    RET_BUYER_DELIVER_GOODS("30", "等待仓库确认收货"),


    /**
     * 逆向-取消,系统自动取消
     */
    RET_SYSTEM_CANCEL("33", "取消,系统自动取消"),

    /**
     * 逆向-待协商
     */
    RET_WAIT_CONSULT("35", "待协商"),

    /**
     * 逆向-客服确认收货
     * 换货别名:等待客服发货
     */
    RET_SELLER_RECEIPTED("40", "仓库确认收货"),
    /**
     * 逆向-客服确认收货
     * 换货别名:等待客服发货
     */
    RET_SELLER_NO_RECEIPTED("41", "仓库已拒收"),


//    /**
//     * 逆向-退货入库
//     */
//    RET_INSTORAGE("45"),

    RET_SELLER_WAITING_SEND("45", "等待仓库发货"),

    //    /**
//     * 逆向-退货入库
//     */
//    RET_INSTORAGE("45"),

    RET_SELLER_AG_SEND("48", "仓库已发货"),
    /**
     * 逆向-客服已发货
     */
    RET_SELLER_CONSINGMENT("50", "等待买家确认收货"),

    /**
     * 逆向-买家确认收货
     * 换货别名:待退款
     */
    RET_BUYER_RECEIPTED("60", "买家确认收货"),

    /**
     * 逆向-买家确认收货
     *  强调 不需要退款
     */
    RET_BUYER_RECEIPTED_END("61", "退换货处理结束"),


//    /**
//     * 逆向-买家拒收
//     */
//    RET_BUYER_REJECTED("62"),


    /**
     * 逆向-待退款
     */
    RET_REFUND_WAIT("70", "等待退款"),

    /**
     * 逆向-已退款
     */
    RET_REFUND_FINISHED("75", "退款完成"),

    /**
     * 逆向-退款失败
     */
    RET_REFUND_FAILURE("76", "退款失败"),

    /**
     * 逆向-退换货已完成
     */
    RET_FINISHED("80", "退换货处理结束"),

    /**
     * 逆向-退换货已取消
     */
    RET_CANCEL("85", "客服取消退换货")

//    /**
//     * 逆向-退换货已取消
//     */
//    RET_DONE("100", "退换货处理结束")
 ;

    private String code;
    private String desc;

    private OrdIRetStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public Integer getIntCode() {
        return Integer.parseInt(this.code);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static void main(String[] args) {
        System.out.println(OrdIRetStatus.valueOf("RET_CANCEL").desc);
    }


    /**
     * 依据code返回payCode对象
     *
     * @param code
     * @return
     */
    public static OrdIRetStatus getDescByCode(String code) {
        OrdIRetStatus[] payCodes = OrdIRetStatus.values();
        for (OrdIRetStatus payCode : payCodes) {
            if (payCode.code.equals(code)) {
                return payCode;
            }
        }
        return null;
    }

    /**
     * @return 返回所有第三方支持的所有银行列表
     */
    public static List<OrdIRetStatus> getBankPayCodes() {
        return Arrays.asList(OrdIRetStatus.values());
    }

}
