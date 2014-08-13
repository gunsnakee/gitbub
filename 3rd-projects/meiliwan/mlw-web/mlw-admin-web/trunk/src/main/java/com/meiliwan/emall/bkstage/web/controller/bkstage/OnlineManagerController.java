package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.google.common.cache.Cache;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Sean on 13-6-11.
 * 给管理员 查看当前登录的使用情况，以及账号监控等...
 */
@RequestMapping("/bkstage/onlineManager")
@Controller("bkstageOnlineManagerController")
public class OnlineManagerController {

    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        Cache<Integer, LoginUser> cache = (Cache<Integer, LoginUser>) request.getSession().getServletContext().getAttribute("onlineUser");
        model.addAttribute("onlineUser", cache.asMap().values());
        return "/bkstage/onlineManager/list";
    }

    @RequestMapping("/helpLogout")
    public String helpLogout(HttpServletRequest request, HttpServletResponse response, Model model) {

        int adminId = ServletRequestUtils.getIntParameter(request, "adminId", -1);
        Cache<Integer, LoginUser> cache = (Cache<Integer, LoginUser>) request.getSession().getServletContext().getAttribute("onlineUser");
        cache.asMap().remove(adminId);
        return StageHelper.writeDwzSignal("200", "踢出成功", "120", StageHelper.DWZ_FORWARD, "/bkstage/onlineManager/list", response);


    }

}
