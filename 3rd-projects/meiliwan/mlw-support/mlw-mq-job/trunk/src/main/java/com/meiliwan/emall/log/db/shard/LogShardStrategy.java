package com.meiliwan.emall.log.db.shard;

import java.util.Map;

import javax.sql.DataSource;

import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

public class LogShardStrategy extends ShardStrategy {
	
	@Override
	public DataSource getTargetDataSource() {
		ShardParam shardParam = getShardParam();
		Map<String, DataSource> map = this.getShardDataSources();
		// 分库策略
		String param = (String) shardParam.getShardValue().toString();
		String datasourceKey = param + "LogDataSource";
		// 获取分库
		DataSource dataSource = map.get(datasourceKey);
		if (dataSource == null){
			dataSource = getMainDataSource();
		}
		return dataSource;
	}

	@Override
	public String getTargetSql() {
		return getSql();
	}

}
