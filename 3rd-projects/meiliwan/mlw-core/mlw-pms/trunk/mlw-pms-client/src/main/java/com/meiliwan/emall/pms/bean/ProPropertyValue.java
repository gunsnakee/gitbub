package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class ProPropertyValue extends BaseEntity{
    private static final long serialVersionUID = 8214900914483368072L;
    private Integer id;

    private Integer proPropId;

    private String name;

    private Date createTime = new Date();

    /**
     * 用于标识是否已经选中，为了方便商品编辑
     */
    private Integer fill = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProPropId() {
        return proPropId;
    }

    public void setProPropId(Integer proPropId) {
        this.proPropId = proPropId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getFill() {
        return fill;
    }

    public void setFill(Integer fill) {
        this.fill = fill;
    }
}