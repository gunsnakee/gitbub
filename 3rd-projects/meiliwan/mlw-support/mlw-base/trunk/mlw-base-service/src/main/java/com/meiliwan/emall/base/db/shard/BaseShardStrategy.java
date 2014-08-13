package com.meiliwan.emall.base.db.shard;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RandomUtil;
import com.meiliwan.emall.core.db.shard.ShardParam;
import com.meiliwan.emall.core.db.shard.strategy.ShardStrategy;

import javax.sql.DataSource;

import java.util.Map;

/**
 * 
 演示依据用户ID分库分表
 */
public class BaseShardStrategy extends ShardStrategy {
    private static final MLWLogger logger = MLWLoggerFactory.getLogger(BaseShardStrategy.class);
	@Override
	public DataSource getTargetDataSource() {
		ShardParam shardParam = getShardParam();
		Map<String, DataSource> map = this.getShardDataSources();
		
		boolean isUpdate = shardParam.isUpdate();
        //临时修改， 让它一直走主库
        //isUpdate = true;
		if(isUpdate){
            DataSource ds = getMainDataSource();
            logger.debug("Main.ds="+ds);
			return getMainDataSource();
		}else{

            //从库的数量
            if(map==null || map.isEmpty()){
                return  getMainDataSource();
            }

            DataSource ds = null;
            //因为Base的从库配置里写了CMS数据库连接的配置，所以对于Base来说要做map.size - 3
            int slaveNum = (map.size() - 3);
            if(slaveNum == 1){
                ds = map.get("dataSourceSlave0");
                logger.debug("dataSourceSlave0="+ds);
            }else if(slaveNum > 1){
                //从库里随即挑选一个连接返回
                String dsStr = "dataSourceSlave"+RandomUtil.rand(slaveNum);
                ds = map.get(dsStr);
                logger.debug(dsStr+"="+ds);
            }
            if(ds == null){
                ds =  getMainDataSource();
                logger.debug("Main.ds="+ds);
            }
            return  ds;
		}	
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
