package com.meiliwan.emall.oms.bean;

import com.meiliwan.emall.core.bean.BaseEntity;

import java.util.Date;
import java.util.List;

public class Retord extends BaseEntity{
    private static final long serialVersionUID = 1463208569214507973L;

    private String retordOrderId;

    private String oldOrderId;

    private Integer createType;

    private String applyComments;

    private Double retPayFare;

    private Double retPayAmount;

    private Double retPayCompensate;

    private Double retTotalAmount;

    private Double retRealCardAmount;
    
    private Double retRealMlwAmount;

    public Double getRetRealCardAmount() {
        return retRealCardAmount;
    }

    public void setRetRealCardAmount(Double retRealCardAmount) {
        this.retRealCardAmount = retRealCardAmount;
    }

    private String uploadPic;

    private String userRetordDesc;

    private Date retTime;

    private Integer uid;

    private String userName;

    private String adRecvName;

    private String adProvince;

    private String adProvinceCode;

    private String adCity;

    private String adCityCode;

    private String adCounty;

    private String adMobile;

    private String adCountyCode;

    private String adTown;

    private String adTownCode;

    private String adDetailAddr;

    private String adPhone;

    private Date updateTime;

    private Date createTime;

    private Integer state;

    private String remark;

    private String retType;

    private String retPayType;

    private String userRetType;

    private String serviceRetAlipay;
    
    private String serviceRetAlipayName;

    private String serviceRetBank;

    private String serviceRetCardNum;

    private String serviceRetCardName;

    private String serviceRetOpenBank;

    private String retStatus;

    private Integer isEndNode;

    public Integer getIsEndNode() {
        return isEndNode;
    }

    public void setIsEndNode(Integer isEndNode) {
        this.isEndNode = isEndNode;
    }

    public String getRetStatus() {
        return retStatus;
    }

    public void setRetStatus(String retStatus) {
        this.retStatus = retStatus;
    }

    private List<RetordLogs> retLogsList;

    private List<Ordi> ordiList;

    private List<Ordi> oldOrdiList;

    public List<Ordi> getOldOrdiList() {
        return oldOrdiList;
    }

    public void setOldOrdiList(List<Ordi> oldOrdiList) {
        this.oldOrdiList = oldOrdiList;
    }

    public String getRetordOrderId() {
        return retordOrderId;
    }

    public void setRetordOrderId(String retordOrderId) {
        this.retordOrderId = retordOrderId == null ? null : retordOrderId.trim();
    }

    public String getOldOrderId() {
        return oldOrderId;
    }

