package com.meiliwan.emall.tongji.db.shard;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 
 演示依据用户ID分库分表
 */
public class OmsShardStrategy extends ShardStrategy {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(OmsShardStrategy.class);
	@Override
	public DataSource getTargetDataSource() {
		Map<String, DataSource> map = this.getShardDataSources();
        DataSource ds = map.get("omsSourceSlave0");
        logger.debug("omsSourceSlave0="+ds);
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
