package com.meiliwan.emall.core.db.shard.spring.support;

import java.util.Map;

import javax.sql.DataSource;

/**
 * @Description: 多数据库支持接口
 */
public interface MultiDataSourceSupport {
	public DataSource getMainDataSource();

	public Map<String, DataSource> getShardDataSources();
}
