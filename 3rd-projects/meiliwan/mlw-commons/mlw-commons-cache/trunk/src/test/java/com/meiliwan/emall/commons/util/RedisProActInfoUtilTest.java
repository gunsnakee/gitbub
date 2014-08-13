package com.meiliwan.emall.commons.util;

import java.util.List;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.bean.ProActInfo;

public class RedisProActInfoUtilTest {
	
//	@Test
	public void testGetUpdatedActIds(){
	   int[] actIds = RedisProActInfoUtil.getUpdatedActIds(false);	
	   
	   for(int actId : actIds){
		   System.out.println("actId:" + actId);
	   }
	}
	
	@Test
	public void testGetDelProActInfos(){
		List<ProActInfo> infos = RedisProActInfoUtil.getDelProActInfos(false);
		
		System.out.println(infos);
	}

}
