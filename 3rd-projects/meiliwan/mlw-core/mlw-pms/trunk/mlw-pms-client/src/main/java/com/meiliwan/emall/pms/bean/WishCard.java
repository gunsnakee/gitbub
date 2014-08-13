package com.meiliwan.emall.pms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class WishCard extends BaseEntity {
    private static final long serialVersionUID = 6779509468434210199L;
    private Integer id;

    private String proName;

    private String proDescription;

    private String image;

    private Integer sourceCountryCode;

    private String sourceCountryName;

    private String sourceDetailAddr;

    private String recvName;

    private String province;

    private String provinceCode;

    private String city;

    private String cityCode;

    private String county;

    private String countyCode;

    private String town;

    private String townCode;

    private String detailAddr;

    private String mobile;

    private String phone;

    private Date createTime;

    private String remark;

    private Integer uid;

    private String commitUser;

    private Byte status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public String getProDescription() {
        return proDescription;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription == null ? null : proDescription.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getSourceCountryCode() {
        return sourceCountryCode;
    }

    public void setSourceCountryCode(Integer sourceCountryCode) {
        this.sourceCountryCode = sourceCountryCode;
    }

    public String getSourceCountryName() {
        return sourceCountryName;
    }

    public void setSourceCountryName(String sourceCountryName) {
        this.sourceCountryName = sourceCountryName == null ? null : sourceCountryName.trim();
    }

    public String getSourceDetailAddr() {
        return sourceDetailAddr;
    }

    public void setSourceDetailAddr(String sourceDetailAddr) {
        this.sourceDetailAddr = sourceDetailAddr == null ? null : sourceDetailAddr.trim();
    }

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName == null ? null : recvName.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode == null ? null : provinceCode.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode == null ? null : cityCode.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode == null ? null : countyCode.trim();
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town == null ? null : town.trim();
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode == null ? null : townCode.trim();
    }

    public String getDetailAddr() {
        return detailAddr;
    }

    public void setDetailAddr(String detailAddr) {
        this.detailAddr = detailAddr == null ? null : detailAddr.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser == null ? null : commitUser.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}
