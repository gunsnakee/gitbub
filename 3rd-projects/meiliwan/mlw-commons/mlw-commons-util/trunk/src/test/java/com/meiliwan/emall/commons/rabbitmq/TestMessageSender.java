package com.meiliwan.emall.commons.rabbitmq;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.meiliwan.emall.commons.log.BizLog;
import com.meiliwan.emall.commons.log.BizLogModel;
import com.meiliwan.emall.commons.log.BizLogObjOrder;
import com.meiliwan.emall.commons.log.BizLogOp;
import com.meiliwan.emall.commons.log.BizLogSubModel;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author leo
 *
 * 说明：需要配置 rabbitmqsvr.meiliwan.com 和 对应的绑定 IP
 */
public class TestMessageSender {
	
	@BeforeMethod
	public void fanoutMsgRecevier (){
		
		new Thread(){

			@Override
			public void run() {
				try {
					ConnectionFactory factory = new ConnectionFactory();
					factory.setHost(RabbitmqConstant.MQ_HOST);
					Connection connection = factory.newConnection();
					
					Channel channel = connection.createChannel();
					channel.exchangeDeclare(RabbitmqConstant.FANOUT_MQ_EXCHANGE_NAME, RabbitmqConstant.FANOUT_MQ_EXCHANGE_TYPE);
					String queueName = channel.queueDeclare().getQueue();
					channel.queueBind(queueName, RabbitmqConstant.FANOUT_MQ_EXCHANGE_NAME, "");
					QueueingConsumer consumer = new QueueingConsumer(channel);
					channel.basicConsume(queueName, true, consumer);
					
					while(true) {
						try {
							QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				            System.out.println(new String(delivery.getBody(), "UTF-8"));
				        } catch (Exception e) {
				        	e.printStackTrace();
				        }
				    }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}.start();
		
	}
	
//	@BeforeMethod
//	public void topicMsgRecevier (){
//		
//		new Thread(){
//
//			@Override
//			public void run() {
//				System.out.println("topicMsgRecevier");
//				try {
//					ConnectionFactory factory = new ConnectionFactory();
//					factory.setHost(RabbitmqConstant.MQ_HOST);
//					
//					Connection connection = factory.newConnection();
//					
//					Channel channel = connection.createChannel();
//					channel.exchangeDeclare(RabbitmqConstant.TOPIC_MQ_EXCHANGE_NAME, RabbitmqConstant.TOPIC_MQ_EXCHANGE_TYPE);
//					String queueName = channel.queueDeclare().getQueue();
//					channel.queueBind(queueName, RabbitmqConstant.TOPIC_MQ_EXCHANGE_NAME, "*.log.durable");
//					QueueingConsumer consumer = new QueueingConsumer(channel);
//					channel.basicConsume(queueName, true, consumer);
//					
//					while(true) {
//						try {
//							QueueingConsumer.Delivery delivery = consumer.nextDelivery();
//				            System.out.println(new String(delivery.getBody(), "UTF-8"));
//				        } catch (Exception e) {
//				        	e.printStackTrace();
//				        }
//				    }
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//			
//		}.start();
//		
//	}
	
	
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testBaseSend() {
		String msg = "i'm ok , txs"; 
		try {
			MsgSender.baseSend(msg);
		} catch (Exception e) {
			Assert.fail();
			e.printStackTrace();
		}
		MsgSender.baseSend(null);
	}
	
	
	@Test
	public void testBizLog2db() throws Exception{
		// bkstage
		BizLog bizLog = new BizLog.LogBuilder(BizLogModel.BKSTAGE, new BizLogSubModel() {
			
			@Override
			public String getSubModelName() {
				return "SubModelName";
			}
		}, "SP-smallsp-add", new Date())
 		.setBlkFlag()
 		.setObj(BizLogObjOrder.ORDER, "1213213")
 		.setOp(BizLogOp.ADD).build();
		
		for (MqModel m : MqModel.values()) {
			Assert.assertNotNull(MsgSender.getInstance(m));
			MsgSender.bizLog(bizLog);
		}
	}
	
}
