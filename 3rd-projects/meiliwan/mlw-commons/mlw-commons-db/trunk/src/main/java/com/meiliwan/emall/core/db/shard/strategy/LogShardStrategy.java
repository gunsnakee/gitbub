package com.meiliwan.emall.core.db.shard.strategy;

import java.util.Map;

import javax.sql.DataSource;

/**
 * 
 演示依据用户ID分库分表
 */
public class LogShardStrategy extends ShardStrategy {

	@Override
	public DataSource getTargetDataSource() {
		
		Map<String, DataSource> map = this.getShardDataSources();
		return map.get("dataSourceLog");
	}

	
	@Override
	public String getTargetSql() {
		String targetSql = getSql();
		/*ShardParam shardParam = getShardParam();
		//不需要分表，所以注释
		Integer param = (Integer) shardParam.getShardValue();
		String tableName = "user_" + (param % 2);
		targetSql = targetSql.replaceAll("\\$\\[user\\]\\$", tableName);*/
		return targetSql;
	}

}
