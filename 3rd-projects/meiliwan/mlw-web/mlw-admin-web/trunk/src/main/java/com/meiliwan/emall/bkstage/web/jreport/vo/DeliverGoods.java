package com.meiliwan.emall.bkstage.web.jreport.vo;

import java.text.DecimalFormat;
import java.util.List;

import com.meiliwan.emall.bkstage.web.html.HtmlFilterUtil;
/**
 * 发货清单
 *
 */
public class DeliverGoods extends BaseReportVO{

	private DecimalFormat df = new DecimalFormat("#0.00");
	
	private String orderId;
	
	private String recvName;
	private String address;
	private String mobile;
	private String phone;
	private String createTime;
	private String transCompany;
	private String ShipperNumber;
	private short isInvoice;
	//发票抬头
	private String invoiceHead;
	private String comment;
	//商品总金额
	private String saleAmout;
	//运费
	private String transportFee;
	private String realPayAmout;
    private String invoiceAmount;
    private List<DeliverGoodsItem> deliverGoodsItems;
    private int totalItem;
    private short isCOD;

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = df.format(invoiceAmount);
    }

    public short getIsCOD() {
		return isCOD;
	}

	public void setIsCOD(short isCOD) {
		this.isCOD = isCOD;
	}

	public int getTotalItem() {
		return totalItem;
	}

	@Override
	public String getId() {
		return orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public List<DeliverGoodsItem> getDeliverGoodsItems() {
		return deliverGoodsItems;
	}

	public void setDeliverGoodsItems(List<DeliverGoodsItem> deliverGoodsItems) {
		this.deliverGoodsItems = deliverGoodsItems;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile==null?"":HtmlFilterUtil.encoding(mobile);
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone==null?"":HtmlFilterUtil.encoding(phone);
	}

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName==null?"":HtmlFilterUtil.encoding(recvName);
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public String getTransCompany() {
		return transCompany==null?"":transCompany;
	}

	public void setTransCompany(String transCompany) {
		this.transCompany = transCompany==null?"":HtmlFilterUtil.encoding(transCompany);
	}

	public String getShipperNumber() {
		return ShipperNumber;
	}

	public void setShipperNumber(String shipperNumber) {
		ShipperNumber = shipperNumber==null?"":HtmlFilterUtil.encoding(shipperNumber);
	}

	public short getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(short isInvoice) {
		this.isInvoice = isInvoice;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment==null?"":HtmlFilterUtil.encoding(comment);
	}

	public String getSaleAmout() {
		return saleAmout;
	}

	public void setSaleAmout(double saleAmout) {
		this.saleAmout = df.format(saleAmout);
	}

	public String getTransportFee() {
		return transportFee;
	}

	public void setTransportFee(double transportFee) {
		this.transportFee = df.format(transportFee);
	}

	public String getRealPayAmout() {
		return realPayAmout;
	}

	public void setRealPayAmout(double realPayAmout) {
		this.realPayAmout = df.format(realPayAmout);
	}

	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = HtmlFilterUtil.encoding(invoiceHead);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address==null?"":HtmlFilterUtil.encoding(address);
	}
	
	
}
