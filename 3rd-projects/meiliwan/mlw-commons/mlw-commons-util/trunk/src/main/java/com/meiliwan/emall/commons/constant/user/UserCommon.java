package com.meiliwan.emall.commons.constant.user;

/**
 * 用户模块公共的  属性
 * Created by jiawu.wu on 13-6-27.
 */
public class UserCommon {

    private UserCommon(){

    }

    //美丽湾发件人显示名称
    public static final String MLW_EMAIL_SHOWNAME = "美丽湾商城";


    public static final String DOMAIN=".meiliwan.com";
    public static final String CHARSET_ENCODE = "UTF-8";

    //真
    public static final String TRUE = "true";

    //假
    public static final String FALSE = "false";

    //用户
    public static final String USER = "passport";

    //用户额外信息
    public static final String USER_EXTRA = "extra";

    //钱包
    public static final String WALLET = "wallet";

    //成功标识
    public static final String SUCCESS_FLAG = "successFlag";

    //提示信息
    public static final String COMMON_MSG = "commonMsg";

    //提示信息关联的字段
    public static final String MSG_FIELD = "msgField";

    //referer前缀
    public static final String REFERER = "referer:";

    //referer URL
    public static final String REFERER_URL = "refererUrl";

    //referer前缀
    public static final String TARGET_URL = "targetUrl";

    //passport的cookie路径
    public static final String PASSPORT_COOKIE_PATH = "/";

    //用户ID
    public static final String UID = "uid";

    //用户名
    public static final String USER_NAME = "userName";

    //昵称
    public static final String NICK_NAME = "nickName";

    //登录类型
    public static final String LOGIN_TYPE = "loginType";

    //令牌字符串
    public static final String TOKEN_STR = "tokenStr";

    //登录名
    public static final String LOGIN_NAME = "loginName";

    //登陆密码
    public static final String LOGIN_PASSWD = "loginPasswd";

    //验证类型
    public static final String VALIDATE_TYPE   = "validateType";

    //验证码
    public static final String VALIDATE_CODE   = "validateCode";

    //要验证的值
    public static final String VALIDATE_VALUE   = "validateValue";

    //返回类型
    public static final String  RETURN_TYPE = "returnType";

    //是否已经登陆
    public static final String IS_LOGINED = "isLogined";

    //登陆链接常量名
    public static final String LOGIN_URL = "loginUrl";

    //登陆链接
    public static final String LOGIN_URL_VALUE = "https://passport.meiliwan.com/user/login";

    //登陆页面ftl
    public static final String LOGIN_URL_PAGE = "/user/login";

    //当前标签页
    public static final String CURR_ITEM="currItem";

    //用户正常状态
    public static final short NORMOL_STATE = 1;

    //用户黑名单状态
    public static final short BLACK_STATE = -1;

    //图片验证码验证超时时间，单位ms
    public static final int TIME_OUT = 20000;

    //图片验证URL
    public static final String  CAPTCHA_URL="http://imagecode.meiliwan.com/captcha/validate";

    //图片验证URL for ajax
    public static final String  CAPTCHA_URL_AJAX =  "http://imagecode.meiliwan.com/captcha/ajaxValidate";

    //邮箱验证URL
    public static final String EMAIL_VALIDATE_URL  = "https://passport.meiliwan.com/user/email/validate";

    //邮箱重置密码URL
    public static final String EMAIL_RESET_PASSWD_URL  = "https://passport.meiliwan.com/user/pwd/reset";

    //设置支付密码时邮箱验证URL
    public static final String PAYPASSWOD_EMAIL_VALIDATE_URL="https://passport.meiliwan.com/ucenter/ppwd/emailValidate";

    //验证身份URL的发送
    public static final String EMAIL_VALIDATE_IDENTITY_URL="https://passport.meiliwan.com/ucenter/safe/validate/email/submit";

    public static final String SESSION_ID = "sessionId";

    //错误代码
    public static final String ERR_CODE="errCode";

    //错误类型
    public static final String ERR_TYPE="errType";

    //找回密码时，往redis临时存放的uid
    public static final String FIND_PWD_UID="findPwdUid";

    //美丽湾首页
    public  static final String MLW_INDEX_URL = "http://www.meiliwan.com";

    //美丽湾用户中心首页
    public  static final String MLW_UCENTER_URL = "http://www.meiliwan.com/ucenter/index";

    //美丽湾passport首页
    public  static final String MLW_PASSPORT_INDEX = "https://passport.meiliwan.com";

    // 7天， cookie _mu  的过期时间
    public static final int MU_TIIME = 604800;


    /** 默认头像 */
    public static final String DEFAULT_HEAD_URI = "http://www.meiliwan.com/images/user_t.jpg";


    public static  enum OrderState{
         /** 等待付款 */
        S10{
             public String toString(){
                 return "10";
             }
         },
        /** 等待发货 */
        S30{
            public String toString(){
                return "30";
            }
        },
        /**
         * 等待确认收货
         */
        S40{
            public String toString(){
                return "40";
            }
        },
        /**
         * 交易成功
         */
        S60{
            public String toString(){
                return "60";
            }
        },
        /**
         * 已取消
         */
        S80{
            public String toString(){
                return "80";
            }
        }
    }

    /**
     *    登录类型
      */
    public static enum LoginType{
        keepUserName,
        keepLogin,
        foreignLogin
    }

    /**
     * 验证方式
     */
    public static  enum ValidateType {
        email, mobile
    }

    /**
     * 异常分类
     */
    public  static enum ExceptionType{
        /** 服务器端程序异常 */
        server_exceptioin,
        /** 用户数据异常 */
        user_exception
    }

    /**
     * 用户Bean的属性
     */
    public static enum PassportBean {
        uid,
        userName,
        nickName,
        email,
        mobile,
        password,
        headUri,
        createTime,
        emailActive,
        mobileActive,
        loginTime,
        loginEquip,
        loginIp,
        loginEquipId,
        state
    }

    public static enum ExtraBean{
        uid,
        sex,
        countryCode,
        countryName,
        provinceCode,
        provinceName,
        cityCode,
        cityName,
        areaCode,
        areaName,
        birthday,
        createTime
    }



}
