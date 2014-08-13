package com.meiliwan.emall.bkstage.service;


import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.annotation.Param;
import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.bkstage.bean.BksAdminRoleKey;
import com.meiliwan.emall.bkstage.bean.BksMenu;
import com.meiliwan.emall.bkstage.bean.BksRole;
import com.meiliwan.emall.bkstage.bean.BksRoleMenuKey;
import com.meiliwan.emall.bkstage.dao.BksAdminDao;
import com.meiliwan.emall.bkstage.dao.BksAdminRoleDao;
import com.meiliwan.emall.bkstage.dao.BksMenuDao;
import com.meiliwan.emall.bkstage.dao.BksRoleDao;
import com.meiliwan.emall.bkstage.dao.BksRoleMenuDao;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.commons.util.EncryptTools;
import com.meiliwan.emall.service.BaseService;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;


/**
 * Created by Sean on 13-5-27.
 */
@Service
public class RoleService extends DefaultBaseServiceImpl implements BaseService {
    private static final long serialVersionUID = -6537398541605859342L;
    @Autowired
    private BksAdminDao bksAdminDao;
    @Autowired
    private BksRoleDao bksRoleDao;
    @Autowired
    private BksMenuDao bksMenuDao;
    @Autowired
    private BksAdminRoleDao bksAdminRoleDao;
    @Autowired
    private BksRoleMenuDao bksRoleMenuDao;

    @IceServiceMethod
    public void userLogin(JsonObject resultObj, String userId, String userPwd) {
        BksAdmin admin = new BksAdmin();
        admin.setLoginName(userId);
        BksAdmin dbObj = bksAdminDao.getEntityByObj(admin);
        if (dbObj != null) {
            String key = Hashing.sha512().hashString(admin.getLoginName() + userPwd).toString();
            if (dbObj.getAdminPwd().equals(key)) {
                dbObj.setAdminPwd("");
                addToResult(dbObj, resultObj);
            }
        }
    }

