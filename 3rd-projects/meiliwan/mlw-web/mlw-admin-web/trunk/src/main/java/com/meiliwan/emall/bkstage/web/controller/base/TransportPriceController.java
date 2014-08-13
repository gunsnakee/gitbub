package com.meiliwan.emall.bkstage.web.controller.base;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.meiliwan.emall.base.bean.BaseAreaManagement;
import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.base.bean.BaseTransportPrice;
import com.meiliwan.emall.base.client.BaseAreaServiceClient;
import com.meiliwan.emall.base.client.BaseTransportAreaPayClient;
import com.meiliwan.emall.base.client.StationFareClient;
import com.meiliwan.emall.base.client.TransportPriceClient;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.base.dto.TransportPriceDTO;
import com.meiliwan.emall.base.dto.TransportPriceTip;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;


/**
 * 物流区域价格
 * 
 * @author yinggao.zhuo
 * 
 */
@Controller
@RequestMapping(value = "/base/fare")
public class TransportPriceController {

	private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());
	
	/**
	 * 运费添加页面,需要查询省列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/priceSetting")
	public String home(HttpServletRequest request,Model model){
		String parentCode = ServletRequestUtils.getStringParameter(request, "parent_code",Constants.CHINA);
		try {
			List<BaseAreaManagement>  list = BaseAreaServiceClient.getAreasByParentCode(parentCode);
			BaseStationFare  stationFare = StationFareClient.getStationFare();
			
			model.addAttribute("tree", list);
			model.addAttribute("stationFare", stationFare);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, String.format("parentCode(%s)", parentCode), WebUtils.getIpAddr(request));
		}
		return "/base/trans/priceSetting";
		
	}
	
	
	
	/**
	 * 已对外提供
	 * 返回JSON
	 * 加required解决异常问题@RequestParam(value="parent_code", required=false)
	 * @param parent_code
	 * @return
	 */
	@RequestMapping(value = "/getByParentCode")
    public void getByParentCode(HttpServletRequest request,HttpServletResponse response){
		String parentCode = ServletRequestUtils.getStringParameter(request, "parentCode","");
		try {
			List<TransportPriceDTO>  list = new ArrayList<TransportPriceDTO>();
			
			if(StringUtils.isBlank(parentCode)){
				WebUtils.writeJsonByObj(list, response);
			}
			list = TransportPriceClient.getListByParentCode(parentCode);
			WebUtils.writeJsonByObj(list, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, parentCode, WebUtils.getIpAddr(request));
		}
	}

    
	/**
	 * 主要处理价格
	 * @param request
	 */
	@RequestMapping(value = "/delAddPrice")
	public void add(HttpServletRequest request,HttpServletResponse response) {
		
		//----------区域服务
		BaseTransportPrice bean = null;
		try {
			String[] areaIds = ServletRequestUtils.getStringParameters(request, "area_code");
			bean = setBean(request);
			TransportPriceClient.delAddPrice(areaIds,bean);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, bean, WebUtils.getIpAddr(request));
			
		}
		
	}

	


	
	private BaseTransportPrice setBean(HttpServletRequest request) {
		BaseTransportPrice bean = new BaseTransportPrice();
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		if(!StringUtils.isBlank(type)){
			bean.setType(type);
		}
		int unified = ServletRequestUtils.getIntParameter(request, "unified",0);
		bean.setUnified(Byte.parseByte(unified+""));
		
		float fixedPrice = ServletRequestUtils.getFloatParameter(request, "fixedPrice",0.0f);
		if(fixedPrice>=0){
			bean.setFixedPrice(new BigDecimal(fixedPrice));
		}
		
		float fullFreeLimit = ServletRequestUtils.getFloatParameter(request, "fullFreeLimit",0.0f);
		if(fullFreeLimit>=0){
			bean.setFullFreeLimit(new BigDecimal(fullFreeLimit));
		}
		float notFullFreePrice = ServletRequestUtils.getFloatParameter(request, "notFullFreePrice",0.0f);
		if(notFullFreePrice>=0){
			bean.setNotFullFreePrice(new BigDecimal(notFullFreePrice));
		}
		
		int predictTimeMin = ServletRequestUtils.getIntParameter(request, "predictTimeMin",0);
		int predictTimeMax = ServletRequestUtils.getIntParameter(request, "predictTimeMax",0);
		bean.setPredictTimeMin(predictTimeMin);
		bean.setPredictTimeMax(predictTimeMax);
		return bean;
	}



	/**
	 * 运费分页 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request,Model model) {

		PagerControl<TransportPriceTip> pc=null;
		try {
			PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
			BaseStationFare stationFare = StationFareClient.getStationFare();
			pc = TransportPriceClient.page(pageInfo);
			//设置全局
			List<TransportPriceTip> list = pc.getEntityList();
			for (TransportPriceTip transportPriceTip : list) {
				if(transportPriceTip.unifiedTrue()){
					transportPriceTip.setBaseStationFare(stationFare);
				}
			}
			model.addAttribute("pc", pc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e,pc,WebUtils.getIpAddr(request));
		}
		return "/base/trans/priceList";
	}

	/**
	 * 主要处理价格
	 * @param request
	 */
	@RequestMapping(value = "/del")
	public void del(HttpServletRequest request,HttpServletResponse response) {
		
		//----------区域服务
		String areaCode = null;
		try {
			areaCode = ServletRequestUtils.getStringParameter(request, "areaCode");
			TransportPriceClient.del(areaCode);
			StageHelper.dwzSuccessForward("操作成功", "", "", response);
		} catch (Exception e) {
			logger.error(e, areaCode, WebUtils.getIpAddr(request));
			StageHelper.dwzFailClose("操作失败。", response);
		}
		
	}
	
}
