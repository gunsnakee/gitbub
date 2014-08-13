package com.meiliwan.emall.commons.rabbitmq;

import org.apache.commons.lang.StringUtils;


class RabbitmqMsg {
	
	private RabbitmqMsg(){}
	
	private String exchangeType = RabbitmqConstant.FANOUT_MQ_EXCHANGE_TYPE;
	
	private String routingKey;
	
	private String msg;
	
	
	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getExchangeType() {
		return exchangeType;
	}

	public void setExchangeType(String exchangeType) {
		this.exchangeType = exchangeType;
	}
	
	public static RabbitmqMsg getFanoutRabbitmqMsg(String msg){
		RabbitmqMsg fanoutMsg = new RabbitmqMsg();
		fanoutMsg.setMsg(msg);
		return fanoutMsg; 
	}

	public static RabbitmqMsg getTopicRabbitmqMsg(String msg, String routingKey){
		if (StringUtils.isBlank(msg) || routingKey == null) {
			return null;
		}
		RabbitmqMsg topicMsg = new RabbitmqMsg();
		topicMsg.setExchangeType(RabbitmqConstant.TOPIC_MQ_EXCHANGE_TYPE);
		topicMsg.setRoutingKey(routingKey);
		topicMsg.setMsg(msg);
		return topicMsg;
	}
	
	
	public static String getRoutingKey(String[] routingKeyInfoArr){
		if (routingKeyInfoArr == null)
			return "";
		
		StringBuilder sb = new StringBuilder("");
		for (String str : routingKeyInfoArr) {
			if (StringUtils.isBlank(str)) {
				continue;
			} 
			sb.append(str).append(RabbitmqConstant.ROUTING_KEY_TOKEN);
		}
		sb.deleteCharAt(sb.length() - 1);
		
		return sb.toString();
	}
	
}
