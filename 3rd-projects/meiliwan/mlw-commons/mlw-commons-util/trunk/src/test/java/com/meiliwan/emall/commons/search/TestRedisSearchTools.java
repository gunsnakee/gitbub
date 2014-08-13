package com.meiliwan.emall.commons.search;

import java.util.Set;

public class TestRedisSearchTools {

	public static void main(String[] args) {
		boolean ok = RedisSearchInfoUtil.addSearchInfo(15247074,15247074, 15247073, 15247067, 15247068, 15247070);
		System.out.println(ok);
		Set<Integer> modifiedProIds = RedisSearchInfoUtil.getModifiedProIds();
		System.out.println(modifiedProIds);
		
//		ok = RedisSearchInfoUtil.removeSearchInfos(modifiedProIds);
		System.out.println(ok);
		modifiedProIds = RedisSearchInfoUtil.getModifiedProIds();
		System.out.println(modifiedProIds);
		
	}

}
