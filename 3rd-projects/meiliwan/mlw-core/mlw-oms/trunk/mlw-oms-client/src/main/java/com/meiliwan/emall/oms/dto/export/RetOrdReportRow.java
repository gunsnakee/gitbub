package com.meiliwan.emall.oms.dto.export;

import java.io.Serializable;
import java.util.Date;
/**
 * 导出退换货列表数据
 * 日期	退货编号	原订单编号	商品编码	商品条形码	商品名称	商品购买价	退商品数量（负数）	退运费金额	补偿运费金额	订单价格	订单状态
 * @author lzl
 */
public class RetOrdReportRow implements Serializable {
	private static final long serialVersionUID = 1L;
	// 创建日期
	private Date createTime ;
	// 退换货编号
	private String retordOrderId ;
	// 原订单编号
	private String oldOrderId ;
	// 商品编号
	private Integer proId;
	// 商品条形码
	private String proBarCode ; 
	// 商品名称
	private String proName ;
	// 商品购买价
	private double prodTotalAmount ;
	// 退商品数量
	private Integer saleNum ;
	// 退运费金额
	private double retPayFare ;
	// 补偿运费金额
	private double retPayCompensate ;
	// 订单金额
	private double retTotalAmount ;
	// 订单状态
	private Integer isEndNode ;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRetordOrderId() {
		return retordOrderId;
	}

	public void setRetordOrderId(String retordOrderId) {
		this.retordOrderId = retordOrderId;
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProBarCode() {
		return proBarCode;
	}

	public void setProBarCode(String proBarCode) {
		this.proBarCode = proBarCode;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public double getProdTotalAmount() {
		return prodTotalAmount;
	}

	public void setProdTotalAmount(double prodTotalAmount) {
		this.prodTotalAmount = prodTotalAmount;
	}

	public Integer getSaleNum() {
		return saleNum;
	}

	public void setSaleNum(Integer saleNum) {
		this.saleNum = saleNum;
	}

	public double getRetPayFare() {
		return retPayFare;
	}

	public void setRetPayFare(double retPayFare) {
		this.retPayFare = retPayFare;
	}

	public double getRetPayCompensate() {
		return retPayCompensate;
	}

	public void setRetPayCompensate(double retPayCompensate) {
		this.retPayCompensate = retPayCompensate;
	}

	public double getRetTotalAmount() {
		return retTotalAmount;
	}

	public void setRetTotalAmount(double retTotalAmount) {
		this.retTotalAmount = retTotalAmount;
	}

	public Integer getIsEndNode() {
		return isEndNode;
	}

	public void setIsEndNode(Integer isEndNode) {
		this.isEndNode = isEndNode;
	}
}