    public void setOldOrderId(String oldOrderId) {
        this.oldOrderId = oldOrderId == null ? null : oldOrderId.trim();
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public String getApplyComments() {
        return applyComments;
    }

    public void setApplyComments(String applyComments) {
        this.applyComments = applyComments == null ? null : applyComments.trim();
    }

    public Double getRetTotalAmount() {
        return retTotalAmount;
    }

    public void setRetTotalAmount(Double retTotalAmount) {
        this.retTotalAmount = retTotalAmount;
    }

    public Double getRetPayFare() {
        return retPayFare;
    }

    public void setRetPayFare(Double retPayFare) {
        this.retPayFare = retPayFare;
    }

    public Double getRetPayAmount() {
        return retPayAmount;
    }

    public void setRetPayAmount(Double retPayAmount) {
        this.retPayAmount = retPayAmount;
    }

    public Double getRetPayCompensate() {
        return retPayCompensate;
    }

    public void setRetPayCompensate(Double retPayCompensate) {
        this.retPayCompensate = retPayCompensate;
    }

    public String getUploadPic() {
        return uploadPic;
    }

    public void setUploadPic(String uploadPic) {
        this.uploadPic = uploadPic == null ? null : uploadPic.trim();
    }

    public String getUserRetordDesc() {
        return userRetordDesc;
    }

    public void setUserRetordDesc(String userRetordDesc) {
        this.userRetordDesc = userRetordDesc == null ? null : userRetordDesc.trim();
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

    public String getAdRecvName() {
        return adRecvName;
    }

    public void setAdRecvName(String adRecvName) {
        this.adRecvName = adRecvName == null ? null : adRecvName.trim();
    }

    public String getAdProvince() {
        return adProvince;
    }

    public void setAdProvince(String adProvince) {
        this.adProvince = adProvince == null ? null : adProvince.trim();
    }

    public String getAdProvinceCode() {
        return adProvinceCode;
    }

    public void setAdProvinceCode(String adProvinceCode) {
        this.adProvinceCode = adProvinceCode == null ? null : adProvinceCode.trim();
    }

    public String getAdCity() {
        return adCity;
    }

    public void setAdCity(String adCity) {
        this.adCity = adCity == null ? null : adCity.trim();
    }

    public String getAdCityCode() {
        return adCityCode;
    }

    public void setAdCityCode(String adCityCode) {
        this.adCityCode = adCityCode == null ? null : adCityCode.trim();
    }

    public String getAdCounty() {
        return adCounty;
    }

    public void setAdCounty(String adCounty) {
        this.adCounty = adCounty == null ? null : adCounty.trim();
    }

    public String getAdMobile() {
        return adMobile;
    }

    public void setAdMobile(String adMobile) {
        this.adMobile = adMobile == null ? null : adMobile.trim();
    }

    public String getAdCountyCode() {
        return adCountyCode;
    }

    public void setAdCountyCode(String adCountyCode) {
        this.adCountyCode = adCountyCode == null ? null : adCountyCode.trim();
    }

    public String getAdTown() {
        return adTown;
    }

    public void setAdTown(String adTown) {
        this.adTown = adTown == null ? null : adTown.trim();
    }

    public String getAdTownCode() {
        return adTownCode;
    }

    public void setAdTownCode(String adTownCode) {
        this.adTownCode = adTownCode == null ? null : adTownCode.trim();
    }

    public String getAdDetailAddr() {
        return adDetailAddr;
    }

    public void setAdDetailAddr(String adDetailAddr) {
        this.adDetailAddr = adDetailAddr == null ? null : adDetailAddr.trim();
    }

    public String getAdPhone() {
        return adPhone;
    }

    public void setAdPhone(String adPhone) {
        this.adPhone = adPhone == null ? null : adPhone.trim();
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getRetType() {
        return retType;
    }

    public void setRetType(String retType) {
        this.retType = retType == null ? null : retType.trim();
    }

    public String getUserRetType() {
        return userRetType;
    }

    public void setUserRetType(String userRetType) {
        this.userRetType = userRetType == null ? null : userRetType.trim();
    }

    public String getServiceRetAlipay() {
        return serviceRetAlipay;
    }

    public void setServiceRetAlipay(String serviceRetAlipay) {
        this.serviceRetAlipay = serviceRetAlipay;
    }

    public String getServiceRetBank() {
        return serviceRetBank;
    }

    public void setServiceRetBank(String serviceRetBank) {
        this.serviceRetBank = serviceRetBank == null ? null : serviceRetBank.trim();
    }

    public String getServiceRetCardNum() {
        return serviceRetCardNum;
    }

    public void setServiceRetCardNum(String serviceRetCardNum) {
        this.serviceRetCardNum = serviceRetCardNum == null ? null : serviceRetCardNum.trim();
    }

    public String getServiceRetCardName() {
        return serviceRetCardName;
    }

    public void setServiceRetCardName(String serviceRetCardName) {
        this.serviceRetCardName = serviceRetCardName == null ? null : serviceRetCardName.trim();
    }

    public String getServiceRetOpenBank() {
        return serviceRetOpenBank;
    }

    public void setServiceRetOpenBank(String serviceRetOpenBank) {
        this.serviceRetOpenBank = serviceRetOpenBank == null ? null : serviceRetOpenBank.trim();
    }

    public String getRetPayType() {
        return retPayType;
    }

    public void setRetPayType(String retPayType) {
        this.retPayType = retPayType;
    }

    public List<RetordLogs> getRetLogsList() {
        return retLogsList;
    }

    public void setRetLogsList(List<RetordLogs> retLogsList) {
        this.retLogsList = retLogsList;
    }

    public List<Ordi> getOrdiList() {
        return ordiList;
    }

    public void setOrdiList(List<Ordi> ordiList) {
        this.ordiList = ordiList;
    }

    @Override
    public String getId() {
        return this.retordOrderId;
    }

	public Double getRetRealMlwAmount() {
		return retRealMlwAmount;
	}

	public void setRetRealMlwAmount(Double retRealMlwAmount) {
		this.retRealMlwAmount = retRealMlwAmount;
	}

	public String getServiceRetAlipayName() {
		return serviceRetAlipayName;
	}

	public void setServiceRetAlipayName(String serviceRetAlipayName) {
		this.serviceRetAlipayName = serviceRetAlipayName;
	}
}