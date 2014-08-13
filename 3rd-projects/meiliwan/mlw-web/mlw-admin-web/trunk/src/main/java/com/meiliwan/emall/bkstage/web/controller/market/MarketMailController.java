package com.meiliwan.emall.bkstage.web.controller.market;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.dmdelivery.webservice.CampaignArrayType;
import com.dmdelivery.webservice.CampaignType;
import com.meiliwan.emall.bkstage.bean.MarketMail;
import com.meiliwan.emall.bkstage.service.MaketMailService;
import com.meiliwan.emall.bkstage.service.MarketMailThread;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.util.DmdeliverUtil;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;

/**
 * 
 * @author yinggao.zhuo
 */
@Controller
@RequestMapping("/market")
public class MarketMailController {

	private static final MLWLogger logger = MLWLoggerFactory
			.getLogger(MarketMailController.class);
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		int count=MaketMailService.countTotal();
		MarketMailThread thread =  MarketMailThread.getInstance();
		boolean running = thread.isRunning();
		
        PageInfo pi = WebParamterUtil.getPageInfo(request);
        String like = ServletRequestUtils.getStringParameter(request, "like", "");
        PagerControl<MarketMail> pc = MaketMailService.getPagerByObj(pi,like);
        
        model.addAttribute("pc", pc);
		model.addAttribute("count", count);
		model.addAttribute("running", running);
		return "/market/list";
	}


	@RequestMapping(value = "/uploadMail")
	public void uploadUser(MultipartHttpServletRequest request,
			HttpServletResponse response)  {

		try {
			
			MultiValueMap<String, MultipartFile> map = request.getMultiFileMap();
			for (String key : map.keySet()) {
				
				List<MultipartFile> list = map.get(key);
				for (MultipartFile multipartFile : list) {
					MaketMailService.saveMails(multipartFile.getInputStream());
				}
			}
			StageHelper.dwzSuccessForward("文件上传成功", "", "/market/list", response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e, null, WebUtils.getIpAddr(request));
			StageHelper.dwzSuccessForward("文件上传失败", "", "/market/list", response);
		}
	}

	@RequestMapping(value = "/sendMail")
    public void sendMail( Model model, HttpServletRequest request,HttpServletResponse response) {
    	
    		int count = ServletRequestUtils.getIntParameter(request, "count",0);
    		int mailId = ServletRequestUtils.getIntParameter(request, "mailId",0);
    		int campain = ServletRequestUtils.getIntParameter(request, "campain",0);
    		int group = ServletRequestUtils.getIntParameter(request, "group",0);
    		
    		MarketMailThread thread =  MarketMailThread.getInstance();
    		if(!thread.isRunning()){
    			thread.setExecuteCount(count);
    			thread.startThread(campain,mailId,group);
    			StageHelper.dwzSuccessForward("命令发送成功", "", "/market/list", response);
    		}else{
    			int success = thread.getSuccess();
    			StageHelper.dwzSuccessForward("正在发送,请稍后再试试,成功"+success+"条", "", "/market/list", response);
    		}
    }
	
	@RequestMapping(value = "/del")
	public void del(HttpServletRequest request, Model model,
			HttpServletResponse response) {
		int id = ServletRequestUtils.getIntParameter(request, "id",0);
		boolean result = MaketMailService.del(id);
		if(result){
			StageHelper.dwzSuccessForward("删除成功", "", "/market/list", response);
		}else{
			StageHelper.dwzFailForward("删除失败", "", "/market/list", response);
		}
	}
	
	@RequestMapping(value = "/sendTestMail")
    public void sendTestMail( Model model, HttpServletRequest request,HttpServletResponse response) {
    	
    		int mailId = ServletRequestUtils.getIntParameter(request, "mailId",0);
    		int campain = ServletRequestUtils.getIntParameter(request, "campain",0);
    		int group = ServletRequestUtils.getIntParameter(request, "group",0);
    		String mail = ServletRequestUtils.getStringParameter(request, "mail","yinggao.zhuo@opi-corp.com");
    		
    		Map<String,Object> fieldValue = new HashMap<String,Object>();
    		int[] groups = new int[]{group};
    	 	boolean result= DmdeliverUtil.sendMail(mail,fieldValue,campain,mailId,groups);
    	 	if(result){
    	 		StageHelper.dwzSuccessForward("发送成功", "", "/market/list", response);
    	 	}else{
    	 		StageHelper.dwzSuccessForward("发送失败", "", "/market/list", response);
    	 	}
    }
	
}
