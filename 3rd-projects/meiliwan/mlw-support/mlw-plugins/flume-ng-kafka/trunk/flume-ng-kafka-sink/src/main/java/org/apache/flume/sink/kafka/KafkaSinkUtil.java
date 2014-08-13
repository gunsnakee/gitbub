/*******************************************************************************
 * Copyright 2013 Renjie Yao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.apache.flume.sink.kafka;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

import org.apache.flume.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaSinkUtil {
	private static final Logger log = LoggerFactory.getLogger(KafkaSinkUtil.class);

	public static Properties getKafkaConfigProperties(Context context) {
		log.info("context={}",context.toString());
		Properties props = new Properties();
		Map<String, String> contextMap = context.getParameters();
		for(String key : contextMap.keySet()) {
			if (!key.equals("type") && !key.equals("channel")) {
				props.setProperty(key, context.getString(key));
				log.info("key={},value={}",key,context.getString(key));
			}
		}
		return props;
	}
	public static Producer<String, String> getProducer(Context context) {
		Producer<String, String> producer;
		producer = new Producer<String, String>(new ProducerConfig(getKafkaConfigProperties(context)));
		return producer;
	}
	
	/**
	 * @return 获取本地的内网IP
	 */
	public static String  getLocalIpSuffix() {  
		try {
            for (Enumeration<NetworkInterface> ni = NetworkInterface.getNetworkInterfaces(); ni
                .hasMoreElements();) {
                NetworkInterface eth = ni.nextElement();
                for (Enumeration<InetAddress> add = eth.getInetAddresses(); add.hasMoreElements();) {
                    InetAddress i = add.nextElement();
                    if (i instanceof Inet4Address) {
                        if (i.isSiteLocalAddress()) {
                        	String localIp = i.getHostAddress();
                        	String[] parts = localIp.split("\\.");
                            return parts[2] + "." + parts[3];
                        }
                    }
                }
            }
        } catch (SocketException e) {
        	log.error(e.getMessage(), e);
        }
        return "";  
	}
}


















