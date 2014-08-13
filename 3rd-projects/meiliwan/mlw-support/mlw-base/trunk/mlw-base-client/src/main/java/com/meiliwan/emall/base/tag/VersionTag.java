package com.meiliwan.emall.base.tag;


import com.meiliwan.emall.base.client.BaseSysConfigServiceClient;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

/**
 * Created by wenlepeng on 13-10-13.
 */
public class VersionTag extends TagSupport {
    private final static MLWLogger logger = MLWLoggerFactory.getLogger(VersionTag.class);

    @Override
    public int doStartTag() throws JspException {

        String version = (String) this.pageContext.getAttribute("version");
        if(StringUtils.isEmpty(version)){
            version = BaseSysConfigServiceClient.getSysValueSysConfigByCode("version");
            this.pageContext.setAttribute("version",version);
        }

        try {
            this.pageContext.getOut().write(version);
        } catch (IOException e) {
            logger.error(e,"version="+version,"");
            return 0;
        }
        return 1;
    }
}
