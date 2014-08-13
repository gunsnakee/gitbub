package com.meiliwan.emall.tongji.task.db.shard;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

import javax.sql.DataSource;

/**
 * 
 演示依据用户ID分库分表
 */
public class TongJiShardStrategy extends ShardStrategy {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(TongJiShardStrategy.class);
	@Override
	public DataSource getTargetDataSource() {
        DataSource ds = getMainDataSource();
        logger.debug("MainDataSource="+ds);
        return ds;
	}

	@Override
	public String getTargetSql() {
		return getSql();
//		String targetSql = getSql();
//		ShardParam shardParam = getShardParam();
//		//
//		Long param = (Long) shardParam.getShardValue();
//		String tableName = "user_" + (param % 2);
//		targetSql = targetSql.replaceAll("\\$\\[user\\]\\$", tableName);
//		return targetSql;
	}

}
