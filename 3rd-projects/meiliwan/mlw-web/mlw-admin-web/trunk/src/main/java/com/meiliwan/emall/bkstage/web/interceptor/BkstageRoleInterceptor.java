package com.meiliwan.emall.bkstage.web.interceptor;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.gson.Gson;
import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.bean.BksUserOperateLog;
import com.meiliwan.emall.bkstage.client.BksUserOperateLogClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.web.WebUtils;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

/**
 * 用于权限的 拦截器
 *
 * @author Sean
 */
public class BkstageRoleInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        //这个是获取 菜单Id用于刷新指定的Tab
        String mId = ServletRequestUtils.getStringParameter(request, "mid", "");
        //获取sessionObj
        LoginUser loginUser = (LoginUser) request.getSession().getAttribute(StageHelper.loginUserSessionName);
        if (loginUser != null) {
            //受管理的控制
            Cache<Integer, LoginUser> cache = (Cache<Integer, LoginUser>) request.getSession().getServletContext().getAttribute("onlineUser");
            LoginUser mangerLogin = cache.asMap().get(loginUser.getBksAdmin().getAdminId());
            //判断管理Session对象
            if (mangerLogin != null && mangerLogin.getSessionId().equals(request.getSession().getId())) {
                mangerLogin.setLastOperatorTime(new Date());
                mangerLogin.setLastRequestURI(request.getRequestURI());
            } else {
                StageHelper.writeDwzSignal("301", "您的账号在其他地方登录,或没有权限，请重新登陆。", mId, StageHelper.DWZ_FORWARD, "/loginAjax?mid=" + mId, response);
                request.getSession().invalidate();
                return false;
            }
            for (BksMenu menu : loginUser.getMenus().values()) {
                String requestURI = menu.getUrl();
                if (!Strings.isNullOrEmpty(requestURI)) {
                    if (requestURI.indexOf("?") > 0) {
                        requestURI = requestURI.substring(0, requestURI.indexOf("?"));
                    }
                }
                if (request.getRequestURI().equals(requestURI)) {
                    loginUser.optCountIncrement();
                    writeUserOperateMessage(loginUser, request, menu);
                    request.setAttribute("curRoleKey", menu.getRoleKey());
                    return true;
                }
            }

            StageHelper.writeDwzSignal("301", "没有访问权限", mId, StageHelper.DWZ_CLOSE_CURRENT, "", response);
            return false;
        }
        StageHelper.writeDwzSignal("301", "会话超时", mId, StageHelper.DWZ_FORWARD, "/loginAjax?mid=" + mId, response);
        return false;
    }

    //写用户操作记录
    private void writeUserOperateMessage(LoginUser loginUser, HttpServletRequest request, BksMenu menu) {
        BksUserOperateLog log = new BksUserOperateLog();
        log.setLoginId(loginUser.getBksAdmin().getAdminId());
        log.setLoginName(loginUser.getBksAdmin().getLoginName());
        log.setIp(WebUtils.getIpAddr(request));
        log.setMenuId(menu.getMenuId());
        log.setMenuName(menu.getName());
        log.setRoleKey(menu.getRoleKey());
        log.setOperateUrl(request.getRequestURL() + "");
        //去除无用参数
        log.setOperateParameter(new Gson().toJson(request.getParameterMap()));
        BksUserOperateLogClient.saveBksUserOperateLog(log);
    }
}
