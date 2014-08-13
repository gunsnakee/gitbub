package com.meiliwan.emall.bkstage.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class BksRole extends BaseEntity {
    private static final long serialVersionUID = 612425256678421277L;
    private Integer roleId;

    private String name;

    private Integer state;

    private String description;

    private String department;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    private Integer isDel;

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer del) {
        isDel = del;
    }


    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    @Override
    public Integer getId() {
        return this.roleId;
    }
}