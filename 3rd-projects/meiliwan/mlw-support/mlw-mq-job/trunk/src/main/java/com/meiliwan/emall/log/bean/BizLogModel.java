package com.meiliwan.emall.log.bean;

import com.meiliwan.emall.commons.rabbitmq.MqModel;

public enum BizLogModel {
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
	BKSTAGE;
	
	public MqModel toMqModel(){
		return MqModel.valueOf(this.name());
	}
}