    @IceServiceMethod
    public void deleteAdminById(JsonObject resultObj, int adminId) {
        BksAdmin dbObj = new BksAdmin();
        dbObj.setIsDel(-1);
        dbObj.setAdminId(adminId);
        try {
            bksAdminDao.update(dbObj);
            addToResult(true, resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            addToResult(false, resultObj);
        }
    }

    @IceServiceMethod
    public void deleteRoleById(JsonObject resultObj, int roleId) {
        BksRole dbObj = new BksRole();
        dbObj.setIsDel(-1);
        dbObj.setRoleId(roleId);
        try {
            bksRoleDao.update(dbObj);
            addToResult(true, resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            addToResult(false, resultObj);
        }
    }

    @IceServiceMethod
    public void updateResetPwd(JsonObject resultObj, int adminId, @Param("newPwd") String newPwd) {
        BksAdmin dbObj = bksAdminDao.getEntityById(adminId);
        if (dbObj != null) {
            String key = Hashing.sha512().hashString(dbObj.getLoginName() + newPwd).toString();
            dbObj.setAdminPwd(key);
            try {
                bksAdminDao.update(dbObj);
                addToResult(true, resultObj);
            } catch (Exception e) {
                e.printStackTrace();
                addToResult(false, resultObj);
            }
        }
    }

    @IceServiceMethod
    public void getAdminById(JsonObject resultObj, int adminId) {
        addToResult(bksAdminDao.getEntityById(adminId), resultObj);
    }

    @IceServiceMethod
    public void getAdminList(JsonObject resultObj, @Param("adminObj") BksAdmin adminObj, @Param("pageInfo") PageInfo pageInfo) {
        if (adminObj != null) {
            addToResult(bksAdminDao.getPagerByObj(adminObj, pageInfo, null), resultObj);
        }
    }

    @IceServiceMethod
    public void saveAdmin(JsonObject resultObj, BksAdmin adminObj) {
        if (adminObj != null) {
            //判断是否有相同的登录名
            BksAdmin admin = new BksAdmin();
            admin.setLoginName(adminObj.getLoginName());
            BksAdmin dbObj = bksAdminDao.getEntityByObj(admin);
            if (dbObj != null) {
                addToResult(false, resultObj);
                return;
            }

            String key = Hashing.sha512().hashString(adminObj.getLoginName() + adminObj.getAdminPwd()).toString();
            adminObj.setAdminPwd(key);
            adminObj.setCreateTime(new Date());
            adminObj.setUpdateTime(new Date());
            try {
                bksAdminDao.insert(adminObj);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
            }
        }
    }

    /**
     * 更改管理员使用状态
     *
     * @param resultObj
     */
    @IceServiceMethod
    public void updateAdminState(JsonObject resultObj, @Param("adminId") int adminId, @Param("state") int state) {
        BksAdmin updateAdmin = new BksAdmin();
        updateAdmin.setAdminId(adminId);
        updateAdmin.setState(state);
        updateAdmin.setUpdateTime(new Date());
        try {
            bksAdminDao.update(updateAdmin);
            addToResult("true", resultObj);
        } catch (Exception e) {
            addToResult("false", resultObj);
        }

    }

    /**
     * 更改管理员信息
     *
     * @param admin
     * @param resultObj
     */
    @IceServiceMethod
    public void updateAdminInfo(JsonObject resultObj, @Param("admin") BksAdmin admin) {
        BksAdmin updateAdmin = new BksAdmin();
        updateAdmin.setAdminId(admin.getId());
        updateAdmin.setAdminName(admin.getAdminName());
        updateAdmin.setAdminEmail(admin.getAdminEmail());
        updateAdmin.setAdminPhone(admin.getAdminPhone());
        updateAdmin.setDepartment(admin.getDepartment());
        updateAdmin.setUpdateTime(new Date());
        updateAdmin.setState(admin.getState());
        try {
            bksAdminDao.update(updateAdmin);
            addToResult(true, resultObj);
        } catch (Exception e) {
            e.printStackTrace();
            addToResult(true, resultObj);
        }
    }

    @IceServiceMethod
    public void getAdminPager(JsonObject resultObj, @Param("admin") BksAdmin bksAdmin, @Param("pageInfo") PageInfo pageInfo) {
        if (bksAdmin != null && pageInfo != null) {
            if (bksAdmin.getAdminName() != null && !Strings.isNullOrEmpty(bksAdmin.getAdminName().trim())) {
                bksAdmin.setAdminName("%" + bksAdmin.getAdminName().trim() + "%");
            } else {
                bksAdmin.setAdminName(null);
            }
            addToResult(bksAdminDao.getPagerByObj(bksAdmin, pageInfo, null, pageInfo.getOrderBySql()), resultObj);
        }
    }

    @IceServiceMethod
    public void saveMenu(JsonObject resultObj, @Param("menu") BksMenu menu) {
        if (menu != null) {
            try {
                bksMenuDao.insert(menu);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
            }
        }

    }

    @IceServiceMethod
    public void updateMenu(JsonObject resultObj, @Param("menu") BksMenu menu) {
        if (menu != null) {
            try {
                bksMenuDao.update(menu);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
            }
        }
    }

    @IceServiceMethod
    public void getMenuPager(JsonObject resultObj, @Param("menu") BksMenu bksMenu, @Param("pageInfo") PageInfo pageInfo) {
        if (bksMenu != null && pageInfo != null) {
            addToResult(bksMenuDao.getPagerByObj(bksMenu, pageInfo, null), resultObj);
        }
    }

    @IceServiceMethod
    public void getMenuList(JsonObject resultObj, @Param("menu") BksMenu bksMenu) {
        if (bksMenu != null) {
            List<BksMenu> rList = bksMenuDao.getListByObj(bksMenu, null);
            Collections.sort(rList);
            addToResult(rList, resultObj);
        }
    }

    @IceServiceMethod
    public void getMenu(JsonObject resultObj, int menuId) {
        addToResult(bksMenuDao.getEntityById(menuId), resultObj);
    }

    @IceServiceMethod
    public void getMenuListByAdminId(JsonObject resultObj, @Param("adminId") int adminId) {
        BksAdminRoleKey adminRoleKey = new BksAdminRoleKey();
        adminRoleKey.setAdminId(adminId);
        List<BksAdminRoleKey> adminRoleKeyList = bksAdminRoleDao.getListByObj(adminRoleKey, null);
        List<BksMenu> rList = new ArrayList<BksMenu>();
        //    Map<String, BksMenu> returnMap = new HashMap<String, BksMenu>();
        for (BksAdminRoleKey key : adminRoleKeyList) {

            BksRole role = bksRoleDao.getEntityById(key.getRoleId());
            if (role == null) continue;

            BksRoleMenuKey roleMenuKey = new BksRoleMenuKey();
            roleMenuKey.setRoleId(role.getRoleId());
            List<BksRoleMenuKey> roleMenuKeys = bksRoleMenuDao.getListByObj(roleMenuKey, null);
            for (BksRoleMenuKey menukey : roleMenuKeys) {
                BksMenu menu = bksMenuDao.getEntityById(menukey.getMenuId());
                if (menu != null && menu.getState() == 0) {
                    if (!rList.contains(menu)) {
                        rList.add(menu);
                    }
                }
            }
        }
        Collections.sort(rList);
        addToResult(rList, resultObj);

    }


    @IceServiceMethod
    public void getMenuListByIdAndModule(JsonObject resultObj,int adminId,String module){
    	 BksAdminRoleKey adminRoleKey = new BksAdminRoleKey();
         adminRoleKey.setAdminId(adminId);
         List<BksAdminRoleKey> adminRoleKeyList = bksAdminRoleDao.getListByObj(adminRoleKey, null);
         List<BksMenu> rList = new ArrayList<BksMenu>();
         //    Map<String, BksMenu> returnMap = new HashMap<String, BksMenu>();
         for (BksAdminRoleKey key : adminRoleKeyList) {

             BksRole role = bksRoleDao.getEntityById(key.getRoleId());
             if (role == null) continue;

             BksRoleMenuKey roleMenuKey = new BksRoleMenuKey();
             roleMenuKey.setRoleId(role.getRoleId());
             List<BksRoleMenuKey> roleMenuKeys = bksRoleMenuDao.getListByObj(roleMenuKey, null);
             for (BksRoleMenuKey menukey : roleMenuKeys) {
                 BksMenu menu = bksMenuDao.getEntityById(menukey.getMenuId());
                 if (menu != null && menu.getState() == 0 && module.equals(menu.getModel())) {
                     if (!rList.contains(menu)) {
                         rList.add(menu);
                     }
                 }
             }
         }
         Collections.sort(rList);
         addToResult(rList, resultObj);
    }

    @IceServiceMethod
    public void checkMenuByIdAndRoleStr(JsonObject resultObj,int adminId,String roleStr){
    	 BksAdminRoleKey adminRoleKey = new BksAdminRoleKey();
         adminRoleKey.setAdminId(adminId);
         List<BksAdminRoleKey> adminRoleKeyList = bksAdminRoleDao.getListByObj(adminRoleKey, null);

         boolean isExist = false;
         for (BksAdminRoleKey key : adminRoleKeyList) {
        	 if(isExist)
        		 break;

    		 BksRole role = bksRoleDao.getEntityById(key.getRoleId());
             if (role == null) continue;

             BksRoleMenuKey roleMenuKey = new BksRoleMenuKey();
             roleMenuKey.setRoleId(role.getRoleId());
             List<BksRoleMenuKey> roleMenuKeys = bksRoleMenuDao.getListByObj(roleMenuKey, null);
             for (BksRoleMenuKey menukey : roleMenuKeys) {
                 BksMenu menu = bksMenuDao.getEntityById(menukey.getMenuId());
                 if (menu != null && menu.getState() == 0 && menu.getRoleKey().equals(roleStr)) {
                	 isExist = true;
                	 break;
                 }
             }

         }
         addToResult(isExist, resultObj);
    }

    @IceServiceMethod
    public void getMenuListByRoleId(JsonObject resultObj, int roleId) {
        List<BksMenu> rList = new ArrayList<BksMenu>();
        BksRoleMenuKey roleMenuKey = new BksRoleMenuKey();
        roleMenuKey.setRoleId(roleId);
        List<BksRoleMenuKey> roleMenuKeys = bksRoleMenuDao.getListByObj(roleMenuKey, null);
        for (BksRoleMenuKey menukey : roleMenuKeys) {
            BksMenu menu = bksMenuDao.getEntityById(menukey.getMenuId());
            if (menu != null && menu.getState() == 0) {
                if (!rList.contains(menu)) {
                    rList.add(menu);
                }
            }
        }
        addToResult(rList, resultObj);
    }

    @IceServiceMethod
    public void saveRole(JsonObject resultObj, @Param("role") BksRole roleObj) {
        if (roleObj != null) {
            try {
                bksRoleDao.insert(roleObj);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
                e.printStackTrace();
            }
        }
    }

    @IceServiceMethod
    public void updateRole(JsonObject resultObj, @Param("role") BksRole roleObj) {
        if (roleObj != null) {
            try {
                bksRoleDao.update(roleObj);
                addToResult(true, resultObj);
            } catch (Exception e) {
                addToResult(false, resultObj);
            }
        }
    }

    @IceServiceMethod
    public void getRolePager(JsonObject resultObj, @Param("role") BksRole bksRole, @Param("pageInfo") PageInfo pageInfo) {
        if (bksRole != null && pageInfo != null) {
            if (bksRole.getName() != null && !Strings.isNullOrEmpty(bksRole.getName().trim())) {
                bksRole.setName("%" + bksRole.getName().trim() + "%");
            } else {
                bksRole.setName(null);
            }
            addToResult(bksRoleDao.getPagerByObj(bksRole, pageInfo, null, pageInfo.getOrderBySql()), resultObj);
        }
    }

    @IceServiceMethod
    public void getRole(JsonObject resultObj, @Param("roleId") int roleId) {
        addToResult(bksRoleDao.getEntityById(roleId), resultObj);
    }

    @IceServiceMethod
    public void getRoleListByAdminId(JsonObject resultObj, @Param("adminId") int adminId) {
        BksAdminRoleKey adminRoleKey = new BksAdminRoleKey();
        adminRoleKey.setAdminId(adminId);
        List<BksAdminRoleKey> adminRoleKeyList = bksAdminRoleDao.getListByObj(adminRoleKey, null);
        List<BksRole> list = new ArrayList<BksRole>();
        for (BksAdminRoleKey key : adminRoleKeyList) {
            BksRole role = bksRoleDao.getEntityById(key.getRoleId());
            if (role != null)
                list.add(role);
        }
        addToResult(list, resultObj);
    }

    @IceServiceMethod
    public void saveRoleMenu(JsonObject resultObj, @Param("list") List<BksRoleMenuKey> list) {
        if (list != null && list.size() > 0) {
            for (BksRoleMenuKey key : list) {
                bksRoleMenuDao.insert(key);
            }
            addToResult(true, resultObj);
        }
    }

    @IceServiceMethod
    public void updateRoleMenuListById(JsonObject resultObj, @Param("list") int[] list, @Param("roleId") int roleId) {
        //先删除后添加
        BksRoleMenuKey delQuery = new BksRoleMenuKey();
        delQuery.setRoleId(roleId);
        bksRoleMenuDao.delete(delQuery);
        if (list != null && list.length > 0) {
            for (int key : list) {
                BksRoleMenuKey nObj = new BksRoleMenuKey();
                nObj.setRoleId(roleId);
                nObj.setMenuId(key);
                bksRoleMenuDao.insert(nObj);
            }
            addToResult(true, resultObj);
        }
    }

    @IceServiceMethod
    public void getRoleMenuListByEntity(JsonObject resultObj, @Param("roleMenuObj") BksRoleMenuKey roleMenuObj) {
        addToResult(bksRoleMenuDao.getListByObj(roleMenuObj, null), resultObj);
    }

    @IceServiceMethod
    public void saveAdminRole(JsonObject resultObj, @Param("list") List<BksAdminRoleKey> list) {
        if (list != null && list.size() > 0) {
            for (BksAdminRoleKey key : list) {
                bksAdminRoleDao.insert(key);
            }
            addToResult(true, resultObj);
        }
        addToResult(false, resultObj);
    }

    @IceServiceMethod
    public void updateAdminRoleListById(JsonObject resultObj, int[] roleIds, @Param("adminId") int adminId) {
        //先删除后添加
        BksAdminRoleKey delQuery = new BksAdminRoleKey();
        delQuery.setAdminId(adminId);
        bksAdminRoleDao.delete(delQuery);
        if (roleIds.length > 0) {
            for (int key : roleIds) {
                BksAdminRoleKey temp = new BksAdminRoleKey();
                temp.setRoleId(key);
                temp.setAdminId(adminId);
                bksAdminRoleDao.insert(temp);
            }
        }
        addToResult(true, resultObj);
    }

    @IceServiceMethod
    public void getAdminRoleListByEntity(JsonObject resultObj, @Param("adminRole") BksAdminRoleKey adminRole) {
        addToResult(bksAdminRoleDao.getListByObj(adminRole, null), resultObj);
    }

    /**
     * 修改财务退款密码
     *
     * @param resultObj
     * @param adminId
     * @param pwd
     */
    public void updateFinancePwd(JsonObject resultObj, int adminId, String pwd) {
        //进行加密
        String newPwd = getEncryptCode(pwd);
        int count = bksAdminDao.updateFincePwd(adminId, newPwd);
        if (count > 0) {
            addToResult(true, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 校验财务退款密码
     *
     * @param resultObj
     * @param adminId
     * @param pwd
     */
    public void checkFinancePwd(JsonObject resultObj, int adminId, String pwd) {
        BksAdmin admin = bksAdminDao.getEntityById(adminId);
        String oldPwd = getEncryptCode(pwd);
        if (admin.getFinancePwd() != null && oldPwd != null && admin.getFinancePwd().equals(oldPwd)) {
            addToResult(true, resultObj);
        } else {
            addToResult(false, resultObj);
        }
    }

    /**
     * 用MD5构造加密code
     *
     * @param pwd
     * @return
     */
    private String getEncryptCode(String pwd) {

        String enCode = EncryptTools.EncryptByMD5(pwd);
        //进行两次加密
        try {
            String ssCode = ConfigOnZk.getInstance().getValue("web/system.properties", "SECURITY_SUBMIT_ENCCODE");
            return EncryptTools.EncryptByMD5(enCode + ssCode);
        } catch (BaseException e) {
            e.printStackTrace();
        }
        return enCode;
    }

    public static enum Action {
        userLogin, resetPwd, saveAdmin, getAdminList, updateAdminState, updateAdmin, getMenuListByAdminId
    }

}
