package com.meiliwan.emall.log.dao.impl;

import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.spring.support.SqlSessionDaoSupport;
import com.meiliwan.emall.log.bean.BizLog;
import com.meiliwan.emall.log.dao.BizLogDao;

public class BizLogDaoImpl extends SqlSessionDaoSupport implements BizLogDao{
	
	private static final String SHARD_KEY = "ShardLog";
	
	@Override
	public int insert(BizLog bizLog) {
		ShardParam shardParam = new ShardParam(SHARD_KEY, bizLog.getModel(), bizLog);
		return getSqlSession().insert("BASE_Log.insertSelective", shardParam) ;
	}
	
}
