package com.meiliwan.emall;

import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenter;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AsyncMsgTaskSvr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:conf/spring/async-task-dal.xml");
		MsgTaskCenter.initTaskCenter();
	}
	
}
