package com.meiliwan.emall.commons.web;

import com.google.gson.Gson;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 *   PrintWriter 工具类
 * Created by jiawu.wu on 13-6-28.
 */
public class PrintWriterUtil {


    private static final MLWLogger LOGGER = MLWLoggerFactory.getLogger(PrintWriterUtil.class);

    private PrintWriterUtil(){

    }

    /**
     * 用PrintWriter写一个String
     * @param response
     * @param str
     */
    public static void writeMsg(HttpServletResponse response,String str){
        PrintWriter out = null;
        try {
            response.setHeader("Content-Language", "zh-cn");
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("application/json; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            out = response.getWriter();
            out.write(str);

        } catch (IOException e) {
            LOGGER.error(e,"writeMsg(response,str): {str:"+str+"}",null);
        } finally {
            if(out!=null){
                out.flush();
                out.close();
            }
        }
    }

    public static void writeMsg(HttpServletResponse response,Map<String,Object> map){
        if(map==null){
            return;
        }
       writeMsg(response,new Gson().toJson(map));
    }

}
