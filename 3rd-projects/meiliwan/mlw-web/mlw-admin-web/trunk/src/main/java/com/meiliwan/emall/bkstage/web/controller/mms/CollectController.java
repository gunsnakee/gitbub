package com.meiliwan.emall.bkstage.web.controller.mms;


import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserCollect;
import com.meiliwan.emall.mms.client.UserCollectClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户收藏 controller
 * Created by yiyou.luo on 13-6-10.
 */
@Controller("bkstageCollectController")
@RequestMapping("/mms/collect")
public class CollectController {
    private  final MLWLogger logger = MLWLoggerFactory
            .getLogger(this.getClass());
    /**
     * 用户收藏列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
       // PageInfo pageInfo = StageHelper.getPageInfo(request);
        PageInfo pageInfo = StageHelper.getPageInfo(request,"create_time","desc");
        UserCollect collect = new UserCollect();
        String nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
        String proName = ServletRequestUtils.getStringParameter(request, "proName", null);
        if (StringUtils.isNotEmpty(nickName)) {
            collect.setNickName(nickName.trim());
        }
        if (StringUtils.isNotEmpty(proName)) {
            collect.setProName(proName.trim());
        }
        PagerControl<UserCollect> pc = new PagerControl<UserCollect>();
        try{
            pc = UserCollectClient.getUserCollectPaperByObj(collect, pageInfo);
        } catch(Exception e){
            Map map = new HashMap();
            map.put("pageInfo",pageInfo);
            map.put("collect",collect);
            logger.error(e,map, WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("服务异常。", "148", "/base/collect/list", response);
        }
        model.addAttribute("pc", pc);
        model.addAttribute("nickName",nickName) ;
        model.addAttribute("proName",proName)  ;
        return "/mms/collect/list";
    }

    /**
     * 用户收藏添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) { //保存数据到后台
            UserCollect msg = getUserCollect(request);
            msg.setId(null);
            boolean isSuccess =false;
            try{
                int id = UserCollectClient.saveUserCollect(msg);
                if (id>0) {
                    isSuccess = true;
                }
            }catch(Exception e){
                Map map = new HashMap();
                map.put("msg",msg);
                map.put("isHandle",isHandle);
                logger.error(e,map, WebUtils.getIpAddr(request));
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "148", StageHelper.DWZ_CLOSE_CURRENT, "/mms/collect/list", response);
        } else {   //跳转到添加数据页面
            return "/mms/collect/add";
        }
    }

    /**
     * 用户收藏编辑
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 编辑信息
        boolean isSuccess = false;
        try{
            if (isHandle > 0) { //保存数据到后台
                UserCollect collect = getUserCollect(request);
                isSuccess = UserCollectClient.updateUserCollect(collect);
                return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "148", StageHelper.DWZ_CLOSE_CURRENT, "/mms/Collect/list", response);
            } else {   //跳转到编辑数据页面
                int id = ServletRequestUtils.getIntParameter(request,"id",-1);
                UserCollect collect = UserCollectClient.getUserCollectById(id);
                request.setAttribute("collect",collect);
                return "/mms/collect/edit";
            }
        } catch (Exception e){
            Map map = new HashMap();
            map.put("isHandle",isHandle);
            map.put("request",request);
            logger.error(e,map, WebUtils.getIpAddr(request));
        }
        return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "148", StageHelper.DWZ_CLOSE_CURRENT, "/mms/Collect/list", response);


    }

    public UserCollect getUserCollect(HttpServletRequest request) {
        UserCollect msg  = new UserCollect();
        /*rule.setId(ServletRequestUtils.getIntParameter(request, "id", -1));
        rule.setFirstCategoryId(ServletRequestUtils.getIntParameter(request, "firstCategoryId", -1));
        rule.setSecondCategoryId(ServletRequestUtils.getIntParameter(request, "secondCategoryId", -1));
        rule.setCategoryId(ServletRequestUtils.getIntParameter(request, "categoryId", -1));
        rule.setCategoryName(ServletRequestUtils.getStringParameter(request, "categoryName", null));
        rule.setRatio(ServletRequestUtils.getDoubleParameter(request, "ratio", 0));
        rule.setIsDel(ServletRequestUtils.getIntParameter(request, "isDel", 0));
        rule.setRuleType(ServletRequestUtils.getIntParameter(request, "ruleType", 0));
        rule.setState(ServletRequestUtils.getIntParameter(request, "state", 0));*/
        return msg;
    }



}
