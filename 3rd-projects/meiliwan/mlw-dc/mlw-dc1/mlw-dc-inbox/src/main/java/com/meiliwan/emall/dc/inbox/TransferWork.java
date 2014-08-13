package com.meiliwan.emall.dc.inbox;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Random;

public class TransferWork extends Thread {
	private static final Logger log=Logger.getLogger(TransferWork.class);
	boolean running=true;
	Random rnd=new Random();
	
	//mq 
	private static final String EXCHANGE_NAME = "dcmq";	
    Connection connection=null;
    Channel channel=null;
    
    public TransferWork(){
    	ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("rabbitmqsvr.meiliwan.com"); //dev: 10.249.15.196
        try{
	        connection = factory.newConnection();
	        channel = connection.createChannel();
	        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        }
        catch(Exception exp){
        	log.error("",exp);
        }
    }
	
	@Override
	public void run(){
		while(running){
			try {
				String pv=DcInboxData.queue.take();
				log.info("dc publisher["+pv+"]");
				channel.basicPublish(EXCHANGE_NAME, "", null, pv.getBytes());
				
			} catch (InterruptedException e) {
				log.error("",e);
			} catch (IOException e) {
				log.error("",e);
			}
		}
	}
	
	public final void setRunning(final boolean flag){
		this.running=flag;
	}
}
