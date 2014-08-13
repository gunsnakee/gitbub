package com.meiliwan.emall.oms.dto;


import java.io.Serializable;

import com.meiliwan.emall.oms.constant.BuyType;

/**
 * Created with IntelliJ IDEA.
 * User: guangdetang
 * Date: 13-9-11
 * Time: 上午10:16
 * To change this template use File | Settings | File Templates.
 */
public class OrdNoStockDTO implements Serializable{
    private static final long serialVersionUID = 3968463207935847706L;

    /** 购买类型，目前只有普通购买（NUM）、套餐购买（PACK）、活动购买3种购买形式（ACT）*/
    private BuyType buyType;

    /** 该购买类型库存不足ID，如果购买类型是套餐购买，则ID是套餐ID，如果购买是普通购买，则ID是商品ID，如果是活动购买，则ID是活动ID*/
    private Integer id;

    public BuyType getBuyType() {
        return buyType;
    }

    public void setBuyType(BuyType buyType) {
        this.buyType = buyType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdNoStockDTO dto = (OrdNoStockDTO) o;

        if (buyType != dto.buyType) return false;
        if (id != null ? !id.equals(dto.id) : dto.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = buyType != null ? buyType.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
