package com.meiliwan.emall.stock.constant;

/**
 * 商品库存操作失败原因CODE
 */
public final class StockErrorCode {

    /** 商品库存不足错误原因 */
    public final static String stressStockCode = "商品库存不足，无法扣减";

    /** 扣减动作，重新设置缓存失败 */
    public final static String cacheStockCode = "设置缓存失败";

    /** 数据库操作失败 */
    public final static String dbStockCode = "扣减动作，操作数据库失败";

    /** 操作成功 */
    public final static String successStockCode = "操作成功";
}
