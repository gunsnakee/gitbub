package com.meiliwan.emall.bkstage.web.controller.sp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.sp2.bean.LotteryAddress;
import com.meiliwan.emall.sp2.bean.LotteryResult;
import com.meiliwan.emall.sp2.bean.LotterySetting;
import com.meiliwan.emall.sp2.client.LotteryAddressClient;
import com.meiliwan.emall.sp2.client.LotteryResultClient;
import com.meiliwan.emall.sp2.client.LotterySettingClient;

@Controller
@RequestMapping(value = "/sp/lottery")
public class LotterySettingController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 查询列表
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/setting")
    public String setting(HttpServletRequest request, Model model) {
    		try {
			List<LotterySetting> list = LotterySettingClient.allList();
			int total=0;
			for (LotterySetting setting : list) {
				total+=setting.getPossibility();
			}
			for (LotterySetting setting : list) {
				setting.setRate( (float)setting.getPossibility()/total*100);
			}
			model.addAttribute("list", list);
		} catch (ServiceException e) {
			logger.error(e, null, WebUtils.getIpAddr(request));
		}
        return "/sp/activity/lotterySetting";
    }

    @RequestMapping("/add")
    public void add(HttpServletRequest request,HttpServletResponse response,Model model) {
    		
    		try {
			LotterySetting setting = getSetting(request);
			LotterySettingClient.insertUpdate(setting);//
			StageHelper.dwzSuccessForward("保存成功", "10340", "/sp/lottery/setting", response);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			logger.error(e, null, WebUtils.getIpAddr(request));
			StageHelper.dwzFailClose("保存失败", response);
		}
    }


    @RequestMapping("/addView")
    public String addView(HttpServletRequest request,HttpServletResponse response,Model model) {
    		int id = ServletRequestUtils.getIntParameter(request, "id",0);
    		LotterySetting setting = LotterySettingClient.findById(id);
    		model.addAttribute("setting", setting);
    		return "/sp/activity/lotterySettingAdd";
    }
    
    @RequestMapping("/result")
    public String result(HttpServletRequest request,HttpServletResponse response,Model model) {
    		PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
    		PagerControl<LotteryResult> page = LotteryResultClient.page(pageInfo);
    		model.addAttribute("pc", page);
    		return "/sp/activity/lotteryResult";
    }
    
   
    @RequestMapping("/getAddress")
    public String getAddress(HttpServletRequest request,HttpServletResponse response,Model model) {
    		int id = ServletRequestUtils.getIntParameter(request, "id",0);
    		LotteryAddress addr = LotteryAddressClient.getById(id);
    		model.addAttribute("addr", addr);
    		return "/sp/activity/lotteryAddress";
    }
    
	private LotterySetting getSetting(HttpServletRequest request) {
		int id = ServletRequestUtils.getIntParameter(request, "id",0);
		String lotteryName = ServletRequestUtils.getStringParameter(request, "lotteryName","");
		String lotteryProduct = ServletRequestUtils.getStringParameter(request, "lotteryProduct","");
		int total = ServletRequestUtils.getIntParameter(request, "total",0);
		int possibility = ServletRequestUtils.getIntParameter(request, "possibility",0);
		boolean type = ServletRequestUtils.getBooleanParameter(request, "type", true);
		LotterySetting setting = new LotterySetting();
		setting.setId(id);
		setting.setLotteryName(lotteryName);
		setting.setLotteryProduct(lotteryProduct);
		setting.setTotal(total);
		setting.setPossibility(possibility);
		setting.setType(type);
		return setting;
	}
}
