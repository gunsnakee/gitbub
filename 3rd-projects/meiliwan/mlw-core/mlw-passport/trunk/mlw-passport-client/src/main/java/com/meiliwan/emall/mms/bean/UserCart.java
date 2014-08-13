package com.meiliwan.emall.mms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class UserCart extends BaseEntity {
    private String cartId;

    private Integer userId;


    private Integer cartCount;

    private Date createTime;

    private Date updateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getId() {
        return this.cartId;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}