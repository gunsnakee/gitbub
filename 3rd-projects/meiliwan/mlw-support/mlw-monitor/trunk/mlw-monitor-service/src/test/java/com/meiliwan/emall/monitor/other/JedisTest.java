package com.meiliwan.emall.monitor.other;

import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;

public class JedisTest {

	public void test(){
		
		try {
			
			ShardJedisTool tool = ShardJedisTool.getInstance();
			tool.set(JedisKey.pms$allbrand, "", 1);
		} catch (JedisClientException e) {
			// TODO Auto-generated catch block
		}
		
		
	}
}
