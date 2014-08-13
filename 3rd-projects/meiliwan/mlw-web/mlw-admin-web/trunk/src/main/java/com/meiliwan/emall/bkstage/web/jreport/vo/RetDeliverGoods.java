package com.meiliwan.emall.bkstage.web.jreport.vo;

import java.text.DecimalFormat;
import java.util.List;

import com.meiliwan.emall.bkstage.web.html.HtmlFilterUtil;
/**
 * 发货清单
 *
 */
public class RetDeliverGoods extends BaseReportVO{

	private DecimalFormat df = new DecimalFormat("#0.00");
	
	private String orderId;
	private String oldOrderId;
	
	
	private String recvName;
	private String address;
	private String mobile;
	private String phone;
	private String createTime;
	private String transCompany;
	private String ShipperNumber;
	
	private String comment;
	//商品总金额
	private String saleAmout;
	
	private String realPayAmout;
    private List<RetDeliverGoodsItem> deliverGoodsItems;
    private int totalItem;
    private int totalRetNum;
    
    
    
	public int getTotalRetNum() {
		return totalRetNum;
	}

	public void setTotalRetNum(int totalRetNum) {
		this.totalRetNum = totalRetNum;
	}

	public void setSaleAmout(String saleAmout) {
		this.saleAmout = saleAmout;
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

	public List<RetDeliverGoodsItem> getDeliverGoodsItems() {
		return deliverGoodsItems;
	}

	public void setDeliverGoodsItems(List<RetDeliverGoodsItem> deliverGoodsItems) {
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

	public String getRealPayAmout() {
		return realPayAmout;
	}

	public void setRealPayAmout(double realPayAmout) {
		this.realPayAmout = df.format(realPayAmout);
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address==null?"":HtmlFilterUtil.encoding(address);
	}

	public String getOldOrderId() {
		return oldOrderId;
	}

	public void setOldOrderId(String oldOrderId) {
		this.oldOrderId = oldOrderId;
	}

	public void setRealPayAmout(String realPayAmout) {
		this.realPayAmout = realPayAmout;
	}
	
	
}
