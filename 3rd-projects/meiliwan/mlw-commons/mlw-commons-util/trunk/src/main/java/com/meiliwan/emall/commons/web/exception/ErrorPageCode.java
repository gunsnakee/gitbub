package com.meiliwan.emall.commons.web.exception;


/**
 * 定义了错误页面的code
 * @author yuxiong
 *
 */
public enum ErrorPageCode {
    //404
    ERROR_404("404"),
     //500
    ERROR_500("500"),

    //重复提交
    ERORR_REPEATSUBMIT("重复提交"),

    //创建订单-重复提交
    ERORR_CREATEORDER_REPEATSUBMIT("创建订单-重复提交"),

    //创建订单-数据不合法
    ERORR_CREATEORDER_DATAINVALID("创建订单-数据不合法"),

    //库存不足
    ERORR_STOCK_SHORTAGE("库存不足"),
    //无效的商品
    ERORR_PRODUCT_INVALID("无效的商品"),
    //无效的用户
    ERROR_USER_INVALID("无效的用户"),
    //无效的订单
    ERROR_ORDER_INVALID("无效的订单"),
    //无效的提交数据
    ERROR_DATA_INVALID("无效的提交数据"),
    //系统异常
    EXCEPTION_SYSTEM_REDIS("Redis系统异常"),

    //系统异常
    EXCEPTION_SYSTEM_SERVICE("系统服务异常"),

    //系统异常
    EXCEPTION_SYSTEM_DEFAULT("系统处理异常") ;

    private String desc;

    ErrorPageCode(String desc){
         this.desc = desc;
    }
}
