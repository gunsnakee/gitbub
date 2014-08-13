package com.meiliwan.emall.pms.util;

/**
 * 定义 修改商品相关信息的操作 例如美丽价格
 * User: wuzixin
 * Date: 13-7-3
 * Time: 下午4:17
 */
public enum ProductOpt {
    MLW_PRICE("mlw_price"),//修改商品美丽价格
    MARKET_PRICE("market_price"),// 修改商品市场价
    BAR_CODE("bar_code"); // 修改商品条形码


    private String code;
    public String getCode() {
        return code;
    }
    private ProductOpt(String code) {
        this.code = code;
    }
}
