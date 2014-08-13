package com.meiliwan.emall.search.common;

public class SolrUtils {
	public static String extractAbbrFromSolr(String node){
		return node.replaceAll("\\D", "");
	}
	
	public static String extractNumberFromLive(String liveNode){
		return liveNode.replaceAll("\\D", "");
	}
	
	
	public static String extractAbbrFromClient(String nodeHttpUrl){
		int idx = nodeHttpUrl.lastIndexOf("/");
		String address = nodeHttpUrl.substring(7, idx);
		return address.replaceAll("\\W+", "");

	}
	
	public static String convertLiveToUrl(String node){
		return "http://" + node.replaceFirst("http://", "").replaceFirst("_", "/");
	}
}	
