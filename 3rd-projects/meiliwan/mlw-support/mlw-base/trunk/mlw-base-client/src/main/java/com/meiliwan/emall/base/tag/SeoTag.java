package com.meiliwan.emall.base.tag;

import com.meiliwan.emall.base.client.BaseSysConfigServiceClient;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * Created by wenlepeng on 13-10-21.
 */
public class SeoTag extends TagSupport {

    private final static MLWLogger logger = MLWLoggerFactory.getLogger(VersionTag.class);

    private String seoName ;

    public String getSeoName() {
        return seoName;
    }

    public void setSeoName(String seoName) {
        this.seoName = seoName;
    }

    @Override
    public int doStartTag() throws JspException {

        String seoValue = (String) this.pageContext.getAttribute(this.seoName);
        if(StringUtils.isEmpty(seoValue)){
            seoValue = BaseSysConfigServiceClient.getSysValueSysConfigByCode(this.seoName);
            if(StringUtils.isBlank(seoValue))   return 1;
            this.pageContext.setAttribute(this.seoName,seoValue);
        }

        try {
            this.pageContext.getOut().write(seoValue);
        } catch (IOException e) {
            logger.error(e,"seo 关键字"+this.seoName,"");
            return 0;
        }
        return 1;
    }

}
