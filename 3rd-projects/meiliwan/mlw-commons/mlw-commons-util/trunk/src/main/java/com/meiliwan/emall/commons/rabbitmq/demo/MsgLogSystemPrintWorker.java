package com.meiliwan.emall.commons.rabbitmq.demo;

import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskWorker;

public class MsgLogSystemPrintWorker implements MsgTaskWorker {

	@Override
	public void handleMsg(String msg) {
		System.out.println(msg);
	}

}
