package com.meiliwan.emall.log.msgrcv;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.rabbitmq.MqModel;
import com.meiliwan.emall.commons.rabbitmq.MsgSender;

public class WatchLogTest {

	@Test(invocationCount=2,threadPoolSize=3)  
	public void testWatch(){
		//watchLog.watchLog.logService
		int i=10;
		Random random = new Random();
		MsgSender sender = MsgSender.getInstance(MqModel.WATCHLOG);
		Date now = new Date();
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		long start = c.getTimeInMillis();
		c.add(Calendar.MILLISECOND, 11);
		long end  = c.getTimeInMillis();
		
		while(i--!=0){
			int randomNum = random.nextInt();
			sender.send(
					MqModel.WATCHLOG, "logService", "iceS(www.11ms.com)("+start+")("+end+")(clientIP:123.125.114.144,localIP:10.249.15.198)");
			
			if(randomNum%10==0){
				try {
					Thread.sleep(1000l);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//System.out.println("finish");
		}
	}
	
	@Test
	public void testTime(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		long start = c.getTimeInMillis();
		c.add(c.SECOND, 10);
		long end  = c.getTimeInMillis();
		System.out.println(start);
		System.out.println(end);
	}
}
