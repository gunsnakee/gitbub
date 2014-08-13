package com.meiliwan.emall.oms.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;

import com.meiliwan.emall.commons.bean.PayCode;
import com.meiliwan.emall.oms.constant.OrderType;


public class TestUitl {

	public static int randomInt(){
		Random r = new Random();
		return r.nextInt();
	}
	
	public static int randomInt(int range){
		Random r = new Random();
		return r.nextInt(range);
	}
	
	public static double randomDouble(){
		Random r = new Random();
		return r.nextDouble();
	}
	
	public static float randomFloat(){
		Random r = new Random();
		return r.nextFloat();
	}
	/**
	 * 随机三个文字
	 * @return
	 */
	public static String randomThreeChar(){
		StringBuffer buffer=new StringBuffer("史努比这个人天分很高他自己也说过只要每天能坚持下来必成大器摄影家书法家作家俯卧撑大王");
		buffer.append("采访黄渤让我想起很久以前看过的一个大马戏团表演一个喜剧演员模仿杂技在梯子上向上攀高他看上去好像没有任何技巧所以摇摇晃晃狼狈不堪看得人都哈哈大笑");
		StringBuffer sb=new StringBuffer();
		Random r=new Random();
		int range=buffer.length();
		sb.append(buffer.charAt(r.nextInt(range)));
		sb.append(buffer.charAt(r.nextInt(range)));
		sb.append(buffer.charAt(r.nextInt(range)));
		return sb.toString();
	}
	
	public static String randomBuyType(){
		Random r=new Random();

		String[] buys= new String[]{"NML","PAK","GIF","ACT_my","ACT_zk","ACT_my_zk","ACT_mlj"};
		return buys[r.nextInt(buys.length)];
	}
	
	public static PayCode randomPayCode(){
		Random r=new Random();
		PayCode[] bt = PayCode.values();
		return bt[r.nextInt(bt.length)];
	}
	
	public static OrderType randomOrderType(){
		Random r=new Random();
		OrderType[] bt = OrderType.values();
		return bt[r.nextInt(bt.length)];
	}
	
	
	public static void main(String[] args) {
		BigDecimal bg = new BigDecimal(-1);
		 double f1 = bg.doubleValue();
		 DecimalFormat df = new DecimalFormat("#0.00");
        System.out.println(df.format(f1));
		System.out.println(randomInt(100000));
		System.out.println(randomBuyType());
		 BigDecimal   bd   =   new   BigDecimal("3.14159265");   
		  bd   =   bd.setScale(2,BigDecimal.ROUND_HALF_UP);  
		  System.out.println(bd);
	}
}
