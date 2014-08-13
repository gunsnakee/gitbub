package com.meiliwan.emall.bkstage.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.bean.BksRole;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

/**
 * Created by Sean on 13-6-3.
 */
public class RoleServiceClient {

    public static final String roleServiceBeanName = "roleService";
    private static final Logger LOG = LoggerFactory.getLogger(RoleServiceClient.class);

    public static BksAdmin adminLogin(String loginName, String loginPwd) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/userLogin", loginName, loginPwd));
        return new Gson().fromJson(obj.get("resultObj"), BksAdmin.class);
    }

    public static BksAdmin getBksAdmin(int adminId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getAdminById", adminId));
        return new Gson().fromJson(obj.get("resultObj"), BksAdmin.class);
    }

    public static boolean resetPwd(int adminId, String newLoginPwd) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateResetPwd", adminId, newLoginPwd));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean delAdminById(int adminId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/deleteAdminById", adminId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean delRoleById(int roleId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/deleteRoleById", roleId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean editAdminState(int adminId, int state) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateAdminState", adminId, state));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean addAdmin(BksAdmin admin) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/saveAdmin", admin));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean editAdminInfo(BksAdmin admin) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateAdminInfo", admin));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean setAdminPwd(String adminId, String adminPwd) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/resetPwd", adminId, adminPwd));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<BksMenu> getMenuListByAdminId(int adminId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getMenuListByAdminId", adminId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksMenu>>() {
        }.getType());
    }

    public static List<BksMenu> getMenuListByIdAndModule(int adminId,String moduleName) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getMenuListByIdAndModule", adminId,moduleName));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksMenu>>() {
        }.getType());
    }

    public static boolean checkMenuByIdAndRoleStr(int adminId,String roleStr) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/checkMenuByIdAndRoleStr", adminId,roleStr));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<BksMenu> getMenuListByBksMenu(BksMenu menu) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getMenuList", menu));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksMenu>>() {
        }.getType());
    }

    public static boolean updateRoleMenuListById(int[] menuIds, int roleId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateRoleMenuListById", menuIds, roleId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<BksMenu> getMenuListByBoleId(int roleId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getMenuListByRoleId", roleId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksMenu>>() {
        }.getType());
    }

    public static PagerControl<BksAdmin> getPagerBksAdmin(BksAdmin admin, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getAdminPager", admin, pageInfo));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BksAdmin>>() {
        }.getType());
    }

    public static PagerControl<BksRole> getRolePager(BksRole role, PageInfo pageInfo) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getRolePager", role, pageInfo));

        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<PagerControl<BksRole>>() {
        }.getType());
    }

    public static boolean addRole(BksRole role) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/saveRole", role));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean editRole(BksRole role) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateRole", role));
        return obj.get("resultObj").getAsBoolean();
    }

    public static BksRole getBksRole(int roleId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getRole", roleId));
        return new Gson().fromJson(obj.get("resultObj"), BksRole.class);
    }

    public static BksMenu getMenuById(int menuId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getMenu", menuId));
        return new Gson().fromJson(obj.get("resultObj"), BksMenu.class);
    }

    public static boolean addMenu(BksMenu menu) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/saveMenu", menu));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean editMenu(BksMenu menu) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateMenu", menu));
        return obj.get("resultObj").getAsBoolean();
    }

    public static boolean updateAdminRoleListById(int[] roleIds, int adminId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateAdminRoleListById", roleIds, adminId));
        return obj.get("resultObj").getAsBoolean();
    }

    public static List<BksRole> getRoleListByAdminId(int adminId) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/getRoleListByAdminId", adminId));
        return new Gson().fromJson(obj.get("resultObj"), new TypeToken<List<BksRole>>() {
        }.getType());
    }

    /**
     * 修改财务退款密码
     * @param adminId
     * @param pwd
     * @return
     */
    public static boolean updateFinancePwd(int adminId, String pwd) {
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/updateFinancePwd", adminId,pwd));
        return obj.get("resultObj").getAsBoolean();
    }

    /**
     * 校验财务退款密码
     * @param adminId
     * @param pwd
     * @return
     */
    public static boolean checkFinancePwd(int adminId, String pwd){
        JsonObject obj = IceClientTool.sendMsg(IceClientTool.BKSTAGE_ICE_SERVICE,
                JSONTool.buildParams("roleService/checkFinancePwd", adminId,pwd));
        return obj.get("resultObj").getAsBoolean();
    }

    public static void main(String args[]) {
        System.out.println(adminLogin("admin", "admin"));
    }

}
