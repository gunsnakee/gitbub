package com.meiliwan.search.util;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONObject;

/**
 * 处理JSON
 * 
 * @author lgn
 * 
 */
public final class JsonUtil {

	static Random rand = new Random();
	
	public static JSONObject parseStringToJson(String json){
		return JSONObject.fromObject(json);
	}
	
	/**
	 * output object as string 
	 * @return
	 */
	public static String strListToResponse(List<?> resultList){
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\", \"numFound\":\"");
		builder.append(resultList.size()).append("\",\"result\":[");
		if (resultList.isEmpty()){
			builder.append("]}");
			
		}else{
			for(int i = 0; i < resultList.size() - 1; i++){
				builder.append("\"").append(resultList.get(i).toString()).append("\",");
			}
			builder.append("\"")
				.append(resultList.get(resultList.size()-1).toString())
				.append("\"]}");
			}
		return builder.toString();
	}
	
	/**
	 * output object as string 
	 * @return
	 */
	public static String strArrayToResponse(Object[] resultList, String strategyJson){
		if (resultList == null){
			return "{\"status\":\"null\", \"numFound\":0,\"result\":null}";
		}
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\"");
		builder.append(", \"numFound\":" ).append(resultList.length);
		if (strategyJson != null){
			builder.append(",\"abtest\":").append(strategyJson);
		}
		builder.append(",\"result\":[");
		if (resultList.length == 0){
			builder.append("]}");
			
		}else{
			for(int i = 0; i < resultList.length - 1; i++){
				builder.append("\"").append(resultList[i].toString()).append("\",");
			}
			builder.append("\"")
				.append(resultList[resultList.length-1].toString())
				.append("\"]}");
			}
		return builder.toString();
	}
	
	
	
	/**
	 * 
	 * @param resultList
	 * @return
	 */
	public static String jsonArrayToResponseWithReqId(Object[] resultList, String reqIdPrefix, String strategyJson){
		if (resultList == null){
			return "{\"status\":\"null\", \"numFound\":0,\"result\":null}";
		}
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\"");
		builder.append(", \"numFound\":" ).append(resultList.length);
		if (reqIdPrefix != null){
			long k = rand.nextInt(100000) + 100000;
			String r = reqIdPrefix + System.currentTimeMillis() + String.valueOf(k).substring(1);
			builder.append(", \"reqId\":\"").append(r).append("\"");
		}
		if (strategyJson != null){
			builder.append(",\"abtest\":").append(strategyJson);
		}
		builder.append(",\"result\":[");
		if (resultList.length == 0){
			builder.append("]}");
			
		}else{
			for(int i = 0; i < resultList.length - 1; i++){
				builder.append(resultList[i].toString()).append(",");
			}
			builder.append(resultList[resultList.length-1].toString())
				.append("]}");
			}
		return builder.toString();
	}
	
	
	public static String jsonListToResponseWithReqId(List<?> resultList, String reqIdPrefix,String strategyJson){
		if (resultList == null){
			return "{\"status\":\"null\", \"numFound\":0,\"result\":null}";
		}
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\"");
		builder.append(", \"numFound\":" ).append(resultList.size());
		if (reqIdPrefix != null){
			long k = rand.nextInt(100000) + 100000;
			String r = reqIdPrefix + System.currentTimeMillis() + String.valueOf(k).substring(1);
			builder.append(", \"reqId\":\"").append(r).append("\"");
		}
		if (strategyJson != null){
			builder.append(",\"abtest\":").append(strategyJson);
		}
		builder.append(",\"result\":[");
		if (resultList.isEmpty()){
			builder.append("]}");
			
		}else{
			for(int i = 0; i < resultList.size() - 1; i++){
				builder.append(resultList.get(i).toString()).append(",");
			}
			builder.append(resultList.get(resultList.size()-1).toString())
				.append("]}");
			}
		return builder.toString();
	}
	
	
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public static JSONObject loadJSONFromFile(String filePath) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		StringBuilder builder = new StringBuilder();
		for(String line = br.readLine(); line!= null; line = br.readLine()){
			builder.append(line);
		}
		return JSONObject.fromObject(builder.toString());
	}
	
	
	public static String jsonObjToResponseWithReqId(String json, String reqIdPrefix,String strategyJson){
		if (json == null){
			return "{\"status\":\"null\",\"result\":null}";
		}
		StringBuilder builder = new StringBuilder("{\"status\":\"ok\"");
		if (reqIdPrefix != null){
			long k = rand.nextInt(100000) + 100000;
			String r = reqIdPrefix + System.currentTimeMillis() + String.valueOf(k).substring(1);
			builder.append(", \"reqId\":\"").append(r).append("\"");
		}
		if (strategyJson != null){
			builder.append(",\"abtest\":").append(strategyJson);
		}
		builder.append(",\"result\":" + json + "}");
		return builder.toString();
	}
	
}
