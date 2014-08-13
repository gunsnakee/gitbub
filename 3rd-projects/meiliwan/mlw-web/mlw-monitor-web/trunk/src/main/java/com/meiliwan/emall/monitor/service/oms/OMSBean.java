package com.meiliwan.emall.monitor.service.oms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.commons.util.DateUtil;

/**
"平台单号","平台名称","店铺名称","授权KEY",
		"交易状态","订单类型","下单时间","更新时间",
		"支付时间","是否货到付款","会员昵称","发票抬头",
		"发票类型","发票明细","卖家留言","买家留言",
		"商家留言","应收金额","实际支付","收件人",
		"电话","收件地址","省","市","区","邮编"
 */
public class OMSBean {
	private String orderId;
	private String marketName;
	private String stroeName;
	private String state;
	private String orderTime;
	private String updateTime;
	private String payTime;
	private String isCOD;
	private String nickName;
	private String invoiceHead;
	private String invoiceType;
	private String invoiceDetail;
	private String sellerMeno;
	private String buyerMemo;
	private String shopMemo;
	private String willPay;
	private String realPay;
	private String receiver;
	private String phone;
	private String address;
	private String province;
	private String city;
	private String town;
	private String zipCode;
	private List<OMSProduct> products=new ArrayList<OMSProduct>();
	private boolean valid = true;
	
	public OMSProduct getFirstProduct(){
		if(products.size()>0){
			return products.get(0);
		}else{
			return null;
		}
	}
	
	public void addProduct(OMSProduct pro){
		pro.setOrderId(orderId);
		products.add(pro);
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMarketName() {
		return marketName;
	}

	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	public String getStroeName() {
		return stroeName;
	}

	public void setStroeName(String stroeName) {
		this.stroeName = stroeName;
	}


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getIsCOD() {
		return isCOD;
	}

	public void setIsCOD(String isCOD) {
		this.isCOD = isCOD;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getInvoiceHead() {
		return invoiceHead;
	}

	public void setInvoiceHead(String invoiceHead) {
		this.invoiceHead = invoiceHead;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceDetail() {
		return invoiceDetail;
	}

	public void setInvoiceDetail(String invoiceDetail) {
		this.invoiceDetail = invoiceDetail;
	}

	public String getSellerMeno() {
		return sellerMeno;
	}

	public void setSellerMeno(String sellerMeno) {
		this.sellerMeno = sellerMeno;
	}

	public String getBuyerMemo() {
		return buyerMemo;
	}

	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}

	public String getShopMemo() {
		return shopMemo;
	}

	public void setShopMemo(String shopMemo) {
		this.shopMemo = shopMemo;
	}

	public String getWillPay() {
		return willPay;
	}

	public void setWillPay(String willPay) {
		this.willPay = willPay;
	}

	public String getRealPay() {
		return realPay;
	}

	public void setRealPay(String realPay) {
		this.realPay = realPay;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public List<OMSProduct> getProducts() {
		return products;
	}

	public void setProducts(List<OMSProduct> products) {
		this.products = products;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		return "OMSBean [orderId=" + orderId + ", marketName=" + marketName
				+ ", stroeName=" + stroeName + ", state="
				+ state + ", orderTime=" + orderTime + ", updateTime="
				+ updateTime + ", payTime=" + payTime + ", isCOD=" + isCOD
				+ ", nickName=" + nickName + ", invoiceHead=" + invoiceHead
				+ ", invoiceType=" + invoiceType + ", invoiceDetail="
				+ invoiceDetail + ", sellerMeno=" + sellerMeno + ", buyerMemo="
				+ buyerMemo + ", shopMemo=" + shopMemo + ", willPay=" + willPay
				+ ", realPay=" + realPay + ", receiver=" + receiver
				+ ", phone=" + phone + ", adress=" + address + ", province="
				+ province + ", city=" + city + ", town=" + town + ", zipCode="
				+ zipCode + ", products=" + products + ", valid=" + valid + "]";
	}
	
	public List<String> toList() {
		List<String> list = new ArrayList<String>();
		list.add(orderId);
		list.add(marketName);
		list.add(stroeName);
		list.add(state);
		list.add(orderTime);
		list.add(updateTime);
		list.add(payTime);
		list.add(isCOD);
		list.add(nickName);
		list.add(invoiceHead);
		list.add(invoiceType);
		list.add(invoiceDetail);
		list.add(sellerMeno);
		list.add(buyerMemo);
		list.add(shopMemo);
		list.add(willPay);
		list.add(realPay);
		list.add(receiver);
		list.add(phone);
		list.add(address);
		list.add(province);
		list.add(city);
		list.add(town);
		list.add(zipCode);
		return list;
	}

	
	
}
