package com.meiliwan.emall.union.cps.cookie;

public class ChanetConstant {
	
	public final static String SOURCE = "source";
	public final static String SOURCE_VALUE = "null";
	
	//Thanks id，由成果网提供，是广告主在成果网这边的编号，固定的
	public final static String THANKS_ID = "t";
	public final static String THANKS_ID_VALUE = "NULL";
	
	
	//加密key请向成果网索取。
	public final static String KEY = "key";
	public final static String KEY_VALUE = "null";
	
	//成果网唯一标识id ([SID])
	public final static String CHANET_ID = "id";
	public final static String CHANET_ID_VALUE = "NULL" ;
	
	//订单号
	public final static String ORDER_NUM = "i";
	
	//签名信息
	public final static String SING_INFO = "sign";
	
	//商品信息，一个或多个四元组，冒号分隔；每个资源组用“/”分隔，goods为商品分类（标识分成比例）、price为单价、count为数量、name为商品ID
	public final static String GOODS_INFO = "o";
	
	// 订单时间
	public final static String ORDER_TIME = "ot";
	
	// pay method
	public final static String PAY_METHOD = "pm";
	
	//order status
	public final static String ORDER_STATUS = "st";
	
	// order pay status
	public final static String ORDER_PAY_STATUS = "ps";
	
	
	//jump url
	public final static String JUMP_URL = "url";
	
	//RD 认证其Authentication period
	public final static String RD_AUTH_key = "RD_AUTH_key";
	public final static int RD_AUTH_PERIOD = 30;
	
	//推广公司
	public final static String LINK_TECH_KEY = "CHANET_LINK_TECH";
	public final static String LINK_TECH = "channet";
	
	public final static String ZK_CONFIG_PATH = "commons/system.properties";
	
	public final static String CHANNET_ALLOW_IP_KEY = "channet_allow_ip";


	
	
	
	
	

}
