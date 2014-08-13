package com.meiliwan.emall.pms.dto;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by jiawuwu on 14-3-6.
 */
public class ProductPublicParasDto {

    private int spuId;

    private int isCod;
    private int brandId;
    private int placeId;
    private int supplierId;
    private Date updateTime = new Date();

    public int getSpuId() {
        return spuId;
    }

    public void setSpuId(int spuId) {
        this.spuId = spuId;
    }

    public int getIsCod() {
        return isCod;
    }

    public void setIsCod(int isCod) {
        this.isCod = isCod;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
