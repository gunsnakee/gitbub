package com.meiliwan.com.emall.union.dao;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.EncryptTools;

public class ChannetQueryTest {
	
	private static String getEncrySignForOrder(String user, String start,String end,String orderId,long unixTime,String key){
		if (StringUtils.isBlank(user)||StringUtils.isBlank(start)||StringUtils.isBlank(end) || StringUtils.isBlank(orderId)||StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();	
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("orderid=").append(orderId).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	private static String getEncrySignForOrderStatus(String user, String start,String end,int orderStatus,long unixTime,String key){
		if (StringUtils.isBlank(user)||StringUtils.isBlank(start)||StringUtils.isBlank(end) || StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();	
		//sig=md5(user=***&start=***&end=***&orderid=***&orderstatus=***&unixtime=***&key=***)
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("orderstatus=").append(orderStatus).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	
	private static String getEncrySign(String user, String start,String end,long unixTime,String key){
		if (StringUtils.isBlank(user) || StringUtils.isBlank(start) || StringUtils.isBlank(end) || StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);
		System.out.println(stringBuilder.toString());

		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	private static String getEncrySignStr(String user, String start,String end,String unixTime,String key){
		if (StringUtils.isBlank(user) || StringUtils.isBlank(start) || StringUtils.isBlank(end) || StringUtils.isBlank(key)) {
			return "";
		}
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("user=").append(user).append("&");
		stringBuilder.append("start=").append(start).append("&");
		stringBuilder.append("end=").append(end).append("&");
		stringBuilder.append("unixtime=").append(unixTime).append("&");
		stringBuilder.append("key=").append(key);

		
		return EncryptTools.EncryptByMD5(stringBuilder.toString());
	}
	
	
//http://union.meiliwan.com/cps/query?user=channet&start=20140219120100&end=
	//20140221080100&orderid=000004260023&orderstatus=0&unixtime=1392946122603&sig=***
	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		//user=channet&start=20140225000000&end=20140225235959&unixtime=1393367627981&key=EFTPdQSdSSBeU7QJ
		String user = "channet";
		String start = "20140225000000";
		String end ="20140315235959" ;
		long  unixtime = DateUtil.getCurrentTimestamp().getTime();
		String key = "EFTPdQSdSSBeU7QJ";
		int orderstatus = 0;
		String orderid ="000006260024";
		String TimeRangeUrl = "http://union.meiliwan.com/cps/query/channet?user=channet&start="+start+"&end="+end+"&unixtime="+unixtime + "&sig=";
		String orderUrl = "http://union.meiliwan.com/cps/query/channet?user=channet&start="+start+"&end="+end+"&orderid="+ orderid +"&unixtime="+unixtime + "&sig=";
		String orderStatusUrl = "http://union.meiliwan.com/cps/query/channet?user=channet&start="+start+"&end="+end+"&orderstatus="+ orderstatus +"&unixtime="+unixtime + "&sig=";
		
		String order = getEncrySignForOrder(user, start, end, orderid, unixtime, key);
		String _orderstatus=getEncrySignForOrderStatus(user, start, end, orderstatus, unixtime, key);
		String timeRange = getEncrySign(user, start, end, unixtime, key);
		
		System.out.println(TimeRangeUrl+timeRange);
		System.out.println(orderUrl+order);
		System.out.println(orderStatusUrl+ _orderstatus);

		System.out.println("\nlinkTech Query Url:");
		System.out.println("http://union.meiliwan.com/cps/queryForLinkTech?yyyymmdd=20140314");
	}

}
