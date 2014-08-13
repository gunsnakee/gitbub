package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

public class ProProductPropertyKey extends BaseEntity{
    private static final long serialVersionUID = 2256144532785825575L;
    private Integer spuId;

    private Integer proPropId;


    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public Integer getProPropId() {
        return proPropId;
    }

    public void setProPropId(Integer proPropId) {
        this.proPropId = proPropId;
    }

    @Override
    public ProProductPropertyKey getId() {
        return this;
    }
}