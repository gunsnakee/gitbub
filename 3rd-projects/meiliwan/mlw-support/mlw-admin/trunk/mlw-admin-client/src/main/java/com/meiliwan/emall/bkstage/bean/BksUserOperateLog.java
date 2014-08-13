package com.meiliwan.emall.bkstage.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

/**
 * 后台用户操作日志表
 */


public class BksUserOperateLog extends BaseEntity {
    private Integer id;

    private Date createTime;

    private String ip;

    private Integer menuId;

    private String operateUrl;

    private Integer loginId;

    private String loginName;

    private String operateParameter;

    private String menuName;

    private String roleKey;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getOperateUrl() {
        return operateUrl;
    }

    public void setOperateUrl(String operateUrl) {
        this.operateUrl = operateUrl == null ? null : operateUrl.trim();
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getOperateParameter() {
        return operateParameter;
    }

    public void setOperateParameter(String operateParameter) {
        this.operateParameter = operateParameter == null ? null : operateParameter.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRoleKey() {
        return roleKey;
    }

    public void setRoleKey(String roleKey) {
        this.roleKey = roleKey;
    }
    //为条件查询用
    private Date startTime;
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}