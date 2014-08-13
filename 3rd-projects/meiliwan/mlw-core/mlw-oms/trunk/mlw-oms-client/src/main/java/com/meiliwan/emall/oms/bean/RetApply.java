package com.meiliwan.emall.oms.bean;

import com.meiliwan.emall.commons.bean.GlobalNames;
import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;

public class RetApply extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -4394576908008623864L;

	private String retordItemId;

    private String retordId;

    private Integer proId;

    private String proName;

    private Short proCount;

    private String oldOrdId;

    private String oldOrdItemId;

    private String applyComments;

    private double retPayAmount;
    private double retPayFare;
    private double retTotalAmount;

    private String retType;
    private String applyRetType;
    
    private String returnReason;

    private Short needRetpro;

    private Date retTime;

    private Integer uid;

    private String userName;

    private String province;

    private String provinceCode;

    private String city;

    private String cityCode;

    private String county;

    private String countyCode;

    private String town;

    private String townCode;

    private String detailAddr;

    private String recvName;

    private String mobile;

    private String phone;

    private Date updateTime=new Date();

    private Date createTime;

    private Short state=GlobalNames.STATE_VALID;

    private String remark;

    private String evidencePic;

    
    public String getApplyRetType() {
		return applyRetType;
	}

	public void setApplyRetType(String applyRetType) {
		this.applyRetType = applyRetType;
	}

	public double getRetPayFare() {
		return retPayFare;
	}

	public void setRetPayFare(double retPayFare) {
		this.retPayFare = retPayFare;
	}

	public String getId() {
        return retordItemId;
    }

    public String getRetordItemId() {
        return retordItemId;
    }

    public void setRetordItemId(String retordItemId) {
        this.retordItemId = retordItemId == null ? null : retordItemId.trim();
    }

    public String getRetordId() {
        return retordId;
    }

    public void setRetordId(String retordId) {
        this.retordId = retordId == null ? null : retordId.trim();
    }

    public Integer getProId() {
        return proId;
    }

    public void setProId(Integer proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName == null ? null : proName.trim();
    }

    public Short getProCount() {
        return proCount;
    }

    public void setProCount(Short proCount) {
        this.proCount = proCount;
    }

    public String getOldOrdId() {
        return oldOrdId;
    }

    public void setOldOrdId(String oldOrdId) {
        this.oldOrdId = oldOrdId == null ? null : oldOrdId.trim();
    }

    public String getOldOrdItemId() {
        return oldOrdItemId;
    }

    public void setOldOrdItemId(String oldOrdItemId) {
        this.oldOrdItemId = oldOrdItemId == null ? null : oldOrdItemId.trim();
    }

    public String getApplyComments() {
        return applyComments;
    }

    public void setApplyComments(String applyComments) {
        this.applyComments = applyComments == null ? null : applyComments.trim();
    }

    public double getRetPayAmount() {
        return retPayAmount;
    }

    public void setRetPayAmount(double retPayAmount) {
        this.retPayAmount = retPayAmount;
    }

    public double getRetTotalAmount() {
        return retTotalAmount;
    }

    public void setRetTotalAmount(double retTotalAmount) {
        this.retTotalAmount = retTotalAmount;
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType == null ? null : retType.trim();
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason == null ? null : returnReason.trim();
    }

    public Short getNeedRetpro() {
        return needRetpro;
    }

    public void setNeedRetpro(Short needRetpro) {
        this.needRetpro = needRetpro;
    }

    public Date getRetTime() {
        return retTime;
    }

    public void setRetTime(Date retTime) {
        this.retTime = retTime;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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

    public String getRecvName() {
        return recvName;
    }

    public void setRecvName(String recvName) {
        this.recvName = recvName == null ? null : recvName.trim();
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getEvidencePic() {
        return evidencePic;
    }

    public void setEvidencePic(String evidencePic) {
        this.evidencePic = evidencePic == null ? null : evidencePic.trim();
    }

}