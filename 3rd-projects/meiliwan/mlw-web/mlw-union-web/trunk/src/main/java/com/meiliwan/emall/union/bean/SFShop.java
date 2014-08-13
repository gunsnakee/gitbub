package com.meiliwan.emall.union.bean;

import com.google.gson.Gson;

public class SFShop {
	private String ShopId;
	private String Provinces;
	private String Area;
	private String ShopName;
	private String ShopCode;
	private String Email;
	public String getShopId() {
		return ShopId;
	}
	public void setShopId(String shopId) {
		ShopId = shopId;
	}
	public String getProvinces() {
		return Provinces;
	}
	public void setProvinces(String provinces) {
		Provinces = provinces;
	}
	public String getArea() {
		return Area;
	}
	public void setArea(String area) {
		Area = area;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getShopCode() {
		return ShopCode;
	}
	public void setShopCode(String shopCode) {
		ShopCode = shopCode;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
	public String toString(){
		return new Gson().toJson(this);
	}

}
