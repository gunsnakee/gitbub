package com.meiliwan.emall.commons.web;

import com.google.gson.Gson;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 *  JsonP 跨域数据访问工具类
 * Created by jiawu.wu on 13-7-3.
 */
public class JsonpUtil {


    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(JsonpUtil.class);


    private JsonpUtil(){

    }


    /**
     * 写Json
     * @param obj
     * @param request
     * @param response
     */
    public static void writeJsonByObj(Object obj,HttpServletRequest request, HttpServletResponse response) {
        try {
            String callBack= ServletRequestUtils.getStringParameter(request, "callback");
            response.setHeader("Content-Language", "zh-cn");
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            response.getWriter().write(callBack+"("+ new Gson().toJson(obj)+")");
        } catch (IOException e) {
            LOGGER.error(e, e.getMessage(), WebUtils.getIpAddr(request));
        }catch (ServletRequestBindingException e){
            LOGGER.error(e, e.getMessage(), WebUtils.getIpAddr(request));
        }
    }
}
