package com.meiliwan.com.emall.commons.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.util.RandomUtil;

public class RandomUtilTest {

	@Test
	public void checkRandom(){
		int range = 4 ;
		System.out.println(RandomUtil.rand(range));
		
		int codeLength = 12 ;
		System.out.println(RandomUtil.randomNumCode(codeLength));
		
		int min = 4 ;
		int max = 8 ;
		System.out.println(RandomUtil.randomNumRange(min, max));
		
		int codeLength0 = 6 ;
		System.out.println(RandomUtil.randomStrCode(codeLength0));
		
//		Map<Integer, Integer> dataCountMap = new HashMap<Integer, Integer>();
//		Random rand = new Random(System.currentTimeMillis());
//		System.out.println("============");
//		for(int i= 0 ; i< 100000000; i++){
//			int data = RandomUtil.nextGaussian(10);
////			double data = rand.nextGaussian();
//			int count = dataCountMap.get(data) == null ? 0 : dataCountMap.get(data);
//			dataCountMap.put(data, count + 1);
//		}
//		
//		System.out.println(dataCountMap);
		
		
		System.out.println(RandomUtil.randomStrCode(4));
		
		System.out.println(RandomUtil.randomAlphaAndNumStr(2));
	}
	
}
