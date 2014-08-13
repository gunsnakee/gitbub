package com.meiliwan.emall.monitor.service.oms;

import java.util.ArrayList;
import java.util.List;

public class OMSProduct {

	private String orderId;
	private String sku;
	private String storeSku;
	private String num;
	private String title;
	private String price;
	private String realPay;
	private String favourPay;
	private String willPay;
	private String invoicePay;
	
	public List<String> toList(){
		List<String> list = new ArrayList<String>();
		list.add(orderId);
		list.add(sku);
		list.add(storeSku);
		list.add(num);
		list.add(title);
		list.add(price);
		list.add(realPay);
		list.add(favourPay);
		list.add(willPay);
		list.add(invoicePay);
		return list;
		
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getStoreSku() {
		return storeSku;
	}

	public void setStoreSku(String storeSku) {
		this.storeSku = storeSku;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRealPay() {
		return realPay;
	}

	public void setRealPay(String realPay) {
		this.realPay = realPay;
	}

	public String getFavourPay() {
		return favourPay;
	}

	public void setFavourPay(String favourPay) {
		this.favourPay = favourPay;
	}

	public String getWillPay() {
		return willPay;
	}

	public void setWillPay(String willPay) {
		this.willPay = willPay;
	}

	public String getInvoicePay() {
		return invoicePay;
	}

	public void setInvoicePay(String invoicePay) {
		this.invoicePay = invoicePay;
	}
	
}
