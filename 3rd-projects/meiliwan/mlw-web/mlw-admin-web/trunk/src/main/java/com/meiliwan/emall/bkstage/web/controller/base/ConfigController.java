package com.meiliwan.emall.bkstage.web.controller.base;

import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.base.client.BaseSysConfigServiceClient;
import com.meiliwan.emall.bkstage.web.Scheduled.SafeStockMonitorScheduled;
import com.meiliwan.emall.bkstage.web.Scheduled.SafeStockUtil;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统基础配置项管理 controller
 * Created by yiyou.luo on 13-6-7.
 */
@Controller("bkstageConfigController")
@RequestMapping("/base/config")
public class ConfigController {

    private final MLWLogger logger = MLWLoggerFactory.getLogger(this.getClass());

    /**
     * 系统基础配置项列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        String sysConfigCode = ServletRequestUtils.getStringParameter(request, "sysConfigCode",null);
        String sysConfigName = ServletRequestUtils.getStringParameter(request, "sysConfigName", null);
        BaseSysConfig config = new BaseSysConfig();
        if(StringUtils.isNotEmpty(sysConfigCode))  {
            config.setSysConfigCode(sysConfigCode);
        }
        if(StringUtils.isNotEmpty(sysConfigName))  {
            config.setSysConfigName(sysConfigName);
        }
        try{
            PagerControl<BaseSysConfig> pc = BaseSysConfigServiceClient.getSysConfigPaperByObj(config, pageInfo);
            model.addAttribute("pc", pc);
        }catch(Exception e){
            Map map = new HashMap();
            map.put("config",config);
            logger.error(e,map,WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("操作失败。", "77", "/base/config/list", response);
        }
        return "/base/config/list";
    }

    /**
     * 系统基础配置项添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {   //保存数据到后台
            BaseSysConfig config = getBaseSysConfig(request);
            boolean isSuccess =false;
            try{
                int id = BaseSysConfigServiceClient.saveSysConfig(config);
                if (id>0) {
                    isSuccess =true;
                }
            }catch(Exception e){
                Map map = new HashMap();
                map.put("config",config);
                logger.error(e,map,WebUtils.getIpAddr(request));
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "77", StageHelper.DWZ_CLOSE_CURRENT, "/base/config/list", response);
        } else {  //跳转到添加数据页面
            return "/base/config/add";
        }
    }

    /**
     * 系统基础配置项编辑
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) { //保存数据到后台
            BaseSysConfig config = getBaseSysConfig(request);
            boolean isSuccess = false;
            try{
                isSuccess = BaseSysConfigServiceClient.updateSysConfig(config);
            }catch(Exception e){
                Map map = new HashMap();
                map.put("config",config);
                logger.error(e,map,WebUtils.getIpAddr(request));
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "77", StageHelper.DWZ_CLOSE_CURRENT, "/base/config/list", response);
        } else {   //跳转到编辑数据页面
            int sysConfigId = ServletRequestUtils.getIntParameter(request,"sysConfigId",0);
            try{
                BaseSysConfig config = BaseSysConfigServiceClient.getSysConfigById(sysConfigId);
                request.setAttribute("config",config);
                return "/base/config/edit";
            }catch (Exception e){
                Map map = new HashMap();
                map.put("sysConfigId",sysConfigId);
                logger.error(e,map,WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "77", "/base/config/list", response);
            }
        }
    }

    /**
     * 系统基础配置项编辑
     */
    @RequestMapping("/editSeo")
    public String editSeo(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) { //保存数据到后台
            BaseSysConfig config = getBaseSysConfig(request);
            boolean isSuccess = false;
            try{

                isSuccess = BaseSysConfigServiceClient.updateSysConfig(config);
            }catch(Exception e){
                Map map = new HashMap();
                map.put("config",config);
                logger.error(e,map,WebUtils.getIpAddr(request));
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "10228", StageHelper.DWZ_CLOSE_CURRENT, "/base/config/seoList", response);
        } else {   //跳转到编辑数据页面
            int sysConfigId = ServletRequestUtils.getIntParameter(request,"sysConfigId",0);
            try{
                BaseSysConfig config = BaseSysConfigServiceClient.getSysConfigById(sysConfigId);
                request.setAttribute("config",config);
                return "/base/config/edit_seo";
            }catch (Exception e){
                Map map = new HashMap();
                map.put("sysConfigId",sysConfigId);
                logger.error(e,map,WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "10228", "/base/config/seoList", response);
            }
        }
    }

    /**
     * 检验系统配置项编码是否被占用x
     */
    @RequestMapping("/checkSysConfigCodeIsUsed")
    public void checkSysConfigCodeIsUsed(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        Boolean isUsed = true;
        String sysConfigCode = ServletRequestUtils.getStringParameter(request, "sysConfigCode",null);
        try{
            if (sysConfigCode != null){
                List<BaseSysConfig> configList = BaseSysConfigServiceClient.getSysConfigByCode(sysConfigCode);
                if (configList != null & configList.size() > 0){
                    isUsed = true;
                }else {
                    isUsed = false;
                }
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("sysConfigCode",sysConfigCode);
            logger.error(e,map,WebUtils.getIpAddr(request));
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print(isUsed);
    }

    public BaseSysConfig getBaseSysConfig(HttpServletRequest request) {
        BaseSysConfig config = new BaseSysConfig();
        config.setSysConfigId(ServletRequestUtils.getIntParameter(request, "sysConfigId", -1));
        config.setSysConfigName(ServletRequestUtils.getStringParameter(request, "sysConfigName", null));
        config.setSysConfigCode(ServletRequestUtils.getStringParameter(request, "sysConfigCode", null));
        config.setSysConfigValue(ServletRequestUtils.getStringParameter(request, "sysConfigValue", null));
        config.setDescp(ServletRequestUtils.getStringParameter(request, "descp", null));
        return config;
    }

    /**
     * 安全库存报警时间设置
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping("/stock-alarm-set")
    public String stockAlarmSet(HttpServletRequest request, HttpServletResponse response,Model model){
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle > 0){
            int sysConfigId = ServletRequestUtils.getIntParameter(request,"sysConfigId",0);
            String sysConfigValue = ServletRequestUtils.getStringParameter(request,"safeTime",null);
            BaseSysConfig sysConfig = new BaseSysConfig();
            sysConfig.setSysConfigId(sysConfigId);
            sysConfig.setSysConfigValue(sysConfigValue);
            sysConfig.setSysConfigCode("safe_stock_time");
            try {
                boolean suc = BaseSysConfigServiceClient.updateSysConfig(sysConfig);
                if (suc){
                    int time = (int)(Float.valueOf(sysConfigValue)*60);
                    SafeStockUtil.setSafeTime(time);
                    //开始执行定时报警任务
                    SafeStockMonitorScheduled.excute();
                }else {
                    return StageHelper.writeDwzSignal("300", "修改库存报警时间失败", "10195", StageHelper.DWZ_FORWARD, "", response);
                }
            }catch (Exception e){
                logger.error(e, sysConfig, WebUtils.getIpAddr(request));
                return StageHelper.writeDwzSignal("300", "修改库存报警时间失败", "10195", StageHelper.DWZ_FORWARD, "", response);
            }
            return StageHelper.writeDwzSignal("200", "操作成功", "10195", StageHelper.DWZ_FORWARD, "/base/config/stock-alarm-set", response);
        }
        BaseSysConfig config = BaseSysConfigServiceClient.getUniqueSysConfigByCode("safe_stock_time");
        model.addAttribute("config",config);
        return "/base/config/stock_alarm_set";
    }

    /**
     * SEO 系统基础配置项列表
     */
    @RequestMapping("/seoList")
    public String seoList(HttpServletRequest request, Model model,HttpServletResponse response) {
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        BaseSysConfig config = new BaseSysConfig();
        try{
            PagerControl<BaseSysConfig> pc = BaseSysConfigServiceClient.getSeoListByEntityAndPageInfo(config, pageInfo);
            model.addAttribute("pc", pc);
        }catch(Exception e){
            Map map = new HashMap();
            map.put("config",config);
            logger.error(e,map,WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("操作失败。", "77", "/base/config/list_seo", response);
        }
        return "/base/config/list_seo";
    }
}
