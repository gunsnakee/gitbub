package com.meiliwan.emall.bkstage.web.controller.mms;


import com.meiliwan.emall.base.bean.BaseSysConfig;
import com.meiliwan.emall.base.client.BaseSysConfigServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserCategoryIntegralRule;
import com.meiliwan.emall.mms.client.UserCategoryIntegralRuleServiceClient;
import com.meiliwan.emall.pms.bean.ProCategory;
import com.meiliwan.emall.pms.client.ProCategoryClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类目积分 controller
 * Created by yiyou.luo on 13-6-10.
 */
@Controller("bkstageCategoryIntegralRuleController")
@RequestMapping("/mms/categoryIntegral")
public class CategoryIntegralRuleController {
    private  final MLWLogger logger = MLWLoggerFactory
            .getLogger(this.getClass());
    /**
     * 类目积分列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
        PageInfo pageInfo = StageHelper.getPageInfo(request);
        UserCategoryIntegralRule rule = new UserCategoryIntegralRule();
        int firstCategoryId = ServletRequestUtils.getIntParameter(request, "firstCategoryId", 0);
        int secondCategoryId = ServletRequestUtils.getIntParameter(request, "secondCategoryId", 0);
        int categoryId = ServletRequestUtils.getIntParameter(request, "categoryId", 0);
        int defaultNum =0;
        try{
            if (firstCategoryId>0) {
                rule.setFirstCategoryId(firstCategoryId);
            }
            if (secondCategoryId>0) {
                rule.setSecondCategoryId(secondCategoryId);
            }
            if (categoryId>0) {
                rule.setCategoryId(categoryId);
            }
            PagerControl<UserCategoryIntegralRule> pc = UserCategoryIntegralRuleServiceClient.getUserCategoryIntegralRulePaperByObj(rule,pageInfo);
            model.addAttribute("pc", pc);
            // 获取一级类目
            ProCategory  category = new ProCategory();
            category.setParentId(0);
            List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
            request.setAttribute("categoryList",categoryList);

            //判断是否先设置默认积分
            UserCategoryIntegralRule ruleDefault = new UserCategoryIntegralRule();
            ruleDefault.setCategoryId(0);
            PagerControl<UserCategoryIntegralRule> pcDefault = UserCategoryIntegralRuleServiceClient.getUserCategoryIntegralRulePaperByObj(ruleDefault,new PageInfo());

            if(pcDefault!=null && pcDefault.getEntityList()!=null && pcDefault.getEntityList().size()>0){
                defaultNum =  pcDefault.getEntityList().size();
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("pageInfo",pageInfo);
            map.put("rule",rule);
            logger.error(e,map,WebUtils.getIpAddr(request));
            StageHelper.dwzSuccessForward("操作失败", "121",
                    "/oms/order/transportList", response);
        }
        model.addAttribute("defaultNum", defaultNum);
        return "/mms/categoryIntegral/list";
    }

    /**
     * 类目积分添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        boolean isSuccess =false;
        UserCategoryIntegralRule rule = getUserCategoryIntegralRule(request);
        try{
            //判断是否访问页面还是处理 添加信息
            if (isHandle > 0) { //保存数据到后台
                rule.setId(null);
                int id = UserCategoryIntegralRuleServiceClient.saveCategoryIntegralRule(rule);
                if (id>0) {
                    isSuccess = true;
                }
            } else {   //跳转到添加数据页面
                // 获取一级类目
                ProCategory  category = new ProCategory();
                category.setParentId(0);
                List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
                request.setAttribute("categoryList",categoryList);
                return "/mms/categoryIntegral/add";
            }

        }catch(Exception e){
            Map map = new HashMap();
            map.put("rule", rule);
            logger.error(e, map, WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "121", StageHelper.DWZ_FORWARD, "/mms/categoryIntegral/list", response);
    }

    /**
     * 类目积分编辑
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        int id = ServletRequestUtils.getIntParameter(request, "id", -1);
        double ratio = ServletRequestUtils.getDoubleParameter(request, "ratio", 0);
        String idStr = ServletRequestUtils.getStringParameter(request,"id",null);
        boolean isSuccess =  false;
        try{
            //判断是否访问页面还是处理 编辑信息
            if (isHandle > 0) { //保存数据到后台
                UserCategoryIntegralRule rule = new UserCategoryIntegralRule();// getUserCategoryIntegralRule(request);
                rule.setId(id);
                rule.setRatio(ratio);
                rule.setUpdateTime(new Date());
                isSuccess = UserCategoryIntegralRuleServiceClient.updateUserCategoryIntegralRule(rule);
                return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "121", StageHelper.DWZ_FORWARD, "/mms/categoryIntegral/list", response);
            } else {   //跳转到编辑数据页面
                UserCategoryIntegralRule rule = UserCategoryIntegralRuleServiceClient.getCategoryIntegralRuleById(idStr);
                request.setAttribute("rule",rule);

                // 获取一级类目
                ProCategory  category = new ProCategory();
                category.setParentId(0);
                List<ProCategory> categoryList = ProCategoryClient.getListByPId(category);
                request.setAttribute("categoryList",categoryList);
                return "/mms/categoryIntegral/edit";
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("isHandle",isHandle);
            map.put("id",id);
            map.put("ratio",ratio);
            logger.error(e, map, WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "121", StageHelper.DWZ_FORWARD, "/mms/categoryIntegral/list", response);
    }

    /**
     * 类目积分编辑(停用 启用)
     */
    @RequestMapping("/updateState")
    public String updateState(HttpServletRequest request, HttpServletResponse response) {
        int toState = ServletRequestUtils.getIntParameter(request, "toState", -1);
        int id =  ServletRequestUtils.getIntParameter(request, "id", -1);
        boolean isSuccess = false;
        try{
            //判断是否访问页面还是处理 编辑信息
            if (toState>=0 && id>0) { //保存数据到后台
                UserCategoryIntegralRule rule = new UserCategoryIntegralRule();
                rule.setId(id);
                rule.setState(toState);
                isSuccess = UserCategoryIntegralRuleServiceClient.updateUserCategoryIntegralRule(rule);
            }
        }catch(Exception e){
            Map map = new HashMap();
            map.put("toState",toState);
            map.put("id",id);
            logger.error(e, map, WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "121", StageHelper.DWZ_FORWARD, "/mms/categoryIntegral/list", response);
    }

    /**
     * 默认积分比例设置
     */
    @RequestMapping("/defaultRatio")
    public String defaultRatio(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        UserCategoryIntegralRule rule = new UserCategoryIntegralRule();
        boolean isSuccess = false;
        try{
            //判断是否访问页面还是处理 编辑信息
            if (isHandle > 0) { //保存数据到后台
                rule = getUserCategoryIntegralRule(request);
                if(rule.getId()>0){  //默认积分比例编辑保存
                    isSuccess = UserCategoryIntegralRuleServiceClient.updateUserCategoryIntegralRule(rule);
                }else {  //默认积分设置保存
                    rule.setId(null);
                    int  id =   UserCategoryIntegralRuleServiceClient .saveCategoryIntegralRule(rule);
                    if (id>0){
                        isSuccess = true;
                    }
                }
              } else {   //跳转到编辑数据页面
                List<UserCategoryIntegralRule> ruleList = UserCategoryIntegralRuleServiceClient.getCategoryIntegralRuleByCategoryId("0") ;
                if (ruleList != null && ruleList.size()>0){
                    rule = ruleList.get(0) ;
                }
                request.setAttribute("rule",rule);
                return "/mms/categoryIntegral/defaultRatio";
            }

        }catch(Exception e){
            Map map = new HashMap();
            map.put("isHandle",isHandle);
            map.put("rule",rule);
            logger.error(e,map,WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "121", StageHelper.DWZ_CLOSE_CURRENT, "/mms/categoryIntegral/list", response);
    }

    /**
     * 检验该类目是否已经存在类目积分中
     */
    @RequestMapping("/categoryIsExisted")
    public void categoryIsExisted(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        Boolean isExisted = false;
        int categoryId = ServletRequestUtils.getIntParameter(request, "categoryId",-1);
        if (categoryId >0){
            try{
                List<UserCategoryIntegralRule> ruleList = UserCategoryIntegralRuleServiceClient.getCategoryIntegralRuleByCategoryId(categoryId+"");
                if (ruleList != null & ruleList.size() > 0){
                    isExisted = true;
                }
            }catch(Exception e){
                Map map = new HashMap();
                map.put("categoryId",categoryId);
                isExisted = true;
            }
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print(isExisted);
    }

    public UserCategoryIntegralRule getUserCategoryIntegralRule(HttpServletRequest request) {
        UserCategoryIntegralRule rule  = new UserCategoryIntegralRule();
        rule.setId(ServletRequestUtils.getIntParameter(request, "id", -1));
        rule.setFirstCategoryId(ServletRequestUtils.getIntParameter(request, "firstCategoryId", -1));
        rule.setSecondCategoryId(ServletRequestUtils.getIntParameter(request, "secondCategoryId", -1));
        rule.setCategoryId(ServletRequestUtils.getIntParameter(request, "categoryId", -1));
        rule.setCategoryName(ServletRequestUtils.getStringParameter(request, "categoryName", null));
        rule.setRatio(ServletRequestUtils.getDoubleParameter(request, "ratio", 0));
        rule.setIsDel(ServletRequestUtils.getIntParameter(request, "isDel", 0));
        rule.setRuleType(ServletRequestUtils.getIntParameter(request, "ruleType", 0));
        rule.setState(ServletRequestUtils.getIntParameter(request, "state", 0));
        return rule;
    }



}
