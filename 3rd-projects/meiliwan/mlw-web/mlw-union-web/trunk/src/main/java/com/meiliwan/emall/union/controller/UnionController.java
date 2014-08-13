package com.meiliwan.emall.union.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.SpringContextUtil;
import com.meiliwan.emall.commons.web.ResponseUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.union.cps.CPSHandler;
import com.meiliwan.emall.union.cps.CPSQueryHandler;
import com.meiliwan.emall.union.cps.cookie.CaiBeiHandler;
import com.meiliwan.emall.union.cps.cookie.ChanetHandler;
import com.meiliwan.emall.union.cps.cookie.LinkTechHandler;
import com.meiliwan.emall.union.cps.cookie.TwoDimCodeHandler;
import com.meiliwan.emall.union.service.UnionOrderService;

/**
 * 
 * @author lsf
 * 
 */
@Controller
@RequestMapping("/cps")
public class UnionController {

	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(UnionController.class);
	private final static String CPS_NAME = "_mcps";
	private final static String ZK_CONFIG_PATH = "commons/system.properties";
	private final static String LINK_TECH = "linkTech";
	
	private static Map<String, CPSHandler> cpsHandlers =  new HashMap<String, CPSHandler>();
	
	static{
		//cookie handler
		cpsHandlers.put("channet", new ChanetHandler());
		cpsHandlers.put("linkTech", new LinkTechHandler());
		cpsHandlers.put("caibei", new CaiBeiHandler());
		cpsHandlers.put("2wCode", new TwoDimCodeHandler());
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/{company}")
	public String jumpByLinkTech(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String company) {
		String url = request.getParameter("url");
		
		CPSHandler cpsHandler = cpsHandlers.get(company);
		if (cpsHandler != null) {
			if(!cpsHandler.isStoped()){
				String _mcps = WebUtils.getCookieValue(request, CPS_NAME);
				String currMcps = cpsHandler.getMcps(request);
				boolean needUpdateCookie = cpsHandler.needUpdateCookie(request, _mcps, currMcps);
				if(needUpdateCookie){
					Cookie cookie = new Cookie(CPS_NAME, cpsHandler.getCPSType() + "|" + currMcps);
					cookie.setPath("/");
					cookie.setDomain(".meiliwan.com");
					cookie.setMaxAge(60 * 60 * 24 * cpsHandler.getMaxAge());
					response.addCookie(cookie);
				}
				
				try{
					cpsHandler.doOther(request, response);
				}catch(Exception ex){
					LOG.error(ex, "do other for company " + company + " error!", WebUtils.getClientIp(request));
				}
			}
			
			url = StringUtils.isBlank(cpsHandler.getTargetUrl(request)) ? url : cpsHandler.getTargetUrl(request);
		}
		
		request.setAttribute("url", StringUtils.isBlank(url) ? "http://www.meiliwan.com" : url);
		return "cps/linkTech";
	}

	
	@RequestMapping("/queryForLinkTech")
	public void queryForLinkTech(HttpServletRequest request, HttpServletResponse response,  String yyyymmdd){
		String clientIp = WebUtils.getIpAddr(request);		
//		LOG.info("query request", "company:linkTech", clientIp);
		try {
			String allowIP = ConfigOnZk.getInstance().getValue(ZK_CONFIG_PATH, "linkTech.allowIP","10.249.9.243");
			LOG.debug("clientIP:" + clientIp + ", allowIP:" + allowIP);
			if(StringUtils.isNotBlank(clientIp) && allowIP.contains(clientIp.split(",")[0])){
				UnionOrderService unionOrderService = (UnionOrderService)SpringContextUtil.getBean("unionOrderService");
				String orderInfos = unionOrderService.queryForLinkTech(yyyymmdd, LINK_TECH);
				
				response.setContentType("text/plain; charset=utf-8");
				response.getWriter().write(orderInfos);
//				LOG.info("write result", orderInfos, clientIp);
			}else{
				ResponseUtil.writeJsonByObj("ip not allowed:" + clientIp, request, response);
			}
		} catch (BaseException e) {
			LOG.error(e, "get config param from zk failure", WebUtils.getIpAddr(request));
		} catch (IOException e) {
			LOG.error(e, "write result error", WebUtils.getIpAddr(request));
		}
	}
	
	@RequestMapping("/query/{company}")
	public void query(HttpServletRequest request, HttpServletResponse response, @PathVariable String company) {
//		LOG.info("query request", "company:" + company, WebUtils.getIpAddr(request));
		if (StringUtils.isBlank(company)) {
			try {
				response.getWriter().write("request params error.");
			} catch (IOException e) {
				LOG.error(e, "request params error.", WebUtils.getIpAddr(request));
			};
			return;
		}
		String handlerBean = company+"QueryHandler";
		CPSQueryHandler cpsQueryHandler = null;
		try{
		   cpsQueryHandler = (CPSQueryHandler)SpringContextUtil.getBean(handlerBean);
		}catch(NoSuchBeanDefinitionException e){
		   LOG.warn(" no such cps company", "query handler bean name: "+ handlerBean, WebUtils.getIpAddr(request));
		}
		if (cpsQueryHandler == null) {
			try {
				response.getWriter().write("no such company.");
			} catch (IOException e) {
				LOG.error(e, "no such company", WebUtils.getClientIp(request));
			};
			return;
		}
		
		if (!cpsQueryHandler.validation(request)) {
			try {
				response.getWriter().write("your url rule in request  not allow access our network.");
			} catch (IOException e) {
				LOG.error(e, "response error", WebUtils.getClientIp(request));
			};
			return;
		}
		
		String resut  = cpsQueryHandler.query(request, response);
		response.setContentType("text/plain; charset=utf-8");
		try {
			if (resut == null) {
				resut = "results is empty,check your request params!";
			}
			response.getWriter().write(resut);
//			LOG.info("write result", resut,  WebUtils.getIpAddr(request));
		} catch (IOException e) {
			LOG.error(e, "response error", WebUtils.getClientIp(request));
		}
	}

}
