package com.meiliwan.emall.base.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物流区域联合查询DTO
 * @author yinggao.zhuo
 * @date 2013-6-10
 */
public class TransportAreaDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8242189107670876606L;
	private Integer transCompanyId;
	private String areaName;
	private String areaName1;
	private String areaName2;
	private String areaName3;
	private String areaCode;
	private String areaCode1;
	private String areaCode2;
	private BigDecimal price;
	private Integer freeSupport;
	private BigDecimal fullFreeSupport;
	private String parentCode;
	private Integer cashOnDelivery;
	private Integer areaGrade;
	private Short state;
	private Integer predictTimeMin;
	private Integer predictTimeMax;
	private Integer notTransCompanyId;
	
	public Integer getNotTransCompanyId() {
		return notTransCompanyId;
	}
	public void setNotTransCompanyId(Integer notTransCompanyId) {
		this.notTransCompanyId = notTransCompanyId;
	}
	public Integer getPredictTimeMin() {
		return predictTimeMin;
	}
	public void setPredictTimeMin(Integer predictTimeMin) {
		this.predictTimeMin = predictTimeMin;
	}
	public Integer getPredictTimeMax() {
		return predictTimeMax;
	}
	public void setPredictTimeMax(Integer predictTimeMax) {
		this.predictTimeMax = predictTimeMax;
	}
	public Short getState() {
		return state;
	}
	public void setState(Short state) {
		this.state = state;
	}
	public String getAreaCode1() {
		return areaCode1;
	}
	public void setAreaCode1(String areaCode1) {
		this.areaCode1 = areaCode1;
	}
	public String getAreaCode2() {
		return areaCode2;
	}
	public void setAreaCode2(String areaCode2) {
		this.areaCode2 = areaCode2;
	}
	public Integer getAreaGrade() {
		return areaGrade;
	}
	public void setAreaGrade(Integer areaGrade) {
		this.areaGrade = areaGrade;
	}
	public String getAreaName3() {
		return areaName3;
	}
	public void setAreaName3(String areaName3) {
		this.areaName3 = areaName3;
	}
	public String getAreaName1() {
		return areaName1;
	}
	public void setAreaName1(String areaName1) {
		this.areaName1 = areaName1;
	}
	public String getAreaName2() {
		return areaName2;
	}
	public void setAreaName2(String areaName2) {
		this.areaName2 = areaName2;
	}
	public Integer getCashOnDelivery() {
		return cashOnDelivery;
	}
	public void setCashOnDelivery(Integer cashOnDelivery) {
		this.cashOnDelivery = cashOnDelivery;
	}
	public Integer getTransCompanyId() {
		return transCompanyId;
	}
	public void setTransCompanyId(Integer transCompanyId) {
		this.transCompanyId = transCompanyId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getFreeSupport() {
		return freeSupport;
	}
	public void setFreeSupport(Integer freeSupport) {
		this.freeSupport = freeSupport;
	}
	public BigDecimal getFullFreeSupport() {
		return fullFreeSupport;
	}
	public void setFullFreeSupport(BigDecimal fullFreeSupport) {
		this.fullFreeSupport = fullFreeSupport;
	}
	@Override
	public String toString() {
		return "TransportAreaDTO [transCompanyId=" + transCompanyId
				+ ", areaName=" + areaName + ", areaName1=" + areaName1
				+ ", areaName2=" + areaName2 + ", areaName3=" + areaName3
				+ ", areaCode=" + areaCode + ", areaCode1=" + areaCode1
				+ ", areaCode2=" + areaCode2 + ", price=" + price
				+ ", freeSupport=" + freeSupport + ", fullFreeSupport="
				+ fullFreeSupport + ", parentCode=" + parentCode
				+ ", cashOnDelivery=" + cashOnDelivery + ", areaGrade="
				+ areaGrade + ", state=" + state + "]";
	}
	
}
