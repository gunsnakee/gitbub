package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.google.common.cache.Cache;
import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.bean.BksUserOperateLog;
import com.meiliwan.emall.bkstage.client.BksUserOperateLogClient;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.web.CaptchaUtil;
import com.meiliwan.emall.commons.web.CookieSessionUtil;
import com.meiliwan.emall.commons.web.WebUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

@Controller("bkstagePortalController")
public class PortalController {

//    private MLWLogger mlwLog =  MLWLoggerFactory.getLogger(AdminController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        return "/login";
    }

    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String userId = ServletRequestUtils.getStringParameter(request, "username", "");
        String userPwd = ServletRequestUtils.getStringParameter(request, "password", "");
        String code = ServletRequestUtils.getStringParameter(request, "J_verCode_login", "");
        int loginResult = 0;
        //打开页面时候第一次提交的时候没有数据
        if (userId.isEmpty() || userPwd.isEmpty() || code.isEmpty()) {
            return "/login";
        }

        //先判断验证码
        String sessionId = null;
        try {
            sessionId = CookieSessionUtil.getSession(request, response).getSessionId();
        } catch (JedisClientException e) {
            sessionId = "";
        }
        //验证码验证失败后返回结果loginResult=2
        if (StringUtils.isEmpty(code) || !isCodeRight(sessionId, code)) {
            loginResult = 2;
            model.addAttribute("loginResult", loginResult);
            return "/login";
        }

        if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userPwd)) {
            BksAdmin admin = RoleServiceClient.adminLogin(userId, userPwd);
            if (admin != null) {
                if (admin.getState() != 0) {
                    loginResult = 1;
                    model.addAttribute("loginResult", loginResult);
                    return "/login";
                }

                LoginUser loginUser = loginInit(admin, request, response);
                if (loginUser != null) {
                    request.getSession().setAttribute(StageHelper.loginUserSessionName, loginUser);
                    return "redirect:/index";
                }
            } else {
                loginResult = 1;
                model.addAttribute("loginResult", loginResult);
                return "/login";
            }
        }
        return "/login";
    }

    private boolean isCodeRight(String sessionId, String code) {
        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(code) || code.length() < 10)
            return false;
        //拆分用户提交的验证码
        String imageCode = code.substring(0, 5);
        String projectName = code.substring(5, 8);
        String hour = code.substring(8, 10);

        //获取当前的小时数
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String strCurHour = null;
        if (currentHour < 10) {
            strCurHour = '0' + String.valueOf(currentHour);
        } else {
            strCurHour = String.valueOf(currentHour);
        }

        try {
            boolean flag = CaptchaUtil.validate("http://imagecode.meiliwan.com/captcha/validate", imageCode, sessionId, 2000);
            if (flag && projectName.equals("mlw") && strCurHour.equals(hour)) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @RequestMapping(value = "/loginAjax")
    public String loginAjax(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        if (isHandle < 1) {
            return "/loginAjax";
        } else {
            String userId = ServletRequestUtils.getStringParameter(request, "username", "");
            String userPwd = ServletRequestUtils.getStringParameter(request, "password", "");
            if (StringUtils.isNotEmpty(userId) && StringUtils.isNotEmpty(userPwd)) {
                BksAdmin admin = RoleServiceClient.adminLogin(userId, userPwd);
                if (admin != null) {
                    if (admin.getState() != 0) {
                        return StageHelper.writeDwzSignal("200", "账号状态异常不能登录", "", StageHelper.DWZ_CLOSE_CURRENT, "", response);
                    }
                    LoginUser loginUser = loginInit(admin, request, response);
                    if (loginUser != null) {
                        request.getSession().setAttribute(StageHelper.loginUserSessionName, loginUser);
                        return StageHelper.writeDwzSignal("200", "登陆成功", "", StageHelper.DWZ_CLOSE_CURRENT, "", response);
                    }
                }
            }
        }
        return StageHelper.writeDwzSignal("300", "账号密码有误", "", StageHelper.DWZ_FORWARD, "/loginAjax", response);
    }

    @RequestMapping(value = "/logout")
    public String logout(HttpServletRequest request) {

        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);
        Cache<Integer, LoginUser> cache = (Cache<Integer, LoginUser>) request.getSession().getServletContext().getAttribute("onlineUser");
        if (loginUser != null) {
            cache.asMap().remove(loginUser.getBksAdmin().getAdminId());

            addLoginOptLog(loginUser, request, -3188, "退出操作", "loginOut");

        }

        request.getSession().invalidate();
        return "redirect:/";
    }

    private LoginUser loginInit(BksAdmin admin, HttpServletRequest request, HttpServletResponse response) {
        if (admin != null) {
            LoginUser loginUser = new LoginUser();
            loginUser.setBksAdmin(admin);
            loginUser.setSessionId(request.getSession().getId());
            loginUser.setLoginIp(StageHelper.getUserIpByRequest(request));
            loginUser.setLoginTime(new Date());

            request.getSession().setAttribute(GlobalNames.SESSIONKEY_ADMIN_NAME, loginUser.getBksAdmin().getAdminName());
            request.getSession().setAttribute(GlobalNames.SESSIONKEY_ADMIN_ID, loginUser.getBksAdmin().getAdminId());

            List<BksMenu> menuMap = RoleServiceClient.getMenuListByAdminId(admin.getAdminId());
            Map<String, BksMenu> map = new HashMap<String, BksMenu>();
            for (BksMenu obj : menuMap) {
                map.put(obj.getRoleKey(), obj);
            }
            loginUser.setMenus(map);
            //当拥有的菜单数量大于0才算可以登录
            if (menuMap != null && menuMap.size() > 0) {
                Cache<Integer, LoginUser> cache = (Cache<Integer, LoginUser>) request.getSession().getServletContext().getAttribute("onlineUser");
                cache.asMap().put(loginUser.getBksAdmin().getAdminId(), loginUser);

                addLoginOptLog(loginUser, request, -9999, "登录操作", "loginOpt");
                // 日志  发MQ

                return loginUser;
            }
        }
        return null;
    }

    private void addLoginOptLog(LoginUser loginUser, HttpServletRequest request, int menuId, String menuName, String roleKey) {
        BksUserOperateLog log = new BksUserOperateLog();
        log.setLoginId(loginUser.getBksAdmin().getAdminId());
        log.setLoginName(loginUser.getBksAdmin().getLoginName());
        log.setIp(WebUtils.getIpAddr(request));
        log.setOperateUrl(request.getRequestURL() + "");
        log.setMenuId(menuId);
        log.setRoleKey(roleKey);
        log.setMenuName(menuName);
        //去除无用参数
        log.setOperateParameter("保密");
        BksUserOperateLogClient.saveBksUserOperateLog(log);
    }

}
