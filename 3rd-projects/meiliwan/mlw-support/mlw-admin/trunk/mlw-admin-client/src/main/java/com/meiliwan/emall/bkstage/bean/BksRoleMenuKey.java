package com.meiliwan.emall.bkstage.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class BksRoleMenuKey extends BaseEntity {
    private static final long serialVersionUID = 8027985975958767708L;
    private Integer roleId;

    private Integer menuId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    @Override
    public BksRoleMenuKey getId() {
        return this;
    }
}