package com.meiliwan.emall;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenter;

public class AsyncMsgTaskSvr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("classpath:conf/spring/async-task-dal.xml");
		MsgTaskCenter.initTaskCenter();
	}
	
}
