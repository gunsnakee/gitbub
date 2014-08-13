package com.meiliwan.emall.commons.constant.order;

public enum OrderType {

    REAL_ORDER(OrderType.ROD,"实体订单"),
    REAL_ORDER_GIFT("RGF","实体订单-赠品"),
    REAL_ORDER_CART("RCT","实体订单-礼品卡"),
    REAL_ORDER_COD(OrderType.RCD,"货到付款"),
    VIRTUAL_ORDER("VOD","虚拟商品订单"),
	APPLY("ALY","申请退换货"),
	RECEDE("REC","退货"),
	CHANGE("CHG","换货");


	private String code;
	private String desc;
	private OrderType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	public String getCode() {
		return code;
	}
	public String getDesc() {
		return desc;
	}
	/** 实体订单  */
	public static final String ROD ="ROD";
	/** 货到付款  */
	public static final String RCD ="RCD";
	
	public static void main(String[] args) {
		OrderType[] types = OrderType.values();
		for (OrderType orderType : types) {
			
			System.out.println(orderType.getCode());
		}
	}
}
