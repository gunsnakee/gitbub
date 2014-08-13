package com.meiliwan.emall.monitor.bean;

import java.util.Date;

import com.meiliwan.emall.core.bean.BaseEntity;

public class OrderStatistics extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 935181161262712668L;

	private Integer id;

	private Integer createCod = 0;

	private Integer createPay = 0;

	private Integer cancel = 0;

	private Integer payFinish = 0;
	
	private Integer codCancel = 0;
	
	private Date createTime;

	private Date createTimeStart;
	private Date createTimeEnd;
	private Integer effective=0;
	
	/**
	 * 设置有效值
	 */
	public void setEffective(){
		effective = createCod+payFinish-codCancel;
	}
	
	public Integer getEffective() {
		return effective;
	}

	public boolean isAllZero(){
		if(createCod.intValue()!=0||createPay.intValue()!=0
				||cancel.intValue()!=0||payFinish.intValue()!=0||codCancel!=0){
			return false;
		}
		return true;
	}
	
	public void addCodCancel(int count) {
		this.codCancel = codCancel + count;
	}
	
	public void addCreateCod(int count) {
		this.createCod = createCod + count;
	}

	public void addCreatePay(int count) {
		this.createPay = createPay + count;
	}

	public void addCancel(int count) {
		this.cancel = cancel + count;
	}

	public void addPayFinish(int count) {
		this.payFinish = payFinish + count;
	}

	@SuppressWarnings("unchecked")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCreateCod() {
		return createCod;
	}

	public void setCreateCod(Integer createCod) {
		this.createCod = createCod;
	}

	public Integer getCreatePay() {
		return createPay;
	}

	public void setCreatePay(Integer createPay) {
		this.createPay = createPay;
	}

	public Integer getCancel() {
		return cancel;
	}

	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}

	public Integer getPayFinish() {
		return payFinish;
	}

	public void setPayFinish(Integer payFinish) {
		this.payFinish = payFinish;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(Date createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public Integer getCodCancel() {
		return codCancel;
	}

	public void setCodCancel(Integer codCancel) {
		this.codCancel = codCancel;
	}

	@Override
	public String toString() {
		return "OrderStatistics [id=" + id + ", createCod=" + createCod
				+ ", createPay=" + createPay + ", cancel=" + cancel
				+ ", payFinish=" + payFinish + ", codCancel=" + codCancel
				+ ", createTime=" + createTime + ", createTimeStart="
				+ createTimeStart + ", createTimeEnd=" + createTimeEnd + "]";
	}


	
}