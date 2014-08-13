package com.meiliwan.emall.account.view;

import com.meiliwan.emall.commons.bean.ErrorPageCode;
import com.meiliwan.emall.commons.exception.WebRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;

import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 
 * 处理referer的RedirectView
 * @author jiawu.wu
 *
 */
public class RefererRedirectView implements View {


    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) {
		String returnUrl = request.getHeader("referer");  
        if(returnUrl==null || returnUrl.trim().equals("")){  
            returnUrl = "/";  
        }
        try {
            response.sendRedirect(returnUrl);
        } catch (IOException e) {
            logger.error(e,e.getMessage()+" url:"+returnUrl, WebUtils.getIpAddr(request));
            throw new WebRuntimeException(ErrorPageCode.EXCEPTION_SYSTEM_DEFAULT);
        }
    }

}
