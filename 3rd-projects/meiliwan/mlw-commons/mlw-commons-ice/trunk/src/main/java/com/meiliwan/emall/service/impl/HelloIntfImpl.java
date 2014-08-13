package com.meiliwan.emall.service.impl;

import java.io.File;
import java.util.List;

import com.google.gson.JsonObject;
import com.meiliwan.emall.pojo.HelloWorld;

public class HelloIntfImpl extends DefaultBaseServiceImpl {

	public  void watch(JsonObject resultObj,  HelloWorld hello, int aaa){
		
		System.out.println("watch msg : " + hello + "====" + aaa);
		
	}
	
	public synchronized  void handleArray(JsonObject resultObj,int[] ids){
		
		System.out.println("handleArray msg : " + ids);
		
	}
	
	public static  void handleList(JsonObject resultObj, List<HelloWorld> hwList){
		
		System.out.println("handleList msg : " + hwList);
		
//		throw new ServiceException("hello-code-1","sdfsjfdjsfjs", new Exception());
		
	}
	
	public void login(JsonObject resultObj, String userName, String passwd){
		System.out.println(userName + "," + passwd);
	}
	
	public void sendMail(JsonObject resultObj, File[] files, String userName, String passwd){
		System.out.println("file size:" + files.length);
		for(File file : files){
			System.out.println(file.getAbsolutePath() + "," + file.exists());
		}
	}

}
