package com.meiliwan.emall.commons.web;


import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.CodeBabyUtils;
import com.meiliwan.emall.commons.web.validate.FormConstans;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 13-6-8.
 */
public class WebUtils {

    public final static String COOKIE_ID_KEY = "_mc";

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(WebUtils.class);

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        Assert.notNull(request);
        Assert.hasText(cookieName);
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName() != null && cookie.getName().equals(cookieName)) {
                    //特殊字符串处理
                    String rValue = CodeBabyUtils.decode(cookie.getValue());
                    return rValue;
                }
            }
        }
        return null;
    }

    public static void setCookieValue(HttpServletResponse response, String cookieName, String value, String cookiePath, int cookieAge) {
        logger.debug("1 setCookieValue:  " + value);
        System.out.println("1 setCookieValue:  " + value);
        Assert.notNull(response);
        Assert.hasText(cookieName);
        Assert.hasText(value);
        //特殊字符串处理
        value = CodeBabyUtils.encode(value);
        Cookie cookie = new Cookie(cookieName, value);
        if (Strings.isNullOrEmpty(cookiePath)) {
            cookie.setPath("/");
        } else {
            cookie.setPath(cookiePath);
        }
        cookie.setMaxAge(cookieAge);
        logger.debug("2 setCookieValue:  " + value);
        System.out.println("2 setCookieValue:  " + value);
        response.addCookie(cookie);
    }

    public static void setCookieValue(HttpServletResponse response, String cookieName, String value, String cookiePath, String domain, int cookieAge) {
        Assert.notNull(response);
        Assert.hasText(cookieName);
        Assert.hasText(value);
        //特殊字符串处理
        value = CodeBabyUtils.encode(value);
        Cookie cookie = new Cookie(cookieName, value);
        if (Strings.isNullOrEmpty(cookiePath)) {
            cookie.setPath("/");
        } else {
            cookie.setPath(cookiePath);
        }
        cookie.setDomain(domain);
        cookie.setMaxAge(cookieAge);
        response.addCookie(cookie);
    }

    /**
     * 获取某个url的domain
     *
     * @param url
     * @return
     */
    public static String getHost(String url) {
        try {
            String s = url;
            int pos = s.indexOf("//");
            if (pos > -1) s = s.substring(pos + 2);
            pos = s.indexOf("/");
            if (pos > -1) s = s.substring(0, pos);
            if (s.trim().length() == 0) s = "";
            return s;
        } catch (Exception exp) {
            return "";
        }
    }

    /**
     *  删除Cookie，使cookie马上过期(.meiliwan.com域)
     * @param request
     * @param response
     * @param path  cookie存放的路径
     * @param cookieNames
     * @return
     */
    public static String deleteCookie(HttpServletRequest request, HttpServletResponse response, String path, String... cookieNames) {
        Assert.notNull(request);

        path = (path == null || path.equals("")) ? "/" : path;

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                for (String cookieName : cookieNames) {
                    Assert.hasText(cookieName);
                    if (cookie.getName().equals(cookieName)) {
                        cookie.setPath(path);
                        cookie.setMaxAge(0);
                        cookie.setDomain(".meiliwan.com");
                        response.addCookie(cookie);
                        break;
                    }
                }

            }
        }
        return null;
    }

    /**
     * 删除Cookie，使cookie马上过期
     * @param request
     * @param response
     * @param path cookie存放的路径
     * @param domain cookie存放的域
     * @param cookieNames
     * @return
     */
    public static String deleteCookieDomain(HttpServletRequest request, HttpServletResponse response, String path, String domain,String... cookieNames) {
        Assert.notNull(request);

        path = (path == null || path.equals("")) ? "/" : path;

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                for (String cookieName : cookieNames) {
                    Assert.hasText(cookieName);
                    if (cookie.getName().equals(cookieName)) {
                        cookie.setPath(path);
                        cookie.setMaxAge(0);
                        cookie.setDomain(domain);
                        response.addCookie(cookie);
                        break;
                    }
                }

            }
        }
        return null;
    }

    public static String deleteAllDomainCookie(HttpServletRequest request, HttpServletResponse response, String domain){
        Assert.notNull(request);
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if( cookie.getDomain()!=null && cookie.getDomain().equals(domain)){
                    cookie.setPath(cookie.getPath());
                    cookie.setMaxAge(0);
                    cookie.setDomain(cookie.getDomain());
                    response.addCookie(cookie);
                }

            }
        }
        return null;
    }

    public static void writeJsonByObj(Object obj, HttpServletResponse response) {
        try {
            response.setHeader("Content-Language", "zh-cn");
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String json = new Gson().toJson(obj);
            logger.debug("JSON:  " + json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeJsonByObj(Object obj, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setHeader("Content-Language", "zh-cn");
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            Map<String, Object> map = new HashMap();
            map.put(FormConstans.REQUEST_FORM_TOKEN, request.getAttribute(FormConstans.REQUEST_FORM_TOKEN));
            map.put("data", obj);
            String json = new Gson().toJson(map);
            logger.debug("JSON:  " + json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeJsonPByObj(Object obj, HttpServletRequest request, HttpServletResponse response) {
        try {
            String callBack = ServletRequestUtils.getStringParameter(request, "callback");
            response.setHeader("Content-Language", "zh-cn");
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(callBack + "(" + new Gson().toJson(obj) + ")");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
    }

    public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Cdn-Src-Ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

    /**
     * 获取当前request完整URL
     * @param request
     * @return
     */
    public static String getCurrentURL(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        if (request.getQueryString() != null) {
            url.append('?');
            url.append(request.getQueryString());
        }
        return url.toString();
    }

}
