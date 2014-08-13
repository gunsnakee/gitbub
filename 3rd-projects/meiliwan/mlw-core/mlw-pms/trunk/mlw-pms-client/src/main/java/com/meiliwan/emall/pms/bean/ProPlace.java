package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.sql.Timestamp;

public class ProPlace extends BaseEntity {
    private static final long serialVersionUID = -5502919348147447190L;
    private Integer placeId;

    private String placeName;

    private String enName;

    private String otherName;

    private String descp;

    private String placeDomain;

    private Timestamp createTime;

    private Integer isDel;

    public Integer getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Integer placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName == null ? null : otherName.trim();
    }

    public String getDescp() {
        return descp;
    }

    public void setDescp(String descp) {
        this.descp = descp == null ? null : descp.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public String getPlaceDomain() {
        return placeDomain;
    }

    public void setPlaceDomain(String placeDomain) {
        this.placeDomain = placeDomain;
    }

    @Override
    public Integer getId() {
        return this.placeId;
    }
}