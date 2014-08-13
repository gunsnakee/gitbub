package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.bean.BksRole;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.html.bkstage.RoleTreeMenuTemplate;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.util.ArrayUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Sean on 13-6-4.
 */
@Controller("bkstageRoleController")
@RequestMapping("/bkstage/role")
public class RoleController {

    /**
     * 权限添加
     */
    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            BksRole role = new BksRole();
            role.setName(ServletRequestUtils.getStringParameter(request, "roleName", null));
            role.setDepartment(ServletRequestUtils.getStringParameter(request, "department", null));
            role.setDescription(ServletRequestUtils.getStringParameter(request, "roleDesc", null));
            role.setState(ServletRequestUtils.getIntParameter(request, "state", -1));
            boolean isSuc = RoleServiceClient.addRole(role);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "51", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/role/list", response);
        } else {
            return "/bkstage/role/add";
        }
    }

    /**
     * 菜单列表
     */
    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        PageInfo pageInfo = StageHelper.getPageInfo(request, "department", " asc");
        BksRole role = new BksRole();
        //强制数量
        pageInfo.setPagesize(200);
        role.setName(ServletRequestUtils.getStringParameter(request, "roleName", null));
        PagerControl<BksRole> pc = RoleServiceClient.getRolePager(role, pageInfo);
        List l = pc.getEntityList();
        
//        for (Object o : l) {
//        	System.out.println(o.getClass());
//        }
        
        model.addAttribute("pc", pc);
        model.addAttribute("roleName", role.getName());
        return "/bkstage/role/list";
    }

    /**
     * 菜单更新
     */
    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            BksRole role = new BksRole();
            role.setRoleId(ServletRequestUtils.getIntParameter(request, "roleId", -1));
            role.setName(ServletRequestUtils.getStringParameter(request, "roleName", null));
            role.setDescription(ServletRequestUtils.getStringParameter(request, "roleDesc", null));
            role.setDepartment(ServletRequestUtils.getStringParameter(request, "department", null));
            role.setState(ServletRequestUtils.getIntParameter(request, "state", -1));
            boolean isSuc = RoleServiceClient.editRole(role);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "51", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/role/list", response);
        } else {
            BksRole bksRole = RoleServiceClient.getBksRole(ServletRequestUtils.getIntParameter(request, "roleId", -1));
            model.addAttribute("obj", bksRole);
            return "/bkstage/role/edit";
        }
    }


    /**
     * 菜单更新
     */
    @RequestMapping("/editState")
    public String editState(HttpServletRequest request, HttpServletResponse response, Model model) {
        boolean isSuc = false;
        //判断是否访问页面还是处理 添加信息
        BksRole role = new BksRole();
        role.setRoleId(ServletRequestUtils.getIntParameter(request, "roleId", -1));
        role.setState(ServletRequestUtils.getIntParameter(request, "state", -1));
        isSuc = RoleServiceClient.editRole(role);
        return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "51", StageHelper.DWZ_FORWARD, "/bkstage/role/list", response);
    }

    /**
     * 菜单更新
     */
    @RequestMapping("/menu")
    public String menu(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        BksRole bksRole = RoleServiceClient.getBksRole(ServletRequestUtils.getIntParameter(request, "roleId", -1));
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            String roleIds = ServletRequestUtils.getStringParameter(request, "userSelectIds", "");
            String[] ids = roleIds.split(",");
            boolean isSuc = RoleServiceClient.updateRoleMenuListById(ArrayUtils.stringToInts(ids), bksRole.getRoleId());
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "设置角色菜单成功" : "操作失败请联系管理员", "51", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/role/list", response);
        } else {
            BksMenu bks = new BksMenu();
            bks.setState(0);
            List<BksMenu> roleMenu = RoleServiceClient.getMenuListByBoleId(bksRole.getRoleId());
            List<BksMenu> allMenu = RoleServiceClient.getMenuListByBksMenu(bks);
            String roleTreeHtml = RoleTreeMenuTemplate.getMenuString(allMenu, 0, roleMenu, "tree treeFolder treeCheck expand");
            model.addAttribute("obj", bksRole);
            model.addAttribute("allMenu", roleTreeHtml);
            return "/bkstage/role/menu";
        }
    }

    @RequestMapping("/del")
    public void del(HttpServletRequest request, HttpServletResponse response) {
        int roleId = ServletRequestUtils.getIntParameter(request, "roleId", -1);
        if (roleId > 0) {
            boolean isSuc = RoleServiceClient.delRoleById(roleId);
            StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "51", StageHelper.DWZ_FORWARD, "/bkstage/role/list", response);
        }

    }
}
