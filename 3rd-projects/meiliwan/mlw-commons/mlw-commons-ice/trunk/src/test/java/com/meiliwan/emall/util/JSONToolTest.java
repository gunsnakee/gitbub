package com.meiliwan.emall.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.pojo.HelloWorld;

public class JSONToolTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String str = JSONTool.buildMultiParams("sfsfs/sdfsf");
//		System.out.println(str);
//		
//		HelloWorld hw = new HelloWorld();
//		hw.setPrefix("hello");
//		hw.setSuffix("world");
//		str = JSONTool.buildMultiParams("sss/djddjd", hw);
//		System.out.println(str);
//		
//		str = JSONTool.buildMultiParams("kkk/ddd", 1, "str", hw, new int[]{1,2,3}, new HelloWorld[]{hw, hw, hw});
//		System.out.println(str);
//		
//		List<HelloWorld> hwList = new ArrayList<HelloWorld>();
//		hwList.add(hw);
//		hwList.add(hw);
//		
//		Set<HelloWorld> hwSet = new HashSet<HelloWorld>();
//		hwSet.add(hw);
//		hwSet.add(hw);
//		
//		Map<String, HelloWorld> hwMap = new HashMap<String, HelloWorld>();
//		hwMap.put("hw1", hw);
//		hwMap.put("hw2", hw);
//		
//		str = JSONTool.buildMultiParams("kkk/fff", hwList, hwSet, hwMap);
//		System.out.println(str);
		
		String str = JSONTool.buildParams("sdfs/sdj", 1, 2, 3);
		System.out.println(str);
		
		JsonObject elem = (JsonObject)new JsonParser().parse(str);
	
		System.out.println(elem.get("action").getAsString());
		System.out.println(elem.getAsJsonObject("params"));
		System.out.println(elem.getAsJsonObject("params").getAsJsonArray("elements").size());
		
		System.out.println("sdfsd");
		

	}

}
