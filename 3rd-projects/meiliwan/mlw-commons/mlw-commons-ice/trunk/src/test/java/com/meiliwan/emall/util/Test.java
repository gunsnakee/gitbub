package com.meiliwan.emall.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.util.ConfigOnZk;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.IceFileTool;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pojo.HelloWorld;

public class Test {

	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		// Method[] methods =HelloIntfImpl.class.getDeclaredMethods();
		// for(Method method : methods){
		// System.out.println(method.getName() + ": " +
		// Modifier.isPublic(method.getModifiers()));
		// }

		// System.out.println("hello-wolrd".split("-")[0]);

		// System.out.println(parseToObject("1", Integer.class));

		// System.out.println(JSONObject.class);

		// String result = JSONTool.buildMultiParams("sendMsg", 3, "string", new
		// HelloWorld());
		// System.out.println(result);
		//
		// result = JSONTool.buildParam("sendMsg", "sdfsj");
		// System.out.println(result);
		//
		// JSONObject obj = JSON.parseObject(result);
		// Object tt = parseToObject(obj.getJSONArray("params").getString(0),
		// String.class);
		// System.out.println(tt);

		// HelloWorld hw = new HelloWorld();
		// hw.setPrefix("aaa");
		// hw.setSuffix("bbb");

//		HelloWorld hw1 = new HelloWorld();
//		hw1.setPrefix("111");
//		hw1.setSuffix("222");
//		
//		HelloWorld hw2 = new HelloWorld();
//		hw2.setPrefix("111");
//		hw2.setSuffix("222");
//
//		List<HelloWorld> hws = new ArrayList<HelloWorld>();
//		hws.add(hw1);
//		hws.add(hw1);
		
//		String str = JSON.toJSONString(hw1);
//		String str1 = JSON.toJSONString(hw1);
//		
//		JSONArray array = new JSONArray();
//		array.add(str1);
//		array.add(str);
//		
//		JSONArray paramArr = new JSONArray();
//		paramArr.add(array);
//		
//		System.out.println(paramArr.toJSONString());

//		HelloWorld[] hwarr = new HelloWorld[2];
//		hwarr[0] = hw1;
//		hwarr[1] = hw1;
//		String result = JSONTool.buildArrayParam("sds", hwarr);
//		System.out.println(result);

//		JSONObject boj = JSON.parseObject(result);
//		List<HelloWorld> obj = JSON.parseObject(boj.getString("params"),
//				new TypeReference<List<HelloWorld>>() {
//				});
//		System.out.println(obj);
		
//		String result = JSONTool.buildMultiParams("sfs", "user", "passwd");
//		System.out.println(result);
		
//		String value = JSONTool.buildMultiParams("prop", 1,2,3,hws);
//		System.out.println(value);
//		
//		value = JSONTool.buildListParam("sds", hws);
//		System.out.println(value);
		
//		Action action = JSON.parseObject("show", );
//		System.out.println(action );
		
//		Method method = Action.class.getMethod("valueOf", new Class[]{String.class});
//		Object obj = method.invoke(null, "show");
//		
//		System.out.println(obj.getClass().isEnum());


//		System.out.println(Action.valueOf(null));
		
//		String remoteFilePath = IceFileTool.uploadFile("/Users/lsf/Pictures/bug.png", "/Users/lsf/Pictures/qingchun0707.jpg");
//		System.out.println(remoteFilePath);
		
		File[] files = IceFileTool.downloadFile(false, "/Users/lsf/Pictures/bug.png");
		System.out.println(files[0].getAbsolutePath());
	}
	
	static enum Action{
		show,update
	}

}
