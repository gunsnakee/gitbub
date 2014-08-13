package com.meiliwan.emall.commons.util;

import java.util.Random;

/**
 * 数学工具类
 * @author yuxiong
 *
 */
public class MathUtil {

	/**
	 * 求指定范围的随机数
	 * @param range
	 * @return
	 */
	public static int rand(int range){
		Random random = new Random();
        return Math.abs(random.nextInt())%range;
	}
	
}
