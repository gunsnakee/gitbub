package com.meiliwan.emall.monitor.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.monitor.allocation.PlayerModule;
import com.meiliwan.emall.monitor.allocation.PlayerModuleKey;
import com.meiliwan.emall.monitor.service.PlayerModuleService;

@Controller
@RequestMapping(value = "/playerModule")
public class PlayerModuleController {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(getClass());
	
	private PlayerModuleService playerModuleService;
	@RequestMapping(value = "/add")
	public void add(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		String moduleName = ServletRequestUtils.getStringParameter(request, "moduleName","");
		int pid = ServletRequestUtils.getIntParameter(request, "pid",0);
		if(StringUtils.isEmpty(moduleName)||pid<=0){
			throw new BaseRuntimeException("the param is not valid");
		}																																																																																																																																												
		PlayerModule playerModule = new PlayerModule();
		playerModule.setModuleName(moduleName);
		playerModule.setPid(pid);
		playerModuleService.add(playerModule);
	}
	
	@RequestMapping(value = "/delete")
	public void delete(HttpServletRequest request, Model model,
			HttpServletResponse response) {

		String moduleName = ServletRequestUtils.getStringParameter(request, "moduleName","");
		int pid = ServletRequestUtils.getIntParameter(request, "pid",0);
		if(StringUtils.isEmpty(moduleName)||pid<=0){
			throw new BaseRuntimeException("the param is not valid");
		}
		PlayerModuleKey playerModuleKey = new PlayerModuleKey();
		playerModuleKey.setModuleName(moduleName);
		playerModuleKey.setPid(pid);
		playerModuleService.delete(playerModuleKey);
	}
	
	
	@RequestMapping(value = "/modulePlayerSetting")
	public String logPlayerList(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		
		int pid = ServletRequestUtils.getIntParameter(request, "pid",0);
		if(pid<=0){
			throw new BaseRuntimeException("the param is not valid");
		}
		List<PlayerModule> modules = playerModuleService.getByPlayerId(pid);
		
		model.addAttribute("modules", modules);
		model.addAttribute("pid", pid);
		
		return "/allocation/modulePlayerSetting";
	}
	
}
