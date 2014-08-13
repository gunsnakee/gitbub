package com.meiliwan.emall.bkstage.web.controller.mms;


import com.google.common.base.Strings;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.StringUtil;
import com.meiliwan.emall.commons.web.TextUtil;
import com.meiliwan.emall.commons.web.WebUtils;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.mms.bean.UserStationMsg;
import com.meiliwan.emall.mms.client.UserPassportClient;
import com.meiliwan.emall.mms.client.UserStationMsgClient;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 站内信 controller
 * Created by yiyou.luo on 13-6-10.
 */
@Controller("bkstageStationMsgController")
@RequestMapping("/mms/stationMsg")
public class StationMsgController {
    private final MLWLogger logger = MLWLoggerFactory
            .getLogger(this.getClass());
    /**
     * 站内信列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, Model model,HttpServletResponse response) {
        // PageInfo pageInfo = StageHelper.getPageInfo(request);
        PageInfo pageInfo = StageHelper.getPageInfo(request, "create_time", "desc");
        UserStationMsg msg = new UserStationMsg();

        String nickName = ServletRequestUtils.getStringParameter(request, "nickName", null);
        String adminName = ServletRequestUtils.getStringParameter(request, "adminName", null);
        if (StringUtils.isNotEmpty(nickName)) {
            msg.setNickName(nickName);
        }
        if (StringUtils.isNotEmpty(adminName)) {
            msg.setAdminName(adminName);
        }
        try{
            PagerControl<UserStationMsg> pc = UserStationMsgClient.getUserStationMsgPaperByObj(msg, pageInfo);
            model.addAttribute("pc", pc);
        }catch(Exception e){
            Map map = new HashMap();
            map.put("msg",msg);
            map.put("pageInfo",pageInfo);
            logger.error(e, map, WebUtils.getIpAddr(request));
            return StageHelper.dwzFailForward("操作失败。", "148", "/base/stationMsg/list", response);
        }
        model.addAttribute("nickName",nickName) ;
        model.addAttribute("adminName",adminName);
        return "/mms/stationMsg/list";
    }

    /**
     * 站内信添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) { //保存数据到后台
            UserStationMsg msg = getUserStationMsg(request);
            msg.setId(null);
            boolean isSuccess = false;
            try{
                int id = UserStationMsgClient.saveUserStationMsg(msg);
                if (id > 0) {
                    isSuccess = true;
                }
            }catch(Exception e){
                Map map = new HashMap();
                map.put("msg",msg);
                logger.error(e, map, WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "148", "/base/stationMsg/list", response);
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "148", StageHelper.DWZ_CLOSE_CURRENT, "/mms/stationMsg/list", response);
        } else {   //跳转到添加数据页面
            return "/mms/stationMsg/add";
        }
    }

    /**
     * 站内信编辑
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 编辑信息
        if (isHandle > 0) { //保存数据到后台
            UserStationMsg msg = getUserStationMsg(request);
            boolean isSuccess = false;
            try{
                isSuccess = UserStationMsgClient.updateUserStationMsg(msg);
            }catch(Exception e){
                Map map = new HashMap();
                map.put("msg",msg);
                logger.error(e, map, WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "148", "/base/stationMsg/list", response);
            }
            return StageHelper.writeDwzSignal(isSuccess ? "200" : "300", isSuccess ? "操作成功" : "操作失败", "148", StageHelper.DWZ_CLOSE_CURRENT, "/mms/stationMsg/list", response);
        } else {   //跳转到编辑数据页面
            int id = ServletRequestUtils.getIntParameter(request, "id", -1);
            try{
                UserStationMsg msg = UserStationMsgClient.getUserStationMsgById(id);
                request.setAttribute("msg", msg);
            }catch(Exception e){
                Map map = new HashMap();
                map.put("id",id);
                logger.error(e, map, WebUtils.getIpAddr(request));
                return StageHelper.dwzFailForward("操作失败。", "148", "/base/stationMsg/list", response);
            }
            return "/mms/stationMsg/edit";
        }
    }

    public UserStationMsg getUserStationMsg(HttpServletRequest request) {
        UserStationMsg msg = new UserStationMsg();
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

    @RequestMapping(value = "/to-send")
    public String toSendMsg(Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletRequestBindingException, IOException {
        String uName = ServletRequestUtils.getStringParameter(request, "uName", null);
        model.addAttribute("name", uName);
        return "/mms/stationMsg/addMsg";
    }

    /**
     * send message
     * @return
     */
    @RequestMapping(value = "/send")
    public void sendMsg(Model model, HttpServletRequest request, HttpServletResponse response)
            throws ServletRequestBindingException, IOException {
        String uName = ServletRequestUtils.getStringParameter(request, "name");
        String msgContent = ServletRequestUtils.getStringParameter(request, "msgContent");
        int contentLength = new String(msgContent.getBytes("gbk"), "ISO8859_1").length();
        if (!Strings.isNullOrEmpty(uName) && contentLength >= 10 && contentLength <= 1000) {
            String[] names = uName.split(";|；");
            if (names != null && names.length > 0) {
                List<Integer> uids = new ArrayList<Integer>();
                List<String> userName = new ArrayList<String>();
                for (String name : names) {
                    if (!Strings.isNullOrEmpty(name)) {
                        UserPassport userPassport = UserPassportClient.getPassportByNickName(name);
                        if (userPassport != null) {
                            uids.add(userPassport.getUid());
                        } else {
                            userName.add(name);
                        }
                    }
                }
                if (userName == null || userName.size() == 0) {
                    LoginUser admin = StageHelper.getLoginUser(request);
                    if (admin != null && admin.getBksAdmin() != null) {
                        boolean isUc = UserStationMsgClient.saveSendMsgs(admin.getBksAdmin().getAdminId(), admin.getBksAdmin().getAdminName(), uids.toArray(), TextUtil.cleanHTML(msgContent));
                        StageHelper.writeDwzSignal(isUc ? "200" : "300", isUc ? "消息发送成功！" : "存在发送失败的消息！", "148", isUc ? StageHelper.DWZ_CLOSE_CURRENT : "", "/mms/stationMsg/list", response);
                    } else {
                        StageHelper.writeDwzSignal("300", "登陆异常，请重新登陆！", "148", "", "/mms/stationMsg/list", response);
                    }
                } else {
                    StageHelper.writeDwzSignal("300", "用户" + userName.toString() + "不存在。请核对", "148", "", "/mms/stationMsg/list", response);
                }
            }
        } else {
            StageHelper.writeDwzSignal("300", "输入有错！", "148", "", "/mms/stationMsg/list", response);
        }
    }
}
