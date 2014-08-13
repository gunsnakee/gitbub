package com.meiliwan.emall.monitor.web;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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
import com.meiliwan.emall.monitor.allocation.PlayerApp;
import com.meiliwan.emall.monitor.bean.RequestExcludeSetting;
import com.meiliwan.emall.monitor.client.AllocationClient;
import com.meiliwan.emall.monitor.service.MLWLogService;
import com.meiliwan.emall.monitor.service.PlayerAppService;
import com.meiliwan.emall.monitor.service.RequestExcludeSettingService;

@Controller
@RequestMapping(value = "/allocation")
public class AllocationController {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(AllocationController.class);
	@Autowired
	private MLWLogService mLWLogService;
	@Autowired
	private PlayerAppService playerAppService;
	@Autowired
	private RequestExcludeSettingService requestExcludeSettingService;
	
	@RequestMapping(value = "/requestSettingList")
	public String requestSettingList(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		LinkedHashMap<String, Integer> resumeTimeConfigList = AllocationClient.getResumeTimeConfigList();
		model.addAttribute("list", resumeTimeConfigList);
		
		return "/allocation/requestSettingList";
	}

	@RequestMapping(value = "/requestExcludeSettingAdd")
	public void requestExcludeSettingAdd(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		
		
		RequestExcludeSetting requestExcludeSetting = setRequestExcludeSetting(request);
		requestExcludeSettingService.add(requestExcludeSetting);

	}

	@RequestMapping(value = "/requestExcludeSettingDelete")
	public void requestExcludeSettingDelete(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		
		int rid = ServletRequestUtils.getIntParameter(request, "rid",0);
		if(rid<=0){
			throw new BaseRuntimeException("rid can not less than 0");
		}
		requestExcludeSettingService.delete(rid);

	}
	
	private RequestExcludeSetting setRequestExcludeSetting(HttpServletRequest request) {

		RequestExcludeSetting requestExcludeSetting = new RequestExcludeSetting();
		String name = ServletRequestUtils.getStringParameter(request, "name","");
		if(!StringUtil.checkNull(name)){
			requestExcludeSetting.setName(name);
		}
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		if(!StringUtil.checkNull(type)){
			requestExcludeSetting.setType(type);
		}
		int resumeTime = ServletRequestUtils.getIntParameter(request, "resumeTime",0);
		if(resumeTime>0){
			requestExcludeSetting.setResumeTime(resumeTime);
		}
		return requestExcludeSetting;
	}
	
	@RequestMapping(value = "/requestExcludeSettingAddView")
	public String requestExcludeSettingAddView(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		return "/allocation/requestExcludeSettingAdd";
	}
	
	@RequestMapping(value = "/requestExcludeSettingList")
	public String requestExcludeSettingList(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		PageInfo pageInfo = null;
		try {
			PagerControl<RequestExcludeSetting> pc = null;
			pageInfo = WebParamterUtil.getPageInfo(request);
			pc = requestExcludeSettingService.getPageRequestExcludeSetting(pageInfo);
			model.addAttribute("pc", pc);
		} catch (Exception e) {
			logger.error(e, pageInfo, WebUtils.getIpAddr(request));
			return "error";
		}
		return "/allocation/requestExcludeSettingList";
	}

	
	@RequestMapping(value = "/appPlayerSetting")
	public String logPlayerList(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		
		int playerId = ServletRequestUtils.getIntParameter(request, "pid",0);
		
		List<String> appstr = mLWLogService.getAllApplication();
		List<PlayerApp> list = playerAppService.getListByPlayerId(playerId);
		List<PlayerApp> apps = new ArrayList<PlayerApp>();
		for (String appName : appstr) {
			PlayerApp app = new PlayerApp();
			app.setAppName(appName);
			for (PlayerApp playerApp : list) {
				if(playerApp.getAppName()!=null&&playerApp.getAppName().equals(appName)){
					app.setPid(playerApp.getPid());
				}
			}
			apps.add(app);
		}
		model.addAttribute("apps", apps);
		model.addAttribute("pid", playerId);
		
		return "/allocation/appPlayerSetting";
	}

	
	
}
