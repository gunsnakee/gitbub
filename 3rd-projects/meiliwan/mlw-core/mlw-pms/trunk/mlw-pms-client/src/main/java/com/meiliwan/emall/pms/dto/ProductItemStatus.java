package com.meiliwan.emall.pms.dto;

import java.io.Serializable;

/**
 * 购物车或者订单列表商品项，用于保存商品项的每一个有效校验状态
 */
public class ProductItemStatus implements Serializable{

    /**
     * 商品ID
     */
    private Integer proId;

    /**
     * 商品状态
     */
    private Boolean status;

    public ProductItemStatus(Integer proId) {
        this.proId = proId;
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
