package com.meiliwan.emall.commons.web;

/**
 * Created with IntelliJ IDEA.
 * User: yuxiong
 * Date: 13-8-21
 * Time: 下午4:13
 * To change this template use File | Settings | File Templates.
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(XssHttpServletRequestWrapper.class);

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values==null)  {
            return null;
        }

        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null){
            return null;
        }
        return cleanXSS(value);
    }

    private String cleanXSS(String value) {
        logger.debug("================================================PreValue:"+value);
        //使用Joup白名单过滤HTML标签
        value = TextUtil.cleanHTML(value);
        //过滤可能的SQL注入
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        logger.debug("================================================cleanXSS.Value:"+value);
        return value;
    }

}
