package com.meiliwan.emall.monitor.web;

import java.util.Date;
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
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;
import com.meiliwan.emall.monitor.bean.RequestLog;
import com.meiliwan.emall.monitor.bean.RequestLogVO;
import com.meiliwan.emall.monitor.common.Project;
import com.meiliwan.emall.monitor.service.MLWLogService;
import com.meiliwan.emall.monitor.service.MonitorService;

/**
 * MonitorController
 *
 */
@Controller
@RequestMapping(value = "/monitor")
public class MonitorController{
    
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(MonitorController.class);
	public static final String ERROR="error";
	@Autowired
	private MLWLogService mLWLogService;
	@Autowired
	private MonitorService monitorService;
	/**
     * 监控请求列表及查询
     *
     * @param request
     * @return
     * @author yinggao.zhuo
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model, HttpServletResponse response) {

    		RequestLogVO dto = new RequestLogVO();
        try {
            PagerControl<RequestLog> pc = null;

            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
           
            setParams(request, model, dto);
            pc = monitorService.getPageRequest(dto, pageInfo);
            
            model.addAttribute("search", dto);
            model.addAttribute("pc", pc);
            
        } catch (Exception e) {
        		logger.error(e,dto,WebUtils.getIpAddr(request));
        		return "error";
        }
        return "/monitor/list";
    }

	private void setParams(HttpServletRequest request, Model model, RequestLogVO dto) {
		// TODO Auto-generated method stub
		int id = ServletRequestUtils.getIntParameter(request, "id",0);
	    	if(id!=0){
	    		dto.setId(id);
	    	}
	    	String type = ServletRequestUtils.getStringParameter(request, "type", "");
	    	if(!StringUtil.checkNull(type)){
	    		dto.setType(type);
	    	}
	    	String name = ServletRequestUtils.getStringParameter(request, "name", "");
	    	if(!StringUtil.checkNull(name)){
	    		dto.setName(name);
	    	}
	    	int hour = ServletRequestUtils.getIntParameter(request, "hour", 0);
	    	if(hour>0){
	    		dto.setHour(hour);
	    	}
	    	String startTime = ServletRequestUtils.getStringParameter(request, "startTime", "");
	    	if(!StringUtil.checkNull(startTime)){
	    		Date sTime = DateUtil.parseDateTime(startTime);
	    		dto.setStartTime(sTime);
	    	}
	    	String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
	    	if(!StringUtil.checkNull(endTime)){
	    		Date eTime = DateUtil.parseDateTime(endTime);
	    		dto.setEndTime(eTime);
	    	}
     	String startTimeConsume = ServletRequestUtils.getStringParameter(request, "startTimeConsume","");
	    	if(!StringUtil.checkNull(startTimeConsume)){
	    		dto.setStartTimeConsume(Long.parseLong(startTimeConsume));
	    	}
     	String endTimeConsume = ServletRequestUtils.getStringParameter(request, "endTimeConsume","");
	    	if(!StringUtil.checkNull(endTimeConsume)){
	    		dto.setEndTimeConsume(Long.parseLong(endTimeConsume));
	    	}	    	
	    	String startTimeDesc= ServletRequestUtils.getStringParameter(request, "startTimeDesc","true");
	    	if(startTimeDesc.equals("true")){
	    		dto.setStartTimeDesc(true);
	    	}	 
	    	if(startTimeDesc.equals("false")){
	    		dto.setStartTimeDesc(false);
	    	}
	    	String timeConsumeDesc= ServletRequestUtils.getStringParameter(request, "timeConsumeDesc","");
	    	if(timeConsumeDesc.equals("true")){
	    		dto.setTimeConsumeDesc(true);
	    	}	 
	    	if(timeConsumeDesc.equals("false")){
	    		dto.setTimeConsumeDesc(false);
	    	}
	    	
	}
	
    @RequestMapping(value = "/getById")
    public String getById(HttpServletRequest request, Model model, HttpServletResponse response) {

	    	int id= ServletRequestUtils.getIntParameter(request, "id",0);
        try {
         	if(id<1){
         		return ERROR;
         	}
         	RequestLog log = monitorService.getRequestById(id);
            model.addAttribute("log", log);
        } catch (Exception e) {
        		logger.error(e,"id("+id+")",WebUtils.getIpAddr(request));
        		return ERROR;
        }
        return "/monitor/detail";
    }
    
    
    /**
     * 监控请求列表及查询
     *
     * @param request
     * @return
     * @author yinggao.zhuo
     */
    @RequestMapping(value = "/MLWLogList")
    public String MLWLogList(HttpServletRequest request, Model model, HttpServletResponse response) {

	    	MLWLogVO dto = new MLWLogVO();
        try {
            PagerControl<MLWLog> pc = null;

            PageInfo pageInfo = WebParamterUtil.getPageInfo(request);
           
            setMLWLogParams(request, model, dto);
            pc = monitorService.getPageMLWLog(dto, pageInfo);
            model.addAttribute("search", dto);
            model.addAttribute("pc", pc);
            model.addAttribute("app", Project.values());
        } catch (Exception e) {
        		logger.error(e,dto,WebUtils.getIpAddr(request));
        		return "error";
        }
        return "/MLWLog/list";
    }
    
    
    @RequestMapping(value = "/getByUuid")
    public void getByUuid(HttpServletRequest request, Model model, HttpServletResponse response) {
    	
	    	String uuid = ServletRequestUtils.getStringParameter(request, "errorUuid","");
	    	List<MLWLog> list = null;
	    	try {
		    	if(!StringUtil.checkNull(uuid)){
		    		list = mLWLogService.getMLWLogListByUuid(uuid);
		    	}
	    	} catch (Exception e) {
	    		logger.error(e,uuid,WebUtils.getIpAddr(request));
	    	}
	    WebUtils.writeJsonByObj(list, response);
    }

