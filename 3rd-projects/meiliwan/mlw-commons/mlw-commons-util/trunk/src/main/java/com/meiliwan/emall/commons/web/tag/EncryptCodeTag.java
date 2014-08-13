package com.meiliwan.emall.commons.web.tag;

/**
 * Created by yuxiong on 13-6-20.
 */

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.CookieSessionUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.commons.web.validate.ValidateFormUtil;
import com.meiliwan.emall.commons.util.RandomCode;
import com.meiliwan.emall.commons.web.validate.FormConstans;
import com.meiliwan.emall.commons.web.validate.ValidateItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class EncryptCodeTag extends TagSupport
{
    private static final long serialVersionUID = 1L;
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(EncryptCodeTag.class);
    private String code;
    private String session;
    private String noPassUrl;

    public String getNoPassUrl() {
        return noPassUrl;
    }

    public void setNoPassUrl(String noPassUrl) {
        this.noPassUrl = noPassUrl;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int doStartTag()
            throws JspException
    {
        if (StringUtil.checkNull(this.session) && StringUtil.checkNull(this.code)) {
            return 0;
        }

        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();

        String sessValue = null;
        String enCode = null;
        try {
            sessValue = CookieSessionUtil.getSession(request, response).getAttribute(session);
        } catch (JedisClientException e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("sessValue", sessValue);
            LOG.error(e, map, WebUtils.getIpAddr(request));
        }

        String randCode = RandomCode.randomStrCode(4);
        //将页面传入的session+code和随机码一起加密得到enCode
        enCode = StringUtil.checkNull(sessValue)?"":sessValue;
        Object codeObj = request.getAttribute(code);

        if (codeObj instanceof String){
            code = codeObj.toString();
        }else if(codeObj instanceof ValidateItem){
            code = codeObj.toString();
        }

        enCode += StringUtil.checkNull(code)?"":code;
        enCode = ValidateFormUtil.getEncryptCode(enCode, randCode);

        StringBuffer inputString = new StringBuffer();
        inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_ENCRYPT_CODE).append("\" value=\"").append(enCode).append("\"/>");
        inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_VALI_NOPASS_URL).append("\" value=\"").append(StringUtil.checkNull(this.noPassUrl)?"":noPassUrl).append("\"/>");
        try {
            this.pageContext.getOut().write(inputString.toString());
        } catch (IOException e) {
            throw new JspTagException(e);
        }
        //将随机码写入session
        try {
            CookieSessionUtil.getSession(request, response).setAttribute(FormConstans.SESSION_RAND_CODE, randCode);
        } catch (JedisClientException e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("enCode", enCode);
            LOG.error(e, map, WebUtils.getIpAddr(request));
        }
        //httpSession.setAttribute(FormConstans.SESSION_RAND_CODE, randCode);
        return 1;
    }

}
