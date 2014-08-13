package com.meiliwan.emall.monitor.service.wms;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.meiliwan.emall.commons.util.DateUtil;

/**
 * String title[] = { "订单编号", "下单时间", "仓库编码", "物流公司编码", "物流公司名称", "收件人",
			"收货地址", "邮政编码", "联系电话", "联系电话", "邮费", "省", "市", "区", "买家备注",
			"卖家备注", "店铺名字", "买家昵称", "买家实际付款", "客户编码", "预存款", "发票抬头", "货到付款",
			"是否需要发票true/false", "是否紧急", "运费到付", "预售类型", "组织架构" };
		
			"ZIP", "Tel", "Tel", "postage", "province", "city", "district", "buyer Memo" 
"Seller Notes", "shop name", "buyer nickname," "buyer actual payment", "Customer Code", "pre-deposit", "Invoice", "cash on delivery" 
"Do you need an invoice true / false", "whether emergency", "freight", "pre-sale type", "organization"

 * @author rubi
 *
 */
public class WMSBean {
	
	private String orderId;
	private String createTime;
	private String codeOfDepot;
	private String codeOfTransport;
	private String nameOfTransport;
	private String receiver;
	private String receiveAddress;
	private String zipCode;
	private String phone;
	private String mobile;
	private String postage;
	private String province;
	private String city;
	private String town;
	private String buyerMemo;
	private String sellerMeno;
	private String shopName;
	private String buyerNickName;
	private String buyerPayment;
	private String customerCode;
	private String preDeposit;
	private String invoice;
	private String cod;
	private String needInvoice;
	private String emergency;
	private String postageReceive;
	private String preSaleType;
	private String organization="01";
	private String saleType="01";
	private boolean valid=true;//是否有效,来判断是否导入sheet
	
	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCodeOfDepot() {
		return codeOfDepot;
	}
	public void setCodeOfDepot(String codeOfDepot) {
		this.codeOfDepot = codeOfDepot;
	}
	public String getCodeOfTransport() {
		return codeOfTransport;
	}
	public void setCodeOfTransport(String codeOfTransport) {
		this.codeOfTransport = codeOfTransport;
	}
	public String getNameOfTransport() {
		return nameOfTransport;
	}
	public void setNameOfTransport(String nameOfTransport) {
		this.nameOfTransport = nameOfTransport;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
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
	public String getBuyerMemo() {
		return buyerMemo;
	}
	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}
	public String getSellerMeno() {
		return sellerMeno;
	}
	public void setSellerMeno(String sellerMeno) {
		this.sellerMeno = sellerMeno;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getBuyerNickName() {
		return buyerNickName;
	}
	public void setBuyerNickName(String buyerNickName) {
		this.buyerNickName = buyerNickName;
	}
	public String getBuyerPayment() {
		return buyerPayment;
	}
	public void setBuyerPayment(String buyerPayment) {
		this.buyerPayment = buyerPayment;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	public String getPreDeposit() {
		return preDeposit;
	}
	public void setPreDeposit(String preDeposit) {
		this.preDeposit = preDeposit;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	}
	public void setInvoiceTrue() {
		this.needInvoice = "true";
	}
	public void setInvoiceFalse() {
		this.needInvoice = "false";
	}
	public String getCod() {
		return cod;
	}
	public void setCod(String cod) {
		this.cod = cod;
	}
	public String getNeedInvoice() {
		return needInvoice;
	}
	public void setNeedInvoice(String needInvoice) {
		this.needInvoice = needInvoice;
	}
	public String getEmergency() {
		return emergency;
	}
	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}
	public String getPostageReceive() {
		return postageReceive;
	}
	public void setPostageReceive(String postageReceive) {
		this.postageReceive = postageReceive;
	}
	public String getPreSaleType() {
		return preSaleType;
	}
	public void setPreSaleType(String preSaleType) {
		this.preSaleType = preSaleType;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	@Override
	public String toString() {
		return "WMSBean [orderId=" + orderId + ", createTime=" + createTime
				+ ", codeOfDepot=" + codeOfDepot + ", codeOfTransport="
				+ codeOfTransport + ", nameOfTransport=" + nameOfTransport
				+ ", receiver=" + receiver + ", receiveAddress="
				+ receiveAddress + ", zipCode=" + zipCode + ", phone=" + phone
				+ ", mobile=" + mobile + ", postage=" + postage + ", province="
				+ province + ", city=" + city + ", town=" + town
				+ ", buyerMemo=" + buyerMemo + ", sellerMeno=" + sellerMeno
				+ ", shopName=" + shopName + ", buyerNickName=" + buyerNickName
				+ ", buyerPayment=" + buyerPayment + ", customerCode="
				+ customerCode + ", preDeposit=" + preDeposit + ", invoice="
				+ invoice + ", cod=" + cod + ", needInvoice=" + needInvoice
				+ ", emergency=" + emergency + ", postageReceive="
				+ postageReceive + ", preSaleType=" + preSaleType
				+ ", organization=" + organization + "]";
	}
	
	public List<String> toList() {
		List<String> list = new ArrayList<String>();
		list.add(orderId);
		list.add(createTime);
		list.add(codeOfDepot);
		list.add(codeOfTransport);
		list.add(nameOfTransport);
		list.add(receiver);
		list.add(receiveAddress);
		list.add(zipCode);
		list.add(mobile);
		list.add(phone);
		list.add(postage);
		list.add(province);
		list.add(city);
		list.add(town);
		list.add(buyerMemo);
		list.add(sellerMeno);
		list.add(shopName);
		list.add(buyerNickName);
		list.add(buyerPayment);
		list.add(customerCode);
		list.add(preDeposit);
		list.add(invoice);
		list.add(cod);
		list.add(needInvoice);
		list.add(emergency);
		list.add(postageReceive);
		list.add(preSaleType);
		list.add(organization);
		list.add(saleType);
		return list;
	}
	
}
