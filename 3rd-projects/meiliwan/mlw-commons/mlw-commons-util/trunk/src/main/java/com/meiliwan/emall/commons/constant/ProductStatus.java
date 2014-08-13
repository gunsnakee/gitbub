package com.meiliwan.emall.commons.constant;

/**
 * 商品状态类型
 * @author yinggao.zhuo
 * @date 2013-6-17
 */
public enum ProductStatus {

	ON(1,"上架"),
	OFF(2,"下架"),
	EDITFAIL(0,"编辑未完成"),
	DELETE(-1,"已删除状态");
	
	private int code;
	private String desc;
	private ProductStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	
	public static void main(String[] args) {
		ProductStatus[] list =  ProductStatus.values();
		for (ProductStatus productStatus : list) {
			
			System.out.println(productStatus);
		}
	}
	
}
