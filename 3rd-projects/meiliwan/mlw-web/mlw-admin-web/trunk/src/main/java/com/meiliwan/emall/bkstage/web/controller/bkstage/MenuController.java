package com.meiliwan.emall.bkstage.web.controller.bkstage;

import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.client.RoleServiceClient;
import com.meiliwan.emall.bkstage.web.StageHelper;
import com.meiliwan.emall.bkstage.web.html.bkstage.RoleTreeMenuTemplate;
import com.meiliwan.emall.commons.util.EncryptTools;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.UUID;

/**
 * Created by Sean on 13-6-4.
 */
@Controller("bkstageMenuController")
@RequestMapping("/bkstage/menu")
public class MenuController {
    @RequestMapping("/list")
    public String list(HttpServletRequest request, HttpServletResponse response, Model model) {
        BksMenu query = new BksMenu();
        query.setState(0);
        List<BksMenu> allMenu = RoleServiceClient.getMenuListByBksMenu(query);
        BksMenu menu = new BksMenu();
        menu.setMenuId(0);
        menu.setParentId(-1);
        menu.setName("菜单根目录");
        menu.setState(0);
        menu.setMenuType(1);
        allMenu.add(menu);
        String roleTreeHtml = RoleTreeMenuTemplate.getMenuString(allMenu, -1, allMenu, "tree treeFolder collapse");
        model.addAttribute("allMenu", roleTreeHtml);
        return "/bkstage/menu/list";
    }

    @RequestMapping("/add")
    public String add(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            BksMenu menu = getBksMenuByRequest(request);
            boolean isSuc = RoleServiceClient.addMenu(menu);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "41", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/menu/list", response);
        } else {
            int menuId = ServletRequestUtils.getIntParameter(request, "parentId", -1);
            BksMenu menu = RoleServiceClient.getMenuById(menuId);
            model.addAttribute("uuid", EncryptTools.EncryptByMD5(UUID.randomUUID().toString()));

            model.addAttribute("parentMenu", menu);
            return "/bkstage/menu/add";
        }
    }

    @RequestMapping("/edit")
    public String edit(HttpServletRequest request, HttpServletResponse response, Model model) {
        int isHandle = ServletRequestUtils.getIntParameter(request, "handle", -1);
        //判断是否访问页面还是处理 添加信息
        if (isHandle > 0) {
            BksMenu menu = getBksMenuByRequest(request);
            boolean isSuc = RoleServiceClient.editMenu(menu);
            return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "操作失败请联系管理员", "41", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/menu/list", response);
        } else {
            int menuId = ServletRequestUtils.getIntParameter(request, "menuId", -1);
            if (menuId < 1) {
                return StageHelper.writeDwzSignal("300", "您没有选择需要编辑的菜单", "41", StageHelper.DWZ_CLOSE_CURRENT, "/bkstage/menu/list", response);
            }
            BksMenu menu = RoleServiceClient.getMenuById(menuId);
            model.addAttribute("obj", menu);
            BksMenu parentMenu = RoleServiceClient.getMenuById(menu.getParentId());
            model.addAttribute("parentMenu", parentMenu);
            return "/bkstage/menu/edit";
        }
    }

    @RequestMapping("/del")
    public String del(HttpServletRequest request, HttpServletResponse response, Model model) {
        boolean isSuc = false;
        int menuId = ServletRequestUtils.getIntParameter(request, "menuId", -1);
        int state = ServletRequestUtils.getIntParameter(request, "state", -1);
        if (menuId > 0) {
            BksMenu query = new BksMenu();
            query.setParentId(menuId);
            query.setState(0);
            List<BksMenu> allMenu = RoleServiceClient.getMenuListByBksMenu(query);
            if (allMenu == null || allMenu.size() == 0) {
                BksMenu menu = new BksMenu();
                menu.setMenuId(menuId);
                menu.setState(state);
                isSuc = RoleServiceClient.editMenu(menu);
            }
        }
        return StageHelper.writeDwzSignal(isSuc ? "200" : "300", isSuc ? "操作成功" : "该菜单有子菜单，不能删除!", "41", StageHelper.DWZ_FORWARD, "/bkstage/menu/list", response);
    }


    public static BksMenu getBksMenuByRequest(HttpServletRequest request) {
        BksMenu bksMenu = new BksMenu();
        int menuId = ServletRequestUtils.getIntParameter(request, "menuId", -1);
        if (menuId > 0)
            bksMenu.setMenuId(menuId);
        int parentId = ServletRequestUtils.getIntParameter(request, "parentId", -1);
        if (parentId > 0)
            bksMenu.setParentId(parentId);
        bksMenu.setAuthorization(ServletRequestUtils.getStringParameter(request, "authorization", null));
        bksMenu.setUrl(ServletRequestUtils.getStringParameter(request, "url", null));
        bksMenu.setModel(ServletRequestUtils.getStringParameter(request, "model", null));
        bksMenu.setName(ServletRequestUtils.getStringParameter(request, "name", null));
        bksMenu.setRoleKey(ServletRequestUtils.getStringParameter(request, "role_key", null));
        bksMenu.setTarget(ServletRequestUtils.getStringParameter(request, "target", null));
        bksMenu.setDescription(ServletRequestUtils.getStringParameter(request, "description", null));
        bksMenu.setMenuType(ServletRequestUtils.getIntParameter(request, "menu_type", 0));
        bksMenu.setSequence(ServletRequestUtils.getIntParameter(request, "sequence", 0));
        bksMenu.setState(ServletRequestUtils.getIntParameter(request, "state", 0));
        return bksMenu;
    }


}
