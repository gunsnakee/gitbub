package com.meiliwan.search.util;

import java.util.HashSet;

/**
 * 解析用户输入的URL，将其对应到不同的handler上
 */
public class UrlPathUtil {
	
	public final static String SEARCH = "search";
	public final static String SPELL = "spell";
	public final static String SUGGEST = "suggest";
	public final static String CATMATCH = "catmatch";
	
	public final static String SELECT = "select";
	public final static String UPDATE = "update";
	
	static HashSet<String> servicesNames ;
	static {
		servicesNames = new HashSet<String>();
		servicesNames.add(SEARCH);
		servicesNames.add(SPELL);
		servicesNames.add(SUGGEST);
		servicesNames.add(CATMATCH);
	}
	
	/**
	 * 检查当前的URL是否合法, a general method
	 * @param path
	 * @return boolean if true means it is a legal URL, else not
	 */
	public static boolean isLegalPath( String path ){
		String[] parts = path.split("/");
		return isLegalPath(parts);

	}
	
	private static boolean isLegalPath( String[] parts ){
		if (parts.length!=0 && parts[0].equals("") && parts.length < 4){
			return false;
		}else{
			if (servicesNames.contains(parts[1])
					 && (parts[3].equals(SELECT) || parts[3].equals(UPDATE))  ){
				return true;
			}
		}
		
		if (parts.length!=0 && !parts[0].equals("") && parts.length < 3){
			return false;
		}else{
			if (servicesNames.contains(parts[0])
					 && (parts[2].equals(SELECT) || parts[2].equals(UPDATE))  ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * null for illegal
	 * @param path
	 * @return
	 */
	public static String getCollNameSafely(String path){
		String[] parts = path.split("/");
		if (isLegalPath(parts)){
			return parts[1];
		}else
			return null;
	}
	
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public static String getCollName(String path){
		int startIndex = 0;
		int endIndex = 0;
		int s = 0;
		for(int i = 0 ; i < path.length();i++){
			char c = path.charAt(i);
			if (c == '/'){
				s += 1;
				if (s == 2){
					startIndex = i;
				}else if ( s == 3){
					endIndex = i;
					break;
				}
			}
		}
		if (endIndex <= startIndex )
			return null;
		return path.substring(startIndex + 1, endIndex);
	}
	
	
	public static void main(String[] args) {
		System.out.println( isLegalPath("/search/mall/select") );
		System.out.println( getCollName("/search/mall/select"));
	}
}
