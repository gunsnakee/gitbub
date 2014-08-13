package com.meiliwan.emall.bkstage.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class BksAdminRoleKey extends BaseEntity {
    private static final long serialVersionUID = 3099452553406200482L;
    private Integer adminId;

    private Integer roleId;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    @Override
    public BksAdminRoleKey getId() {
        return this;
    }
}