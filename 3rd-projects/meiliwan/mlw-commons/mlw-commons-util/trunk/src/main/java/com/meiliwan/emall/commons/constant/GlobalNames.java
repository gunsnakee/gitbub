package com.meiliwan.emall.commons.constant;

import java.util.Date;

/**
 * 全局常量定义类
 * @author yuxiong
 *
 */
public final class GlobalNames {

	//全局常量定义，状态值：有效=0
    public final static short STATE_VALID = 0;
	
	//全局常量定义，状态值：无效=-1
    public final static short STATE_INVALID = -1;
	
	//全局常量定义，状态值：审核通过2
    public final static short STATE_CHECK_PASS = 2;
	
	//全局常量定义，状态值：审核不通过-2
    public final static short STATE_CHECK_NOPASS = -2;
	
	//全局常量定义，订单类型：正向=1
    public final static short ORDER_BILL_TYPE_FORWARD = 1;
	
	//全局常量定义，订单类型：逆向=-1
    public final static short ORDER_BILL_TYPE_REVERSE = -1;
	
	public final static int ADMIN_ID = 888;
	public final static String ADMIN_NAME = "美丽湾客服";
	
	// 配置的zk根
	public final static String ZKCONFIGS_CHROOT = "/mlwconf"; 
	// 分布式锁的根
	public final static String DISTRIBUTED_LOCK_CHROOT = "/locks";
	// solr的zk路径
	public final static String SOLR_CHROOT = "/solr";
	// 
	public final static String SEARCH_CHROOT = ZKCONFIGS_CHROOT + "/search";
	//默认为系统身份的管理员ID
	public final static int ADMINID_SYS_DEFAULT = 999;
	
	// 系统管理员id session 属性key
	public final static String SESSIONKEY_ADMIN_ID = "admin_id";
	
	// 系统管理员name session 属性key
	public final static String SESSIONKEY_ADMIN_NAME = "系统";
	
    //地域编码 世界编码
    public final static String BASE_AREA_CODE_WORLD ="10" ;
    //地域编码 中国编码
    public final static String BASE_AREA_CODE_CHINA ="1000" ;
    
    public final static int BASE_CASH_ON_DELIVER_YES =1 ;
    public final static int BASE_CASH_ON_DELIVER_NO =0 ;


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
	
	public final static String REAL_INDEX = "indexName";
	//AB测试一些变量
	public static final String ABTEST_RATIO = "ratio";
	public static final String ABTEST_STRING = "abtest";
	public static final String ABTEST_PARAM = "param";
	public static final String ABTEST_ABBR = "simple";
	
	//销售类型:购销
	public final static int SELLTYPE_BUY=1;
	//销售类型:代销
	public final static int SELLTYPE_HELP=2;
	//销售类型:联营
	public final static int SELLTYPE_UNION=3;
	
	public final static int UID_NULL = Integer.MIN_VALUE;
	
	public final static int SESSION_EXPIRE_SECONDS = 10 * 6 * 30 ;

    /**搜索结果和三级类目结果显示商品条数,翻页*/
	public final static int PAGER_PRODUCT_COUNT = 32;
	public final static String PRODUCT_SEARCH_NAME = "product";

    /** 商品能否加入购物车标志 根据商品相关信息做为判断条件 */
    /**库存不足状态*/
    public final static String PRODUCT_STOCK_SHORT = "short";
    /** 商品已经下架 */
    public final static String PRODUCT_GET_OFF ="off";
    
    /** 美丽湾的顶级域名常量 */
	public static final String MLW_DOMAIN = ".meiliwan.com";

    /**商品评价列表条数,翻页*/
    public final static int PAGER_COMMENT_COUNT = 10;
    /**我的收藏列表条数,翻页*/
    public final static int PAGER_COLLECT_COUNT = 10;
    /**我的积分明细列表条数,翻页*/
    public final static int PAGER_INTEGRAL_DETAIL_COUNT = 15;
    /**我的站内信列表条数,翻页*/
    public final static int PAGER_STATIONMSG_COUNT = 10;
    /**我的咨询列表条数,翻页*/
    public final static int PAGER_CONSULT_COUNT = 10;
    /**我的投诉列表条数,翻页*/
    public final static int PAGER_COMPLAINTS_COUNT = 15;

    /**类目积分状态 停用 */
    public final static int CATEGORY_INTEGRAL_RULE_STATE_STOP = 0;
    /**类目积分状态 启用 */
    public final static int CATEGORY_INTEGRAL_RULE_STATE_STRAT = 1;
	
	/**订单24小时后无付款取消订单*/
	public final static long AUTO_CANCEL_ORDER_TIME = (new Date().getTime() / 1000) + 60 * 60*48;

    /**站内信用户是否已读-未读*/
    public final static Short USER_STATIONMSG_ISREAD_NO = 0;
    /**站内信用户是否已读- 已读*/
    public final static Short USER_STATIONMSG_ISREAD_YES= 1;
    
    public final static String LOGIN_PAGE="redirect:https://passport.meiliwan.com/user/dPlogin";

    /** 用户收货地址最大条数  */
    public final static int USER_ADDRESS_MAX_NUM = 10;
    /** 退货货取消的天数 */
    public final static int RET_ORDER_SYSTEM_CANCEL = 20;
    
    public final static byte USER_ADDRESS_IS_DEFAULT_YES = 1;
    public final static byte USER_ADDRESS_IS_DEFAULT_NO = 0;

    /**资讯活动ID*/
    public final static int INFO_CONTENT_ACT_ID = 4;

	public static final Short COMMENT_VISIBLE = 1;

    /** 商品业务库存日志相关 */
    //表示商品库存的增加
    public static final Short STOCK_LOG_ADD = 1;
    //表示商品库存的减少
    public static final Short STOCK_LOG_SUB = -1;

    /** 标示评论为虚假评论*/
    public final static String COMMENT_ORDERID="0000000000";

    /** 用户类型 */
    public final static String USER_ADMIN = "admin";
    public final static String USER_FRONT = "user";

    //用1来表示是否是ajax请求
    public static final String IS_AJAX = "1";
}
