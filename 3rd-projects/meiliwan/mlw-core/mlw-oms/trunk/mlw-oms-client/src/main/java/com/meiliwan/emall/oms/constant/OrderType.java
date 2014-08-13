package com.meiliwan.emall.oms.constant;

public enum OrderType {

    REAL_ORDER(OrderType.ROD,"实体订单"),
    REAL_ORDER_GIFT("RGF","实体订单-赠品"),
    REAL_ORDER_CART("RCT","实体订单-礼品卡"),
    REAL_ORDER_COD(OrderType.RCD,"货到付款"),
    REAL_ORDER_PRESELL(OrderType.RPS,"预售订单"),
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
	/** 预售订单  */
	public static final String RPS ="RPS";
	/** 普通订单 - 处理预售订单外的实体订单  */
	public static final String RPT ="RPT";
	/**
	 * 校验订单类型 正向生成的订单
	 * 注：新增订单类型需要留意这里
	 * return 符合正向类型 true
	 */
	public static boolean checkOrderType(String ocode){
		boolean isforward = false ;
		if (ocode.equals(OrderType.REAL_ORDER.getCode()) 
				|| ocode.equals(OrderType.REAL_ORDER_CART.getCode()) 
				|| ocode.equals(OrderType.REAL_ORDER_COD.getCode()) 
				|| ocode.equals(OrderType.VIRTUAL_ORDER.getCode()) 
				|| ocode.equals(OrderType.REAL_ORDER_GIFT.getCode())
				|| ocode.equals(OrderType.REAL_ORDER_PRESELL.getCode())) {
			isforward = true;
		}
		return isforward ;
	}
	
	public static void main(String[] args) {
		OrderType[] types = OrderType.values();
		for (OrderType orderType : types) {
			
			System.out.println(orderType.getCode());
		}
	}
}
