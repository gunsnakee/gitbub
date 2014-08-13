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
package com.mlcs.core.flume.log;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Sink of Kafka which get events from channels and publish to Kafka. I use this in our 
 * company production environment which can hit 100k messages per second.
 * <tt>zk.connect: </tt> the zookeeper ip kafka use.<p>
 * <tt>topic: </tt> the topic to read from kafka.<p>
 * <tt>batchSize: </tt> send serveral messages in one request to kafka. <p>
 * <tt>producer.type: </tt> type of producer of kafka, async or sync is available.<o>
 * <tt>serializer.class: </tt>{@kafka.serializer.StringEncoder}
 */
public class LoggerSink extends AbstractSink implements Configurable{
	
	private static final Logger log = LoggerFactory.getLogger(LoggerSink.class);
	
	public Status process() throws EventDeliveryException {
		Channel channel = getChannel();
		Transaction tx = channel.getTransaction();
		try {
			tx.begin();
			Event e = channel.take();
			if(e == null) {
				tx.rollback();
				return Status.BACKOFF;
			}
			log.info(new String(e.getBody()) + "\n");
			tx.commit();
			return Status.READY;
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			return Status.BACKOFF;
		} finally {
			tx.close();
		}
	}

	@Override
	public synchronized void start() {
		super.start();
	}

	@Override
	public synchronized void stop() {
		super.stop();
	}

	@Override
	public void configure(Context context) {
//		LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory(); 
//		JoranConfigurator configurator = new JoranConfigurator(); 
//		configurator.setContext(lc); 
//		lc.reset(); 
//
//		String logbackPath = context.getString("logback.path", "logback.xml");
//		if(!new File(logbackPath).exists()){
//			URL url = LoggerSink.class.getClassLoader().getResource("logback.xml");
//			if(url != null){
//				logbackPath = url.toString();
//			}
//		}
//		try { 
//		    configurator.doConfigure(logbackPath); 
//		} catch (JoranException e) { 
//		    e.printStackTrace(); 
//		} 
//
//		StatusPrinter.printInCaseOfErrorsOrWarnings(lc); 
	}
}
