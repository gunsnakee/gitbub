package com.meiliwan.emall.oms.dto.export;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;

public class OrdLogisticsReportRow implements Serializable {
    
	private static final long serialVersionUID = -8890762528400584379L;
	
	/**  待出库时间  */
	private Date waitOutDepositoryDate;
	/**  出库时间  */
	private Date outDepositoryDate;
	
	private String orderId;
	/**  运单号  */
	private String logisticsNumber;
	/**  商品数量 	 */
	private int saleNumber=0;
	/**  订单金额，实际支付总额  */
	private double realPayAmount=0;
	/**  收货地址  */
	private String deliveryAddress;
	/** 第三方代收  */
	private double insteadRealPayAmount=0;
	
	private DecimalFormat df = new DecimalFormat("#0.00");
	public Date getWaitOutDepositoryDate() {
		return waitOutDepositoryDate;
	}
	public void setWaitOutDepositoryDate(Date waitOutDepositoryDate) {
		this.waitOutDepositoryDate = waitOutDepositoryDate;
	}
	public Date getOutDepositoryDate() {
		return outDepositoryDate;
	}
	public void setOutDepositoryDate(Date outDepositoryDate) {
		this.outDepositoryDate = outDepositoryDate;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getLogisticsNumber() {
		return logisticsNumber;
	}
	public void setLogisticsNumber(String logisticsNumber) {
		this.logisticsNumber = logisticsNumber;
	}
	public int getSaleNumber() {
		return saleNumber;
	}
	public void setSaleNumber(int saleNumber) {
		this.saleNumber = saleNumber;
	}
	public String getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
	public double getRealPayAmount() {
		return realPayAmount;
	}
	/**
	 * 返回字符串，小数点两位
	 * @return
	 */
	public String getRealPayAmount2() {
		return df.format(realPayAmount);
	}
	public void setRealPayAmount(double realPayAmount) {
		this.realPayAmount = realPayAmount;
	}
	public double getInsteadRealPayAmount() {
		return insteadRealPayAmount;
	}
	/**
	 * 返回字符串，小数点两位
	 * @return
	 */
	public String getInsteadRealPayAmount2() {
		return df.format(insteadRealPayAmount);
	}
	
	public void setInsteadRealPayAmount(double insteadRealPayAmount) {
		this.insteadRealPayAmount = insteadRealPayAmount;
	}
	@Override
	public String toString() {
		return "OrdLogisticsReport [waitOutDepositoryDate="
				+ waitOutDepositoryDate + ", outDepositoryDate="
				+ outDepositoryDate + ", orderId=" + orderId
				+ ", logisticsNumber=" + logisticsNumber + ", saleNumber="
				+ saleNumber + ", realPayAmount=" + realPayAmount
				+ ", deliveryAddress=" + deliveryAddress
				+ ", insteadRealPayAmount=" + insteadRealPayAmount + "]";
	}
	
	
}