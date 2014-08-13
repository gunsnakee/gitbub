package com.meiliwan.emall.commons.kafka;

import org.testng.annotations.Test;

/**
 * 
 * @author lsf
 *
 */
public class KafkaSenderTest {
	
	@Test
	public void testSendMsg(){
		KafkaSender.sendMsg("kafka_topic", "hahahahahoufoweuofuwoeruwoeufsdflsdjflsjfoiuweoufworfoweuroweuouworuwoeurow");
		
		KafkaSender.shutdown();
	}

}
