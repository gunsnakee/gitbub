package com.meiliwan.emall.core.db.shard.demo.shard;

import java.util.Map;

import javax.sql.DataSource;

import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

/**
 * 
 演示依据用户ID分库分表
 */
public class UserShardStrategy extends ShardStrategy {

	@Override
	public DataSource getTargetDataSource() {
		ShardParam shardParam = getShardParam();
		
		//
		Long param = (Long) shardParam.getShardValue();
		Map<String, DataSource> map = this.getShardDataSources();
		if (param >= 10 && param <= 50) {
			return map.get("dataSourceSlave");
		}
		if (param >= 100) {
			return map.get("dataSourceSlave2");
		}
		return getMainDataSource();
	}

	@Override
	public String getTargetSql() {
		String targetSql = getSql();
		ShardParam shardParam = getShardParam();
		//
		Long param = (Long) shardParam.getShardValue();
		String tableName = "user_" + (param % 2);
		targetSql = targetSql.replaceAll("\\$\\[user\\]\\$", tableName);
		return targetSql;
	}

}
