package com.meiliwan.emall.commons.web.tag;

/**
 * Created by yuxiong on 13-6-20.
 */

import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.CookieSessionUtil;
import com.meiliwan.emall.commons.util.RandomCode;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.commons.web.validate.FormConstans;
import com.meiliwan.emall.commons.web.validate.ValidateFormUtil;
import com.meiliwan.emall.commons.web.validate.ValidateItem;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.HashMap;
import java.util.Map;


public class SecureCodeTag extends TagSupport
{
    private static final long serialVersionUID = 1L;
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(SecureCodeTag.class);
    private String code;
    private String noPassUrl;
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getNoPassUrl() {
        return noPassUrl;
    }

    public void setNoPassUrl(String noPassUrl) {
        this.noPassUrl = noPassUrl;
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

        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();

        String sessValue = null;
        String enCode = null;
        ShardJedisTool jedis = null;
        try {
            jedis = ShardJedisTool.getInstance();

            String randCode = null;

            //将页面传入的session+code和随机码一起加密得到enCode
            enCode = StringUtil.checkNull(sessValue)?"":sessValue;
            Object codeObj = request.getAttribute(code);

            if (codeObj instanceof String){
                code = codeObj.toString();
            }else if(codeObj instanceof ValidateItem){
                code = codeObj.toString();
            }
            //如果这里拿不到uid，那么则走seesion去拿用户id
            if(StringUtil.checkNull(sessionId)){
                sessionId = CookieSessionUtil.getSession(request, response).getAttribute(CookieSessionUtil.sessionField);
            }
            String cacheRandCode = jedis.get(JedisKey.secure$rand, sessionId);
            int randNum = 0;
            if(StringUtil.checkNull(cacheRandCode)){
                //先随机一个4-10的随机数，作为随机因子，再以此因子位randCode的随机长度
                randNum = RandomCode.randomNumRange(4, 10);
                randCode = RandomCode.randomStrCode(randNum);
                //将随机码写入redis
                jedis.set(JedisKey.secure$rand, sessionId, randCode);
            }else{
                randCode = cacheRandCode;
            }
            enCode += StringUtil.checkNull(code)?"":code;
            LOG.debug("enCode="+enCode+", sessionId="+sessionId+", randCode="+randCode+", randNum="+randNum);
            enCode = ValidateFormUtil.getEncryptCode(enCode, randCode);

            StringBuffer inputString = new StringBuffer();
            inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_SECURE_SIGN).append("\" value=\"").append(enCode).append("\"/>");
            if (!StringUtil.checkNull(this.noPassUrl)){
                inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_VALI_NOPASS_URL).append("\" value=\"").append(this.noPassUrl).append("\"/>");
            }

            this.pageContext.getOut().write(inputString.toString());

        } catch (Exception e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("enCode", enCode);
            LOG.error(e, map, WebUtils.getIpAddr(request));
        }
        return 1;
    }

}
