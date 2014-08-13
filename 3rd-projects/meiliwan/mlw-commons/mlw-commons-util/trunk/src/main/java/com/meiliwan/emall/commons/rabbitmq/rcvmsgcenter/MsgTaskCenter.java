package com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.rabbitmq.RabbitmqConstant;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.MsgTaskFlowshopConfig;
import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.MsgTaskWorkerConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class MsgTaskCenter {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(MsgTaskCenter.class);
	
	private static Connection connection = null;
	
	static {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost(RabbitmqConstant.MQ_HOST);
			// channel
			connection = factory.newConnection();
		} catch (IOException e) {
			LOG.error(new BaseException("msg-task-center connect to rabbitmq-svr err.", e), "RabbitmqConstant.MQ_HOST", "rabbitmqsvr.meiliwan.com");
		}
	}
	
	public static void main(String[] args) {
		initTaskCenter();
	}
	
	public static void initTaskCenter() {
		// 1. 初始化配置 & 2. 获取消息加工队列的配置;
		InputStream fi = ClassLoader.getSystemResourceAsStream("conf/rabbitmq-msgcenter.xml");
		MsgTaskCenterConfig msgTaskCenterConfig = initMsgTaskCenterConfig(fi);
		
		// 3. 分别创建不同的消息处理机和对应的消息队列;
		List<MsgTaskFlowshop> taskFlowshopList = parseFromConfig(msgTaskCenterConfig);
		// 4. 每个消息队列获取的消息交给消息处理机依次执行自己的处理队列;
		for (MsgTaskFlowshop flowshop : taskFlowshopList) {
			flowshop.startRun();
		}
	}
	
	/**
	 * 	  根据配置文件解析生成对应的java配置对象,
	 * 然后准备将这些配置对象注入到Java的操作对象中;
	 * @param is
	 * @return
	 */
	public static MsgTaskCenterConfig initMsgTaskCenterConfig(InputStream is){
		MsgTaskCenterConfig taskCenterConfig = null;
		Digester digester = new Digester();
		digester.addObjectCreate("msgtaskcenter-config",
				com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.class);
		digester.addSetProperties("msgtaskcenter-config", "belonged-model", "belongedModel");
		
		digester.addObjectCreate("msgtaskcenter-config/task-flowshop",
				com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.MsgTaskFlowshopConfig.class);
		digester.addSetProperties("msgtaskcenter-config/task-flowshop", "rabbit-msgtype", "rabbitMsgType");
		digester.addSetProperties("msgtaskcenter-config/task-flowshop", "flowshop-name", "flowshopName");
		
		digester.addObjectCreate("msgtaskcenter-config/task-flowshop/msg-patterns/msg-pattern",
				com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.MsgPatternConfig.class);
		digester.addBeanPropertySetter("msgtaskcenter-config/task-flowshop/msg-patterns/msg-pattern", "msgPattern");
		digester.addSetNext("msgtaskcenter-config/task-flowshop/msg-patterns/msg-pattern", 
				"addMsgPatternConfig",
				"com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig$MsgPatternConfig");
		
		digester.addObjectCreate("msgtaskcenter-config/task-flowshop/taskworkers/taskworker",
				com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig.MsgTaskWorkerConfig.class);
		digester.addSetProperties("msgtaskcenter-config/task-flowshop/taskworkers/taskworker");
		
		digester.addSetNext("msgtaskcenter-config/task-flowshop/taskworkers/taskworker",
				"addTaskWorkerConfig", 
				"com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig$MsgTaskWorkerConfig");
		digester.addSetNext("msgtaskcenter-config/task-flowshop", 
				"addTaskFlowshopConfig",
				"com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenterConfig$MsgTaskFlowshopConfig");

		try {
			taskCenterConfig = (MsgTaskCenterConfig) digester.parse(is);
		} catch (IOException e) {
			LOG.error(new BaseException("access MsgTaskCenterConfig io stream err.", e), "", "");
		} catch (SAXException e) {
			LOG.error(new BaseException("sax parse MsgTaskCenterConfig io stream err.", e), "", "");
		}
		return taskCenterConfig;
	}
	
	
	/**
	 * 提取配置文件，準備進行消息處理;
	 * @param msgTaskCenterConfig
	 * @return
	 */
	private static List<MsgTaskFlowshop> parseFromConfig(MsgTaskCenterConfig msgTaskCenterConfig){
		List<MsgTaskFlowshop> flowshopList = new ArrayList<MsgTaskFlowshop>();
		//1. task center ;
		if (msgTaskCenterConfig != null) {
			    //2. task flowshop;
				List<MsgTaskFlowshopConfig> taskFlowshopConfigList = msgTaskCenterConfig.getMsgTaskFlowshopConfigList();
				if (taskFlowshopConfigList != null && !taskFlowshopConfigList.isEmpty()) {
					// init taskFlowshopConfig list 
					// in order to get MsgTaskFlowshop list;
					for (MsgTaskFlowshopConfig taskFlowshopConfig : taskFlowshopConfigList) {
						MsgTaskFlowshop taskFlowshop = new MsgTaskFlowshop();
						
						// 3. flowshop - msgpatterns
						taskFlowshop.setMsgPatternList(taskFlowshopConfig.getMsgPatternList());
						taskFlowshop.setExchangeType(taskFlowshopConfig.getRabbitMsgType());
						taskFlowshop.setFlowshopName(taskFlowshopConfig.getFlowshopName());
						// 4. flowshop - taskworker 
						List<MsgTaskWorkerConfig> taskWorkerConfigList = taskFlowshopConfig.getMsgTaskWorkerConfigList();
						if (taskWorkerConfigList != null && !taskWorkerConfigList.isEmpty()) {
							for (MsgTaskWorkerConfig taskWorkerConfig : taskWorkerConfigList) {
								String className = taskWorkerConfig.getClassname();
								try {
									LOG.debug("now to load className {}", className);
									MsgTaskWorker taskWorker = (MsgTaskWorker)Class.forName(className).newInstance();
									taskFlowshop.addMsgTaskWorker(taskWorker);
								} catch (Exception e) {
									LOG.error(new BaseException("className[" + className + "] init err.", e), "", "rabbitmqsvr.meiliwan.com");
								}
							}
						}
						flowshopList.add(taskFlowshop);
					}
				}
		}
		
		return flowshopList;
	}
	
	/**
	 * @author leo
	 * 
	 * 设计上，不同的shop不同的exchange控制，且不同的消息队列名
	 * 
	 */
	public static class MsgTaskFlowshop {
		private String exchangeType = "" ;
		private String exchangeName = "";
		private String flowshopName = "";
		
		private List<String> msgPatternList = new ArrayList<String>();
		private List<MsgTaskWorker> msgTaskWorkerList = new ArrayList<MsgTaskWorker>();
		
		
		public String getExchangeType() {
			return exchangeType;
		}

		public void setExchangeType(String exchangeType) {
			this.exchangeType = exchangeType;
		}

		public List<MsgTaskWorker> getMsgTaskWorkerList() {
			return msgTaskWorkerList;
		}
		
		public void setMsgPatternList(List<String> msgPatternList) {
			this.msgPatternList = msgPatternList;
		}
		
		public void addMsgTaskWorker(MsgTaskWorker msgTaskWorker) {
			msgTaskWorkerList.add(msgTaskWorker);
		}
		
		public void setFlowshopName(String flowshopName) {
			this.flowshopName = flowshopName;
		}

		public  void  startRun() {
			if (connection == null) {
				LOG.error(new BaseException("msg-task-center has no connect with rabbitmq-svr"), "connection is null", "rabbitmqsvr.meiliwan.com");
				return;
			}
			if (msgTaskWorkerList == null || msgTaskWorkerList.isEmpty()) {
				LOG.error(new BaseException("msg-task-center's shop[" + flowshopName + "] has no valid workers"), "msgTaskWorkerList is null or blank" , "rabbitmqsvr.meiliwan.com");
				return;
			}
			
			try {
				Channel channel = declareChannel();
				// channel queue
				final String queueName = channel.queueDeclare().getQueue();
				
				// 1 queue 对应多个 msg-pattern
				if (msgPatternList != null && !msgPatternList.isEmpty()) {
					for (String  msgP: msgPatternList) {
						channel.queueBind(queueName, exchangeName, msgP);
					}
				}
				
				// consumer
				final QueueingConsumer consumer = new QueueingConsumer(channel);
				channel.basicConsume(queueName, true, consumer);
				
				// 创建队列消息接受和处理线程
				new Thread(new Runnable(){

					@Override
					public void run() {
						while (true) {
							try {
								QueueingConsumer.Delivery delivery =  consumer.nextDelivery();
								String msg = null;
								try {
									 msg = new String(delivery.getBody(), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									LOG.error(new BaseException("receive msg not support UTF-8", e), "", "rabbitmqsvr.meiliwan.com");
									 msg = new String(delivery.getBody());
								}
								for (MsgTaskWorker msgworker : msgTaskWorkerList) {
									long t1 = System.nanoTime();
									msgworker.handleMsg(msg);
									long t2 = System.nanoTime();
									LOG.debug("queuename[{}] flowshop[{}] taskworker [{}] done his job,  and cost [{}] ns ! " , new Object[] {queueName,flowshopName, msgworker.getClass().getName(), (t2 - t1)});
								}
							} catch (ShutdownSignalException e) {
								e.printStackTrace();
							} catch (ConsumerCancelledException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
					
				}).start();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
		
		private Channel declareChannel() throws IOException{
			Channel channel = connection.createChannel();
			// exchangeName & exchangeType
			if (RabbitmqConstant.FANOUT_MQ_EXCHANGE_TYPE.equals(exchangeType)) {
				exchangeName = RabbitmqConstant.FANOUT_MQ_EXCHANGE_NAME;
			} else if (RabbitmqConstant.TOPIC_MQ_EXCHANGE_TYPE.equals(exchangeType)) {
				exchangeName = RabbitmqConstant.TOPIC_MQ_EXCHANGE_NAME;
			}
			// channel declare
			channel.exchangeDeclare(exchangeName, exchangeType);
			return channel;
		}
		
	}
	
}
