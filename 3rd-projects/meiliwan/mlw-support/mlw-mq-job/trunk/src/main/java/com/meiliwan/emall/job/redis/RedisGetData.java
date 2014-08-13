package com.meiliwan.emall.job.redis;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.RedisSearchInfoUtil;

import java.util.Set;

/**
 * Created with IntelliJ IDEA. User: jiawuwu Date: 13-9-29 Time: 上午11:16 To
 * change this template use File | Settings | File Templates.
 */
public class RedisGetData {

    private RedisGetData(){

    }
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(RedisGetData.class);

	public static void main(String[] args) throws JedisClientException {

		if (args == null || args.length <= 0) {
			Set<Integer> proIds = RedisSearchInfoUtil.getModifiedProIds();
			LOG.debug("proIds:" + proIds);
		}else{
			String value = ShardJedisTool.getInstance().get(JedisKey.vu$test,
					 args[0]);
			LOG.debug("value:" + value);
		}

	}

}
