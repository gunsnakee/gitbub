package com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter;

public interface MsgTaskWorker {
	
	void handleMsg(String msg);
	
}
