package com.meiliwan.emall.union.cps.cookie;

import javax.servlet.http.HttpServletRequest;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.cps.CPSHandler;

/**
 * 二维码链接
 * @author lsf
 *
 */
public class TwoDimCodeHandler extends CPSHandler{
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(TwoDimCodeHandler.class);

	@Override
	public String getMcps(HttpServletRequest request) {
//		return request.getParameter("s") + "." + request.getParameter("n");
		return request.getParameter("s");
	}

	@Override
	public String getCPSType() {
		return "2wCode";
	}

	@Override
	public String getTargetUrl(HttpServletRequest request) {
		String targetUrl = "http://www.meiliwan.com";
		try {
			targetUrl = ConfigOnZk.getInstance().getValue("union-web/2wCode.properties", getMcps(request));
		} catch (BaseException e) {
			LOG.error(e, "", WebUtils.getIpAddr(request));
		}
		return targetUrl;
	}
}
