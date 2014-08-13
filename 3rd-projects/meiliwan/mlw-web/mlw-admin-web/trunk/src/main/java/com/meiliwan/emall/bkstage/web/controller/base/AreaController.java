package com.meiliwan.emall.bkstage.web.controller.base;

import com.meiliwan.emall.base.bean.BaseAreaManagement;
import com.meiliwan.emall.base.client.BaseAreaServiceClient;
import com.meiliwan.emall.base.constant.Constants;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.*;
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
 * 地域管理 controller
 * Created by yiyou.luo on 13-6-6.
 */
@Controller("bkstageAreaController")
@RequestMapping("/base/area")
public class AreaController {
    private final MLWLogger logger = MLWLoggerFactory
            .getLogger(this.getClass());
    /**
     * 地域列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        BaseAreaManagement area = new BaseAreaManagement();
        String areaCode = ServletRequestUtils.getStringParameter(request, "areaCode", null);
        String parentCode = ServletRequestUtils.getStringParameter(request, "parentCode", null);
        String areaName = ServletRequestUtils.getStringParameter(request, "areaName",null);
        int areaGrade = ServletRequestUtils.getIntParameter(request, "areaGrade", -1);
        if (StringUtils.isNotEmpty(areaCode)) {
            area.setAreaCode(areaCode.trim());
        }
        if (StringUtils.isNotEmpty(parentCode)) {
            area.setParentCode(parentCode.trim());
        }
        if (StringUtils.isNotEmpty(areaName)) {
            area.setAreaName(areaName.trim());
        }
        if (areaGrade>=0) {
            area.setAreaGrade(areaGrade);
        }
        try{
            PagerControl<BaseAreaManagement> pc = BaseAreaServiceClient.getAreaPaperByObj(area, pageInfo);
            model.addAttribute("pc", pc);
        }catch(Exception e){
            Map map = new HashMap();
            map.put("area",area);
            logger.error(e, map, WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("操作失败。", "68", "/base/area/list", response);
        }
        model.addAttribute("areaCode", areaCode) ;
        model.addAttribute("parentCode", parentCode) ;
        model.addAttribute("areaName", areaName) ;
        model.addAttribute("areaGrade", areaGrade) ;

        return "/base/area/list";
    }

    /**
     * 地域添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        boolean isSuccess =false;
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) { //保存数据到后台
            BaseAreaManagement area = getBaseAreaManagement(request);
            try{
                int areaId = BaseAreaServiceClient.saveArea(area);
                if (areaId>0) {
                    isSuccess = true;
                }
            }catch(Exception e){
                Map map = new HashMap();
                map.put("area",area);
                logger.error(e, map, WebUtils.getIpAddr(request));
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "68", StageHelper.DWZ_CLOSE_CURRENT, "/base/area/list", response);
        } else {   //跳转到添加数据页面
            try{
                //地域列表（国家）
                List<BaseAreaManagement> countryList  = BaseAreaServiceClient.getAreasByParentCode(Constants.BASE_AREA_CODE_WORLD);
                request.setAttribute("countryList",countryList);
            }catch(Exception e){
                Map map = new HashMap();
                map.put("isHandle",isHandle);
                logger.error(e, map, WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "68", "/base/area/list", response);
            }
            return "/base/area/add";
        }
    }

    /**
     * 地域编辑
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        boolean isSuccess = false;
        try{
            //判断是否访问页面还是处理 编辑信息
            if (isHandle > 0) { //保存数据到后台
                BaseAreaManagement area = getBaseAreaManagement(request);
                isSuccess = BaseAreaServiceClient.updateArea(area);
                return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "68", StageHelper.DWZ_CLOSE_CURRENT, "/base/area/list", response);
            } else {   //跳转到编辑数据页面
                //地域列表（国家）
                int areaId = ServletRequestUtils.getIntParameter(request,"areaId",0);
                BaseAreaManagement area = BaseAreaServiceClient.getAreaById(areaId);
                request.setAttribute("area",area);
                return "/base/area/edit";
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("isHandle",isHandle);
            logger.error(e, map, WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("操作失败。", "68", "/base/area/list", response);
        }

    }

    /**
     * 根据上级编码获取地区select html
     */
    @RequestMapping("/getAreaByParentCode")
    public void getAreaByParentCode(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        StringBuffer optionHtml = new StringBuffer();
        String parentCode = ServletRequestUtils.getStringParameter(request, "parentCode",null);
        try{
            if (parentCode != null){
                List<BaseAreaManagement> areaList = BaseAreaServiceClient.getAreasByParentCode(parentCode) ;
                if (areaList != null & areaList.size() > 0){
                    for (BaseAreaManagement area:areaList){
                        optionHtml.append("<option value='"+area.getAreaCode()+"'>"+area.getAreaName()+"</option>");
                    }
                }
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("parentCode",parentCode);
            logger.error(e, map, WebUtils.getIpAddr(request));
            optionHtml.append("sysError");
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print(optionHtml.toString());
    }

    public BaseAreaManagement getBaseAreaManagement(HttpServletRequest request) {
        BaseAreaManagement area = new BaseAreaManagement();
        area.setAreaId(ServletRequestUtils.getIntParameter(request, "areaId", -1));
        area.setAreaCode(ServletRequestUtils.getStringParameter(request, "areaCode", null));
        area.setAreaGrade(ServletRequestUtils.getIntParameter(request, "areaGrade", -1));
        area.setAreaName(ServletRequestUtils.getStringParameter(request, "areaName", null));
        area.setParentCode(ServletRequestUtils.getStringParameter(request, "parentCode", null));
        area.setIsLastGrade(ServletRequestUtils.getIntParameter(request, "isLastGrade", -1));
        area.setIsDel(ServletRequestUtils.getIntParameter(request, "isDel", -1));
        return area;
    }



}