	private void setMLWLogParams(HttpServletRequest request, Model model, MLWLogVO dto) {
		// TODO Auto-generated method stub
		int id = ServletRequestUtils.getIntParameter(request, "id",0);
	    	if(id!=0){
	    		dto.setId(id);
	    	}
	    	String level = ServletRequestUtils.getStringParameter(request, "level", "ERROR");
	    	if(!StringUtil.checkNull(level)){
	    		dto.setLevel(level);
	    	}
	    	String module = ServletRequestUtils.getStringParameter(request, "module", "");
	    	if(!StringUtil.checkNull(module)){
	    		dto.setModule(module);
	    	}
	    	String application = ServletRequestUtils.getStringParameter(request, "application", "");
	    	if(!StringUtil.checkNull(application)){
	    		dto.setApplication(application);
	    	}
	    	String serverIp = ServletRequestUtils.getStringParameter(request, "serverIp", "");
	    	if(!StringUtil.checkNull(serverIp)){
	    		dto.setServerIp(serverIp);
	    	}
	    String clientIp = ServletRequestUtils.getStringParameter(request, "clientIp", "");
	    	if(!StringUtil.checkNull(clientIp)){
	    		dto.setClientIp(clientIp);
	    	}
	    	
	    	String createTimeStart = ServletRequestUtils.getStringParameter(request, "createTimeStart", "");
	    	if(!StringUtil.checkNull(createTimeStart)){
	    		Date sTime = DateUtil.parseDateTime(createTimeStart);
	    		dto.setCreateTimeStart(sTime);
	    	}
	    	String createTimeEnd = ServletRequestUtils.getStringParameter(request, "createTimeEnd", "");
	    	if(!StringUtil.checkNull(createTimeEnd)){
	    		Date eTime = DateUtil.parseDateTime(createTimeEnd);
	    		dto.setCreateTimeEnd(eTime);
	    	}
	    
	    	int desc= ServletRequestUtils.getIntParameter(request, "desc",1);
	    	if(desc==1){
	    		dto.setOrder(true);
	    	}	 
	    	if(desc==0){
	    		dto.setOrder(false);
	    	}
	}
	
    @RequestMapping(value = "/getMLWLogById")
    public String getMLWLogById(HttpServletRequest request, Model model, HttpServletResponse response) {

	    	int id= ServletRequestUtils.getIntParameter(request, "id",0);
        try {
         	if(id<1){
         		return ERROR;
         	}
         	MLWLog bean = monitorService.getMLWLogById(id);
            model.addAttribute("bean", bean);
        } catch (Exception e) {
        		logger.error(e,"id("+id+")",WebUtils.getIpAddr(request));
        		return ERROR;
        }
        return "/MLWLog/detail";
    }
    
}
