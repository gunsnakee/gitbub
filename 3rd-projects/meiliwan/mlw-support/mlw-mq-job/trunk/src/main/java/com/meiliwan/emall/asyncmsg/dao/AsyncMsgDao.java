package com.meiliwan.emall.asyncmsg.dao;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;


public interface AsyncMsgDao {

	    int insertSelective(AsyncMsg asyncMsg);

	    List<AsyncMsg> selectNeed2SendAsyncmsgBefore(Date time);
	    
	    int updateByPrimaryKeySelective(AsyncMsg asyncMsg);
	   
}
