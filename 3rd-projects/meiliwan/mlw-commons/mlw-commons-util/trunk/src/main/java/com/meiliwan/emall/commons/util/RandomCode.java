package com.meiliwan.emall.commons.util;

import java.util.Random;


public class RandomCode {


	private static final String[] ints = {"0","1","2","3","4","5","6","7","8","9"};
	
	private static final String[] strs = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	private RandomCode(){

    }
	
	/**
	 * 生成指定长度的字符串
	 * @param strarray 字符库数组
	 * @param codeLength 字符长度
	 * @return
	 */
	private static String randomCode(String[] strarray,int codeLength){
		StringBuilder sb = new StringBuilder();
		int  len = strarray.length;
		Random random = new Random();
		for(int i=0;i<codeLength;i++){
			sb.append(strarray[random.nextInt(len)]);
		}
		return sb.toString();
	}
	
	/**
	 * 生成指定长度的随机字符串（数字+大写英文字母）
	 * @param codeLength
	 */
	public static String  randomStrCode(int codeLength){
		return RandomCode.randomCode(strs, codeLength);
	}
	
	/**
	 * 生成指定长度的随机字符串(数字)
	 * @param codeLength
	 */
	public static String randomNumCode(int codeLength){
		return RandomCode.randomCode(ints, codeLength);
	}

    /**
     * 生成指定长度的随机字符串(数字)
     * @param min
     * @param max
     */
    public static int randomNumRange(int min, int max){
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;
    }
}
