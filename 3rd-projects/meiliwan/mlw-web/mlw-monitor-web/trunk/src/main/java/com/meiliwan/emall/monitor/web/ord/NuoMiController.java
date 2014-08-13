package com.meiliwan.emall.monitor.web.ord;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.Config;
import com.meiliwan.emall.monitor.service.ConfigService;

@Controller
@RequestMapping(value = "/nuomi")
public class NuoMiController {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
	
	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "/list")
	public String requestCount(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		PageInfo pageInfo = null;
		try {
			PagerControl<Config> pc = null;
			pageInfo = WebParamterUtil.getPageInfo(request);
			pc = configService.getPagePlayer(pageInfo);
			model.addAttribute("pc", pc);
		} catch (Exception e) {
			logger.error(e, pageInfo, WebUtils.getIpAddr(request));
			return "error";
		}
		return "/nuomi/list";
	}
	
	
	
	@RequestMapping(value = "/add")
	public void addPlayer(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		Config player = null;
		try {
			player = setPlayer(request);
			configService.add(player);	
		} catch (Exception e) {
			logger.error(e, player, WebUtils.getIpAddr(request));
		}

	}

	@RequestMapping(value = "/delete")
	public void delete(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		int pid = ServletRequestUtils.getIntParameter(request, "id",0);
		if(pid<=0){
			throw new BaseRuntimeException("the pid can not less than 0");
		}
		configService.delete(pid);

	}
	
	@RequestMapping(value = "/addView")
	public String addView(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		
		return "/nuomi/add";
	}
	
	private Config setPlayer(HttpServletRequest request) {

		Config player = new Config();
		String name = ServletRequestUtils.getStringParameter(request, "name","");
		if(!StringUtil.checkNull(name)){
			player.setCode(name);
		}
		String mobile = ServletRequestUtils.getStringParameter(request, "value","");
		if(!StringUtil.checkNull(mobile)){
			player.setValue(mobile);
		}
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		if(!StringUtil.checkNull(type)){
			player.setType(type);
		}
		
		return player;
	}
	
}
