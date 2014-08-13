package com.meiliwan.emall.commons.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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
	
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(CaptchaUtil.class);
	
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
}
