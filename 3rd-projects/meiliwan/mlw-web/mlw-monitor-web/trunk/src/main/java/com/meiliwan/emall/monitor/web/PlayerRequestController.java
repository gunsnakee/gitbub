package com.meiliwan.emall.monitor.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.PlayerRequestVO;
import com.meiliwan.emall.monitor.service.PlayerRequestService;

@Controller
@RequestMapping(value = "/playerRequest")
public class PlayerRequestController {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(getClass());

	@Autowired
	private PlayerRequestService playerRequestService;
	@RequestMapping(value = "/list")
	public String requestCount(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		PageInfo pageInfo = null;
		try {
			PagerControl<PlayerRequestVO> pc = null;
			pageInfo = WebParamterUtil.getPageInfo(request);
			pc = playerRequestService.getPagePlayerRequest(pageInfo);
			model.addAttribute("pc", pc);
		} catch (Exception e) {
			logger.error(e, pageInfo, WebUtils.getIpAddr(request));
			return "error";
		}
		return "/playerRequest/list";
	}
	
	@RequestMapping(value = "/joinIn")
	public void joinIn(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		int pid = ServletRequestUtils.getIntParameter(request, "pid", 0);
		playerRequestService.add(pid);
	}
	
	@RequestMapping(value = "/cancel")
	public void cancel(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		int pid = ServletRequestUtils.getIntParameter(request, "pid", 0);
		playerRequestService.delete(pid);
	}
	
}
