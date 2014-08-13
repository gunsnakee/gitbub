package com.meiliwan.emall.bkstage.web.controller.base;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.base.client.StationFareClient;
import com.meiliwan.emall.bkstage.web.StageHelper;

/**
 * 全局运费设置
 * 
 * @author yinggao.zhuo
 * 
 */
@Controller
@RequestMapping(value = "/base/stationFare")
public class StationFareController {

//	private final MLWLogger logger = MLWLoggerFactory
//			.getLogger(this.getClass());

	@RequestMapping(value = "/view")
	public String view(Model model,HttpServletRequest request, HttpServletResponse response) {

		BaseStationFare fare = StationFareClient.getStationFare();
		model.addAttribute("fare", fare);
		
		//增加页面
		return "/base/trans/stationFareSetting";
	}

	@RequestMapping(value = "/setting")
	public String setting(Model model,HttpServletRequest request, HttpServletResponse response) {

		
		BaseStationFare fare  = setBean(request);
		StationFareClient.update(fare);
		return StageHelper.dwzSuccessForward("操作成功", "",
				"/base/stationFare/view", response);
	}
	
	private BaseStationFare setBean(HttpServletRequest request){
		BaseStationFare fare = new BaseStationFare();
		String type = ServletRequestUtils.getStringParameter(request,"type", "");
		if(!StringUtils.isBlank(type)){
			fare.setType(type);
		}
		String message = ServletRequestUtils.getStringParameter(request,"message", "");
		if(!StringUtils.isBlank(message)){
			fare.setMessage(message);
		}
		String fixedPrice = ServletRequestUtils.getStringParameter(request,"fixedPrice", "");
		if(!StringUtils.isBlank(fixedPrice)){
			fare.setFixedPrice(new BigDecimal(fixedPrice));
		}
		String fullFreeLimit = ServletRequestUtils.getStringParameter(request,"fullFreeLimit", "");
		if(!StringUtils.isBlank(fullFreeLimit)){
			fare.setFullFreeLimit(new BigDecimal(fullFreeLimit));
		}
		String notFullFreePrice = ServletRequestUtils.getStringParameter(request,"notFullFreePrice", "");
		if(!StringUtils.isBlank(notFullFreePrice)){
			fare.setNotFullFreePrice(new BigDecimal(notFullFreePrice));
		}
		return fare;
	}

}
