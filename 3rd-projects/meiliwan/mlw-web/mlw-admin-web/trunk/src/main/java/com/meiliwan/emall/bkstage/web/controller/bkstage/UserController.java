package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.controller.bkstage.view.LoginUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 平台用户修改密码，修改信息等等...
 * Created by Sean on 13-6-11.
 */
@Controller("bkstageUserController")
@RequestMapping("/bkstage/user/")
public class UserController {


    @RequestMapping("/settingPwd")
    public String settingPwd(HttpServletRequest request, HttpServletResponse response, Model model) {

        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        LoginUser loginUser = StageHelper.getLoginUser(request);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            String oldPwd = ServletRequestUtils.getStringParameter(request, "oldPassword", "");
            if (null == RoleServiceClient.adminLogin(loginUser.getBksAdmin().getLoginName(), oldPwd)) {
                return StageHelper.writeDwzSignal("300", "登陆密码错误", "127", StageHelper.DWZ_CLOSE_CURRENT, "", response);
            }
            String pwd = ServletRequestUtils.getStringParameter(request, "password", null);
            boolean isSuc = RoleServiceClient.resetPwd(loginUser.getBksAdmin().getAdminId(), pwd);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "设置密码成功" : "操作失败请联系管理员", "127", StageHelper.DWZ_CLOSE_CURRENT, "", response);
        } else {
            BksAdmin admin = RoleServiceClient.getBksAdmin(loginUser.getBksAdmin().getAdminId());
            if (admin != null) {
                model.addAttribute("obj", admin);
            }
        }
        return "/bkstage/user/settingPwd";
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            BksAdmin admin = getBksAdmin(request);
            boolean isSuc = RoleServiceClient.editAdminInfo(admin);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "修改信息成功" : "操作失败请联系管理员", "128", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/menu/list", response);
        } else {
            BksAdmin admin = RoleServiceClient.getBksAdmin(StageHelper.getLoginUser(request).getBksAdmin().getAdminId());
            model.addAttribute("obj", admin);
            return "/bkstage/user/edit";
        }
    }

    public BksAdmin getBksAdmin(HttpServletRequest request) {
        BksAdmin admin = new BksAdmin();
        admin.setAdminEmail(ServletRequestUtils.getStringParameter(request, "adminEmail", null));
        admin.setAdminName(ServletRequestUtils.getStringParameter(request, "adminName", null));
        admin.setDepartment(ServletRequestUtils.getStringParameter(request, "adminDep", null));
        admin.setAdminPhone(ServletRequestUtils.getStringParameter(request, "adminPhone", null));
        return admin;
    }


}
