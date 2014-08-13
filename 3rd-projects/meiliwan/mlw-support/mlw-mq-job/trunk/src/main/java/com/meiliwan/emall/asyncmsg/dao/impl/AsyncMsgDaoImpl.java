package com.meiliwan.emall.asyncmsg.dao.impl;

import java.util.Date;
import java.util.List;

import com.meiliwan.emall.asyncmsg.dao.AsyncMsgDao;
import com.meiliwan.emall.commons.rabbitmq.asyncmsg.AsyncMsg;
import com.meiliwan.emall.core.db.shard.spring.support.SqlSessionDaoSupport;

public class AsyncMsgDaoImpl extends SqlSessionDaoSupport implements AsyncMsgDao {
	
	@Override
	public int insertSelective(AsyncMsg asyncMsg) {
		
		return getSqlSession().insert("BASE_AsygncMsg.insertSelective", asyncMsg) ;
	}

	@Override
	public int updateByPrimaryKeySelective(AsyncMsg asyncMsg) {
		return getSqlSession().update("BASE_AsygncMsg.updateByPrimaryKeySelective", asyncMsg);
	}

	@Override
	public List<AsyncMsg> selectNeed2SendAsyncmsgBefore(Date time) {
		
		return getSqlSession().selectList("BASE_AsygncMsg.selectNowNeed2SendAsyncmsg", time);
	}

}
