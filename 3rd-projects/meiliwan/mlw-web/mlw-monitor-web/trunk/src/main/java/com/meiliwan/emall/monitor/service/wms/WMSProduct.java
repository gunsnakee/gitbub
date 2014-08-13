package com.meiliwan.emall.monitor.service.wms;

import java.util.ArrayList;
import java.util.List;

public class WMSProduct {

	private String orderId;
	private String code;
	private String name;
	private String format;//规格
	private String color;
	private String num;
	private String price;
	private String discountAmount;
	private String total;
	
	public List<String> toList(){
		List<String> list = new ArrayList<String>();
		list.add(orderId);
		list.add(code);
		list.add(name);
		list.add(format);
		list.add(color);
		list.add(num);
		list.add(price);
		list.add(discountAmount);
		list.add(total);	
		return list;
		
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getDiscountAmount() {
		return discountAmount;
	}
	public void setDiscountAmount(String discountAmount) {
		this.discountAmount = discountAmount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	
	
}
