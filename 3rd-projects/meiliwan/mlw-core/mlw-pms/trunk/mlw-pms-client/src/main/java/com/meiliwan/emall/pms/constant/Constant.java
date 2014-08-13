package com.meiliwan.emall.pms.constant;

/**
 * User: wuzixin
 * Date: 13-12-24
 * Time: 下午8:46
 */
public final class Constant {
    //商品相关状态
    //商品状态类型：商品上架状态
    public final static int PRODUCT_ON = 1;
    //商品状态类型：商品下架状态
    public final static int PRODUCT_OFF = 2;
    //商品状态类型：编辑未完成
    public final static int PRODUCT_EDITFAIL = 0;
    //商品状态类型：已删除状态
    public final static int PRODUCT_DELETE = -1;

    //商品评价相关
    //查询评价内容大于15个字的评价。
    public final static int COMMENT_CONTENT_LENGTH = 45;
    //评价内容400字以内
    public final static int COMMENT_LENGTH = 400;
    //评论回复内容400字以内
    public final static int COMMENT_REPLY_LENGTH = 400;
    //评论有用
    public final static int COMMENT_USEFUL = 1;
    //评论无用
    public final static int COMMENT_USELESS = 0;
    // 是否评论_未
    public final static short COMMENT_IS_COMMENT_NO = -1;
    //是否评论_已
    public final static short  COMMENT_IS_COMMENT_YES = 0;

    //商品咨询相关
    //咨询回复长度大于5个字
    public final static int CONSULT_REPLY_LENGTH_START = 5;
    //咨询回复内容长度小于1000字
    public final static int CONSULT_REPLY_LENGTH_END = 1000;

    //投诉相关
    //投诉回复长度大于5个字
    public final static int COMPLAINTS_REPLY_LENGTH_START = 5;
    //投诉回复内容长度小于500字
    public final static int COMPLAINTS_REPLY_LENGTH_END = 500;
    //投诉未处理
    public final static short COMPLAINTS_NO_HANDLE = 0;

    /** 商品能否加入购物车标志 根据商品相关信息做为判断条件 */
    /**库存不足状态*/
    public final static String PRODUCT_STOCK_SHORT = "short";
    /** 商品已经下架 */
    public final static String PRODUCT_GET_OFF ="off";

    public final static String PRODUCT_SEARCH_NAME = "product";

    //状态删除
    public static final short STATE_DEL = -1;
    //状态有效
    public static final short STATE_VALID = 1;
    //状态无效
    public static final short STATE_UNVALID = 0;

    //状态有效
    public static final short COD_VALID = 1;
    //状态无效
    public static final short COD_UNVALID = 0;

    //卡相关类型
    //电子卡
    public static final int EPCARD = 1;
    //实体卡
    public static final int LPCARD = 0;

    //卡相关状态
    //未导出
    public static final int CARDUNExport = 0;
    //已导出
    public static final int CARDExport = 1;
    //未销售
    public static final int CARDUNSELL = 0;
    //已销售
    public static final int CARDSELL = 1;
    //未激活
    public static final int CARDUNACTIVE = 0;
    //已激活
    public static final int CARDACTIVE = 1;
    //解冻
    public static final int CARDUNFREEZE = 0;
    //冻结
    public static final int CARDFREEZE = 1;
    //未作废
    public static final int CARDUNDEL = 0;
    //已作废
    public static final int CARDDEL = 1;

    //是否是sku属性-是
    public static final short IS_SKU_YES = 1;
    //是否是sku属性-否
    public static final short IS_SKU_NO = 0;

    //是否是异图-是
    public static final short IS_IMAGE_YES = 1;
    //是否是异图-否
    public static final short IS_IMAGE_NO = 0;

    //表示无规格
    public static final short SKU_FLAG_ZERO = 0;
    //表示单规格
    public static final short SKU_FLAG_ONE = 1;
    //表示双规格
    public static final short SKU_FLAG_TWO = 2;

}
