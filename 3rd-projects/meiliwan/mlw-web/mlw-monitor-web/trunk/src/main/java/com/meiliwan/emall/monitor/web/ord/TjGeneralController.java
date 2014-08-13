package com.meiliwan.emall.monitor.web.ord;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.WebParamterUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.monitor.bean.*;
import com.meiliwan.emall.monitor.common.Project;
import com.meiliwan.emall.monitor.common.TjGeneralIndex;
import com.meiliwan.emall.monitor.service.MLWLogService;
import com.meiliwan.emall.monitor.service.MonitorService;
import com.meiliwan.emall.monitor.service.TjGeneralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * MonitorController
 *
 */
@Controller
@RequestMapping(value = "/tjgeneral")
public class TjGeneralController {
    
	private static final MLWLogger logger = MLWLoggerFactory.getLogger(TjGeneralController.class);
	public static final String ERROR="error";
	/**
     * 各个统计指标报表
     *
     * @param request
     * @return
     * @author yiyou.luo
     */
    @RequestMapping(value = "/list")
    public String list(HttpServletRequest request, Model model, HttpServletResponse response) {
        String startTimeStr = ServletRequestUtils.getStringParameter(request, "startTime", "");
        String endTimeStr = ServletRequestUtils.getStringParameter(request, "endTime", "");
        String indexCode = ServletRequestUtils.getStringParameter(request, "indexCode", "pv");
        Date startTime = null;
        Date endTime = null;
        if(!StringUtil.checkNull(startTimeStr)){
            startTime = DateUtil.parseDateTime(startTimeStr);

        }else {
            //默认前七天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -7);
            startTime = calendar.getTime();
        }
        if(!StringUtil.checkNull(endTimeStr)){
            endTime = DateUtil.parseDateTime(endTimeStr);
        }else {
            //默认昨天
            endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.DATE, -1);
            endTime = calendar.getTime();
        }
        try {
            List<String> dateList =  buildDateList(startTime,endTime);
            TjGeneralService  tjGeneralService = TjGeneralService.getInstance();
            Map tjgvvMap = tjGeneralService.getAllTjGeneral(dateList,getTjGeneralIndex(indexCode));
            model.addAttribute("startTime",startTime);
            model.addAttribute("endTime",endTime);
            model.addAttribute("dateList",dateList);
            model.addAttribute("tjgvvMap",tjgvvMap.values());
        } catch (Exception e) {
        		logger.error(e,null,WebUtils.getIpAddr(request));
        		return "error";
        }
        model.addAttribute("indexCode",indexCode);
        return "/tjgeneral/list";
    }

    /**
     * 各个统计指标报表
     *
     * @param request
     * @return
     * @author yiyou.luo
     */
    @RequestMapping(value = "/dayReport")
    public String dayReport(HttpServletRequest request, Model model, HttpServletResponse response) {
        String startTimeStr = ServletRequestUtils.getStringParameter(request, "startTime", "");
        String endTimeStr = ServletRequestUtils.getStringParameter(request, "endTime", "");
        String indexCode = ServletRequestUtils.getStringParameter(request, "indexCode", "ord");
        Date startTime = null;
        Date endTime = null;
        if(!StringUtil.checkNull(startTimeStr)){
            startTime = DateUtil.parseDateTime(startTimeStr);

        }else {
            //默认前3天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -3);
            startTime = calendar.getTime();
        }
        if(!StringUtil.checkNull(endTimeStr)){
            endTime = DateUtil.parseDateTime(endTimeStr);
        }else {
            //默认昨天
            endTime = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endTime);
            calendar.add(Calendar.DATE, -1);
            endTime = calendar.getTime();
        }
        try {
            List<String> dateList =  buildDateList(startTime,endTime);
            int tableWidth = 1 ;
            if(dateList!=null && dateList.size()>3){
                tableWidth = dateList.size()/3 + tableWidth;
            }

            TjGeneralService  tjGeneralService = TjGeneralService.getInstance();
            Map tjgvvMap = tjGeneralService.getAllTjGeneralForDayReport(dateList,getTjGeneralIndex(indexCode));
            model.addAttribute("startTime",startTime);
            model.addAttribute("endTime",endTime);
            model.addAttribute("dateList",dateList);
            model.addAttribute("tjgvvMap",tjgvvMap.values());
            model.addAttribute("tableWidth",tableWidth);

            TjGeneralViewVo tjgvTotal = tjGeneralService.getAllTjGeneralTotalForDayReport(dateList);
            model.addAttribute("tjgvTotal",tjgvTotal);
        } catch (Exception e) {
            logger.error(e,null,WebUtils.getIpAddr(request));
            return "error";
        }
        model.addAttribute("indexCode",indexCode);
        return "/tjgeneral/dayReport";
    }

    private List<String> buildDateList(Date startTime,Date endTime){
        List<String> list = new ArrayList<String>();
        if(startTime!=null && endTime!=null){
            long intervalMilli = endTime.getTime() - startTime.getTime();
            int gapDate = (int) (intervalMilli / (24 * 60 * 60 * 1000));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date calEndDate = endTime;
            Calendar cal = Calendar.getInstance();
            cal.setTime(calEndDate);
            list.add(sdf.format(calEndDate));
            for(int i=0;i<gapDate;i++){
                cal.add(cal.DATE,-1);
                list.add(sdf.format(cal.getTime()));
            }
        }
        return list;
    }


    private TjGeneralIndex getTjGeneralIndex(String indexCode){
        TjGeneralIndex.values();
        for(TjGeneralIndex tjgi:TjGeneralIndex.values()){
            if (indexCode.equals(tjgi.getCode())){
                return tjgi;
            }
        }
        return TjGeneralIndex.PV;
    }

}
