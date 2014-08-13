package com.meiliwan.emall.base.constant;

/**
 * Base 模块常量定义类
 * 
 * @author yiyou.luo
 * 
 */
public class Constants {
	// Base 模块常量定义，地域级别 国家=0
	public static final int AREA_GRADE_COUNTRY = 0;

	// Base 模块常量定义，地域级别 省/直辖市=1
	public static final int AREA_GRADE_PROVINCE = 1;

	// Base 模块常量定义，地域级别 市=2
	public static final int AREA_GRADE_CITY = 2;

	// Base 模块常量定义，地域级别 区/县=3
	public static final int AREA_GRADE_COUNTY = 3;

	// Base 模块常量定义，地域级别 街道/乡镇=4
	public static final int AREA_GRADE_TOWN = 4;

	// 美丽湾物流公司,默认
	public static final int DEFAULT_TRANS_ID = 999;

	public static final Integer COD_VALID = 1;
	public static final Integer COD_INVALID = 0;

	// 状态删除
	public static final short STATE_DEL = -1;
	// 状态未删除
	public static final short STATE_NOT_DEL = 0;
	// 状态有效
	public static final short STATE_VALID = 1;
	// 状态无效
	public static final short STATE_UNVALID = 0;
	public static final short IS_NOT_DEFAULT = 0;
	public static final short IS_DEFAULT = 1;

	public final static int BASE_CASH_ON_DELIVER_YES = 1;
	public final static int BASE_CASH_ON_DELIVER_NO = 0;

	// 地域编码 世界编码
	public final static String BASE_AREA_CODE_WORLD = "10";
	// 地域编码 中国编码
	public final static String BASE_AREA_CODE_CHINA = "1000";

	// 验证码
	public static final String VALIDATE_CODE = "validateCode";

	// 用户ID
	public static final String UID = "uid";

	/** 支持满包邮 **/
	public static final Integer FULL_SUPPORT = 1;

	/** 不支持满包邮 **/
	public static final Integer FULL_SUPPORT_NOT = 0;

	/** 全国 **/
	public static final String CHINA = "1000";

	// 美丽湾发件人显示名称
	public static final String MLW_EMAIL_SHOWNAME = "美丽湾商城";

	/** 站内信用户是否已读-未读 */
	public final static Short USER_STATIONMSG_ISREAD_NO = 0;
	/** 站内信用户是否已读- 已读 */
	public final static Short USER_STATIONMSG_ISREAD_YES = 1;
	/** 我的站内信列表条数,翻页 */
	public final static int PAGER_STATIONMSG_COUNT = 10;

	public static final String MSG_WHITELIST = "commons/msgWhiteList.properties";// 短信白名单
	// 短信成功发送后的返回码
	public static final int MOBILE_SUCESS_CODE = 605;

	/** 搜索结果和三级类目结果显示商品条数,翻页 */
	public final static int PAGER_PRODUCT_COUNT = 32;
	public final static String PRODUCT_SEARCH_NAME = "product";

	/** 商品评价列表条数,翻页 */
	public final static int PAGER_COMMENT_COUNT = 10;
	/** 我的收藏列表条数,翻页 */
	public final static int PAGER_COLLECT_COUNT = 10;
	/** 我的积分明细列表条数,翻页 */
	public final static int PAGER_INTEGRAL_DETAIL_COUNT = 15;
	/** 我的咨询列表条数,翻页 */
	public final static int PAGER_CONSULT_COUNT = 10;
	/** 我的投诉列表条数,翻页 */
	public final static int PAGER_COMPLAINTS_COUNT = 15;

	/** 是否评论_未 */
	public final static short COMMENT_IS_COMMENT_NO = -1;
	/** 是否评论_已 */
	public final static short COMMENT_IS_COMMENT_YES = 0;

	/** 用户收货地址最大条数 */
	public final static int USER_ADDRESS_MAX_NUM = 20;
	public final static byte USER_ADDRESS_IS_DEFAULT_YES = 1;
	public final static byte USER_ADDRESS_IS_DEFAULT_NO = 0;

	/** 投诉未处理 */
	public final static short COMPLAINTS_NO_HANDLE = 0;
}
