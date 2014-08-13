package com.meiliwan.emall.dc.inbox.util;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: wenlepeng
 * Date: 13-11-13
 * Time: 下午1:18
 * To change this template use File | Settings | File Templates.
 */
public class WebUtil {
    private final static Logger log= Logger.getLogger(WebUtil.class);

    public static String getStringPara(HttpServletRequest request,String paramName){
        if(paramName != null && !"".equals(paramName)){
           return request.getParameter(paramName);
        }
        return null;
    }

    public static int getIntPara(HttpServletRequest request, String paramName) {
        int paramValue = 0;
        if(paramName != null && !"".equals(paramName)){
            try{
                paramValue = Integer.valueOf(request.getParameter(paramName));
            }catch (Exception e){
                log.error("获取参数转换为int 异常[paramNam:"+paramName+"]",e);
                paramValue = 0;
            }
        }
        return paramValue;
    }

    public static long getLongPara(HttpServletRequest request, String paramName) {
        long paramValue = 0;
        if(paramName != null && !"".equals(paramName)){
            try{
                paramValue = Long.valueOf(request.getParameter(paramName));
            }catch (Exception e){
                log.error("获取参数转换为long 异常[paramNam:"+paramName+"]",e);
                paramValue = 0;
            }
        }
        return paramValue;
    }
}
