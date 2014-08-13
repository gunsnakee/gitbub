package com.meiliwan.emall.commons.rabbitmq;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

public final class RabbitmqConstant {
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(RabbitmqConstant.class);
	public static final String MQ_HOST ;
	
	public static final String FANOUT_MQ_EXCHANGE_TYPE = "fanout";
	public static final String FANOUT_MQ_EXCHANGE_NAME = "fanout_msgqueue_meiliwan";
	
	public static final String TOPIC_MQ_EXCHANGE_TYPE = "topic";
	public static final String TOPIC_MQ_EXCHANGE_NAME = "topic_msgqueue_meiliwan";
	public static final String ROUTING_KEY_TOKEN = ".";
	
	static {
		String mqhost = "rabbitmqsvr.meiliwan.com";
		try {
			mqhost = ConfigOnZk.getInstance().getValue("commons/system.properties", mqhost);
		} catch (BaseException e) {
			LOG.error(e, "get mq host '"+mqhost+"' failure", null);
		}finally{
			MQ_HOST = mqhost;
			LOG.debug("mq host is " + MQ_HOST);
		}
	}
}
