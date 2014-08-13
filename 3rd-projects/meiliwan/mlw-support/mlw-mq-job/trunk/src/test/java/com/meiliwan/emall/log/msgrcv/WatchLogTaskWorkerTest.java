package com.meiliwan.emall.log.msgrcv;

import org.testng.annotations.Test;

import com.meiliwan.emall.commons.util.StringUtil;

public class WatchLogTaskWorkerTest {

	@Test
	public void getClinetIP(){
		String str="clientIP:10.249.9.235,localIP:10.249.15.198";
		str="";
		str=null;
		if(!StringUtil.checkNull(str)&&str.length()>21){
			String ip = str.substring(9, str.indexOf(","));
			System.out.println(ip);
		}
	}
}
