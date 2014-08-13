package com.meiliwan.emall.commons.kafka;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.exception.BaseRuntimeException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 
 * @author lsf
 * 
 */
public class KafkaSender {

	private final static MLWLogger LOG = MLWLoggerFactory
			.getLogger(KafkaSender.class);

	private final static String ZK_CONFIG_PATH = "commons/system.properties";
	private static Producer<String, String> producer;
	
	public static final String KAFKA_TOPIC = "kafka_topic";
	
	private KafkaSender(){
	}

	static {
		ConfigOnZk zkConfig = ConfigOnZk.getInstance();
		try {
			String brokerList = zkConfig.getValue(ZK_CONFIG_PATH,
					"metadata.broker.list");
			Properties props = new Properties();
			props.put("metadata.broker.list", brokerList);
			props.put("serializer.class", zkConfig.getValue(ZK_CONFIG_PATH,
					"serializer.class", "kafka.serializer.StringEncoder"));
			// props.put("partitioner.class",
			// "com.meiliwan.kafka.SimplePartitioner");
			props.put("request.required.acks", zkConfig.getValue(
					ZK_CONFIG_PATH, "request.required.acks", "1"));

			ProducerConfig config = new ProducerConfig(props);
			producer = new Producer<String, String>(config);
		} catch (BaseException e) {
			LOG.error(e, "get zk config params error", null);
		}
	}

	/**
	 * 
	 * @param topic
	 * @param msgs
	 * @return
	 */
	public static void sendMsg(String topic, String... msgs) {
		if (StringUtils.isEmpty(topic) || msgs == null || msgs.length <= 0) {
			return ;
		}
		
		if(producer == null){
			throw new BaseRuntimeException("Kafka producer has not bean initiated yet.");
		}

		for (String msg : msgs) {
			KeyedMessage<String, String> data = new KeyedMessage<String, String>(
					topic, msg);
			producer.send(data);
		}

	}
	
	/**
	 * 
	 */
	public static void shutdown(){
		if(producer != null){
			producer.close();
		}
	}

}
