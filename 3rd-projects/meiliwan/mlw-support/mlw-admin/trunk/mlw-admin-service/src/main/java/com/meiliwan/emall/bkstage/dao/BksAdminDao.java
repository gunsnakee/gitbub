package com.meiliwan.emall.bkstage.dao;

import com.meiliwan.emall.bkstage.bean.BksAdmin;
import com.meiliwan.emall.core.db.IDao;

public interface BksAdminDao extends IDao<Integer, BksAdmin> {

    /**
     * 修改财务退款密码
     * @param adminId 管理员Id
     * @param pwd 新密码
     */
    int updateFincePwd(int adminId,String pwd);
}