package com.meiliwan.emall.commons.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;


/**
 * @author leo
 * 连接验证码服务器，并获取验证结果
 	如果url为http://imagecode.meiliwan.com/captcha/validate.do 验证通过时会立刻清除memory cache中的验证码，
 	再次验证将失效；
 	url为http://imagecode.meiliwan.com/captcha/ajaxValidate.do ，则不会清除cache,可以再次验证。
 	对于页面需要ajax验证，然后提交表单时，服务器需要再次验证的需求
	该参数值需要注意。仅在服务器端验证一次的需求,url参数必须使用前者
	
 * <pre>
 * Sample use:
 * 		String sid = CookieSessionUtil.getSession(request, response).getSessionId();
 * 		CaptchaUtil.validate("http://imagecode.meiliwan.com/captcha/validate", "12345", sid,100);
 * Sample use for ajax request:
 * 		CaptchaUtil.validate("http://imagecode.meiliwan.com/captcha/ajaxValidate", "12345", sid,100);
 * </pre>
 */
public final class CaptchaUtil {
	
	//图片验证码验证超时时间，单位ms
     static final int TIME_OUT = 20000;

    //图片验证URL
     static final String  CAPTCHA_URL="http://imagecode.meiliwan.com/captcha/validate";

    //图片验证URL for ajax
     static final String  CAPTCHA_URL_AJAX =  "http://imagecode.meiliwan.com/captcha/ajaxValidate";

	
	 static final MLWLogger logger = MLWLoggerFactory.getLogger(CaptchaUtil.class);
	
    /**
     * 校验验证码
     * 参数url，形如 http://imagecode.meiliwan.com/captcha/validate.do
     * @param url Action 路径
     * @param code 用户输入的验证码字符
     * @param sid Cookie中_captcha_key值
     * @return Boolean true 验证通过
     * @throws Exception URL格式错误或网络不畅抛出此异常
     */
    public static boolean validate(String url, String code, String sid) throws Exception {
        return validate(url, code, sid, 0);
    }

    /**
     * 校验验证码
     *  	如果url为http://imagecode.meiliwan.com/captcha/validate 验证通过时会立刻清除memory cache中的验证码，再次验证将失效；
    		为http://imagecode.meiliwan.com/captcha/ajaxValidate ，则不会清除cache,可以再次验证。
    		对于页面需要ajax验证，然后提交表单时，服务器需要再次验证的需求
    		该参数值需要注意。仅在服务器端验证一次的需求,url参数必须使用前者;
     * @param url Action 路径
     * @param code 用户输入的验证码字符
     * @param sid Cookie中_captcha_key值
     * @param timeout 连接超时(millisends)
     * @return Boolean true 验证通过
     * @throws Exception URL格式错误或网络不畅抛出此异常
     */
    public static boolean validate(String url, String code, String sid, int timeout)
        throws Exception {
        if (url == null) {
            throw new NullPointerException("Parameter url is null");
        }
        if (code == null) {
            throw new NullPointerException("Parameter code is null");
        }
        if (sid == null) {
            throw new NullPointerException("Parameter sid is null");
        }
        url = url + "?code=" + code + "&sid=" + sid;
        URLConnection connection = null;

        try {
            connection = new URL(url).openConnection();
        } catch (MalformedURLException e) {
            logger.error(e, "url " + url, "imagecode.meiliwan.com");
            throw e;
        } catch (IOException e) {
            logger.error(e , "url " + url, "imagecode.meiliwan.com");
            throw e;
        }

        boolean result = false;
        if (connection != null) {
            if (timeout >= 0) {
                connection.setConnectTimeout(timeout);
            }
            InputStream in = null;
            try {
                connection.connect();
                in = connection.getInputStream();
                byte[] b = new byte[in.available()];
                in.read(b);
                result = Boolean.parseBoolean(new String(b));
            } catch (Exception e) {
            	logger.error(e, "url " + url, "imagecode.meiliwan.com");
                throw e;
            } finally {
                if (in != null) in.close();
            }
        }
        return result;
    }

    /**
     * 读取Cookie中_captcha_key值
     * @param request
     * @return String 
     */
    public static String readCaptchaCookie(HttpServletRequest request) {
        String key = "_captcha_key";
        String value = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie c = cookies[i];
                if (c.getName().equalsIgnoreCase(key)) {
                    value = c.getValue();
                    return value;
                }
            }
        }
        return value;
    }
    
    /**
     * 校验图片验证码
     *
     * @param request
     * @param response
     * @param code
     * @param ajax
     * @return
     */
    public static boolean validateImgCodeByType(HttpServletRequest request, HttpServletResponse response, String code, Boolean ajax) {
        if (request == null || response == null) {
            return false;
        }
        // 获取sessionID
        String sessionId = null;
        try {
            sessionId = CookieSessionUtil.getSession(request, response).getSessionId();
        } catch (JedisClientException e) {
            logger.error(e,"CookieSessionUtil.getSession(request, response).getSessionId()", WebUtils.getIpAddr(request));
            return false;
        }

        logger.debug("validateImgCode >>>>> sessionId = " + sessionId + " code = " + code);

        boolean success = false;
        //校验图片验证码

        try {
            success = CaptchaUtil.validate(ajax ? CAPTCHA_URL_AJAX : CAPTCHA_URL, code, sessionId, TIME_OUT);
        } catch (Exception e) {
            logger.error(e,"CaptchaUtil.validate(ajax ? CAPTCHA_URL_AJAX : CAPTCHA_URL, code, sessionId, UserCommon.TIME_OUT): {ajax:"+ajax+",CAPTCHA_URL_AJAX:"+ CAPTCHA_URL_AJAX+",CAPTCHA_URL:" + CAPTCHA_URL + ",code:" +code+",sessionId:"+sessionId+", TIME_OUT:"+ TIME_OUT+"}",WebUtils.getIpAddr(request));
        }


        logger.debug("validateImgCode >>>>> rs = " + success);

        return success;
    }

    /**
     * 验证图片验证码
     *
     * @param request
     * @param response
     * @return
     */
    public static boolean validateImgCode(HttpServletRequest request, HttpServletResponse response, String code) {
        return validateImgCodeByType(request, response, code, false);
    }
    
    /**
     * 验证图片验证码 ajax方式
     *
     * @param request
     * @param response
     * @param code
     * @return
     */
    public static boolean validateImgCodeAjax(HttpServletRequest request, HttpServletResponse response, String code) {
        return validateImgCodeByType(request, response, code, true);
    }
}
