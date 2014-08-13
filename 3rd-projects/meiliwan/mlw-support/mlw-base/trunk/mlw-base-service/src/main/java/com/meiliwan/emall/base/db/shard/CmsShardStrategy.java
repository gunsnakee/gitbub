package com.meiliwan.emall.base.db.shard;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

import javax.sql.DataSource;

import java.util.Map;

/**
 *base 中的标签读取碎片的数据库在 CMS 库中
 演示依据用户ID分库分表
 */
public class CmsShardStrategy extends ShardStrategy {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(BaseShardStrategy.class);
	@Override
	public DataSource getTargetDataSource() {
		Map<String, DataSource> map = this.getShardDataSources();
        //return map.get("cmsSlaveDataSource");

        DataSource ds = null;
        //因为CMS的数据库连接是写在Base的配置文件里的，所以这里要做map.size() - 3， 专门拿cmsSlaveDataSource的连接
        int slaveNum = (map.size() - 3);
        if(slaveNum == 1){
            ds = map.get("cmsSlaveDataSource0");
            logger.debug("cmsSlaveDataSource0="+ds);
        }else if(slaveNum > 1){
            //从库里随即挑选一个连接返回
            String dsStr = "cmsSlaveDataSource"+RandomUtil.rand(slaveNum);
            ds = map.get(dsStr);
            logger.debug(dsStr+"="+ds);
        }
        if(ds == null){
            ds = map.get("cmsSlaveDataSource0");
            logger.debug("cmsSlaveDataSource0="+ds);
        }
        return  ds;
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
