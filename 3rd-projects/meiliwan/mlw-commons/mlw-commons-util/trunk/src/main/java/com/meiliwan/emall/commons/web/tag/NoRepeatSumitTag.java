package com.meiliwan.emall.commons.web.tag;

/**
 * Created by yuxiong on 13-6-20.
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

import com.meiliwan.emall.commons.exception.JedisClientException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NoRepeatSumitTag extends TagSupport
{
    private static final MLWLogger LOG = MLWLoggerFactory.getLogger(NoRepeatSumitTag.class);
    private static final long serialVersionUID = 1L;
    private String formName;
    private String usedIn;
    private enum UsedFor{FORM, BUTTON, URL};

    public String getUsedIn() {
        return usedIn;
    }

    public void setUsedIn(String usedIn) {
        this.usedIn = usedIn;
    }

    public int doStartTag()
            throws JspException
    {

        HttpServletRequest request = (HttpServletRequest)this.pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse)this.pageContext.getResponse();
        String uuidStr = null;
        try {

            uuidStr = ValidateFormUtil.getNoRepeatSubmitCode(request, response, formName);
            StringBuffer inputString = new StringBuffer();
            usedIn = StringUtil.checkNull(usedIn)?UsedFor.FORM.name():usedIn;
                //如果有url参数，则以url的get参数形式做输出，否则输出input hidden
                UsedFor used = UsedFor.valueOf(usedIn);
                switch (used){
                      case BUTTON:
                          if(!StringUtil.checkNull(this.formName)){
                              inputString.append(" ").append(FormConstans.REQUEST_CURR_FORMID).append("=\"").append(this.formName)
                                      .append("\"");
                          }
                          inputString.append(" ").append(FormConstans.REQUEST_FORM_TOKEN).append("=\"").append(uuidStr)
                                  .append("\"");
                          break;
                      case URL:
                          if(!StringUtil.checkNull(this.formName)){
                              inputString.append(FormConstans.REQUEST_CURR_FORMID).append("=").append(this.formName)
                                      .append("&");
                          }
                          inputString.append(FormConstans.REQUEST_FORM_TOKEN).append("=").append(uuidStr);
                          break;
                      default:
                          if(!StringUtil.checkNull(this.formName)){
                              inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_CURR_FORMID).append("\" id=\"").append(FormConstans.REQUEST_CURR_FORMID)
                                      .append("_").append(this.formName).append("\" value=\"").append(this.formName).append("\"/>");
                              inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_FORM_TOKEN).append("\" id=\"").append(FormConstans.REQUEST_FORM_TOKEN)
                                      .append("_").append(this.formName).append("\" value=\"").append(uuidStr).append("\"/>");
                          }else{
                              inputString.append("<input type=\"hidden\" name=\"").append(FormConstans.REQUEST_FORM_TOKEN).append("\" id=\"").append(FormConstans.REQUEST_FORM_TOKEN)
                                      .append("\" value=\"").append(uuidStr).append("\"/>");
                          }
                          break;
                }

            LOG.debug("NoRepeatSumitTag ---------------------------------------- uuidStr:"+uuidStr);
            this.pageContext.getOut().write(inputString.toString());

        } catch (Exception e) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("uuidStr", uuidStr);
            LOG.error(e, map, WebUtils.getIpAddr(request));
        }
        return 1;
    }



    public String getFormName() {
        return this.formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }
}
