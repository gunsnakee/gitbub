package com.meiliwan.emall.service.union;


import com.meiliwan.emall.commons.rabbitmq.rcvmsgcenter.MsgTaskCenter;
import com.meiliwan.emall.service.BackgroundService;

public class UnionTaskService implements BackgroundService{


	@Override
	public void bgRun() {
		MsgTaskCenter.initTaskCenter("mqconf/union-mq-config.xml"); 
	}

}
