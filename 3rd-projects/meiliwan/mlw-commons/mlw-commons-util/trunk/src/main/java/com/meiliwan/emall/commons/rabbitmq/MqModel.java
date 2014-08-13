package com.meiliwan.emall.commons.rabbitmq;

public enum MqModel {
	/** 用户 */
	USER, 
	/** 商品 */
	PRODUCT, 
	/** 订单 */
	ORDER, 
	/** 促销/活动 */
	SP,
	/** 用户钱包*/
	ACCOUNT,
	/** 运营后台 */
	BKSTAGE,
	/** 图片服务 */
	UPLOADIMAGE,
	/** 日志 */
	LOG,
	
	/** 监控日志  */
	WATCHLOG,
	
	/** 延迟发送msg */
	ASYNCMSG ,

    /** 数据中心*/
    DATACENTER;
	;
	
	public String mqname(){
		return this.name().toLowerCase();
	}
	
	public static MqModel fromMqname(String mqname) {
		try {
			return MqModel.valueOf(mqname.toUpperCase());
		} catch (Exception e) {
		}
		return null;
	}
	
}
