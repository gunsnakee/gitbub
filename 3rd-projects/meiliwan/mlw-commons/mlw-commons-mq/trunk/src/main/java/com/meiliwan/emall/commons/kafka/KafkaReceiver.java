package com.meiliwan.emall.commons.kafka;

import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

/**
 * 
 * @author lsf
 *
 */
public class KafkaReceiver {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(KafkaReceiver.class);

	private final static String ZK_CONFIG_PATH = "commons/system.properties";
	private static ConsumerConnector consumer ;
	
	private KafkaReceiver(){
	}
	
	static {
		ConfigOnZk zkConfig = ConfigOnZk.getInstance();
		try{
			String zkServers = zkConfig.getValue(ZK_CONFIG_PATH, "zookeeper.connect");
			Properties props = new Properties();
			props.put("zookeeper.connect", zkServers);
		    props.put("group.id", "group1");
		    props.put("zookeeper.session.timeout.ms", zkConfig.getValue(ZK_CONFIG_PATH, "zookeeper.session.timeout.ms", "400"));
		    props.put("zookeeper.sync.time.ms", zkConfig.getValue(ZK_CONFIG_PATH, "zookeeper.sync.time.ms", "200"));
		    props.put("auto.commit.interval.ms", zkConfig.getValue(ZK_CONFIG_PATH, "auto.commit.interval.ms", "1000"));
		    
		    consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));
		} catch (BaseException e){
			LOG.error(e, "get zk config params error", null);
		}
	}
	
	
}
