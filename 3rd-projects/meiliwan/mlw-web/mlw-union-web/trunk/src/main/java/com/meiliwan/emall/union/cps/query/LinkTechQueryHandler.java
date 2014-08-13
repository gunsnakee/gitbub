package com.meiliwan.emall.union.cps.query;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.cps.CPSQueryHandler;

@Deprecated
public class LinkTechQueryHandler implements CPSQueryHandler{
	
	private final static MLWLogger LOGGER = MLWLoggerFactory.getLogger(LinkTechQueryHandler.class.getName());
	private final static String ZK_CONFIG_PATH ="commons/system.properties";

	@Override
	public boolean validation(HttpServletRequest request) {
		
		String clientIp = WebUtils.getClientIp(request);
		
		try {
			String allowIpString = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH, "linkTech.allowIP","10.249.9.243");
			if(StringUtils.isNotBlank(clientIp) && allowIpString.contains(clientIp.split(",")[0])){
				return true;
			}
				
		} catch (BaseException e) {
			LOGGER.error(e, "get config param error from zookeeper", clientIp);
		}
		return false;
	}

	@Override
	public String query(HttpServletRequest request,
			HttpServletResponse response) {
//		return unionOrderService.queryForLinkTech(yyyymmdd, LINK_TECH);
		return null;
	}

}
