package com.meiliwan.emall.union.cps.cookie;

import javax.servlet.http.HttpServletRequest;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.union.bean.UnionConstant;
import com.meiliwan.emall.union.cps.CPSHandler;

public class LinkTechHandler extends CPSHandler {
	
	private final static MLWLogger logger = MLWLoggerFactory.getLogger(LinkTechHandler.class.getName());
	
	@Override
	public boolean needUpdateCookie(HttpServletRequest request, String _mcps, String currMcps) {
		String zkMid = "";
		try {
			zkMid = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH, "linkTech.mId", "meiliwan");
		} catch (BaseException e) {
			logger.error(e, "get config from zookeeper failure!!", null);
		}
		return (super.needUpdateCookie(request, _mcps, currMcps) || _mcps.startsWith(UnionConstant.CAIBEI) ) && zkMid.equalsIgnoreCase(request.getParameter("m_id"));
	}
	
	@Override
	public String getMcps(HttpServletRequest request) {
		String a_id = request.getParameter("a_id"); 
		String c_id = request.getParameter("c_id"); 
		String l_id = request.getParameter("l_id"); 
		String l_type1 = request.getParameter("l_type1");
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(a_id).append("|").append(c_id).append("|").append(l_id).append("|").append(l_type1);
		
		return stringBuilder.toString();
	}
	
	@Override
	public String getCPSType() {
		return "linkTech";
	}
	
}
