package com.meiliwan.emall.bkstage.web.html;


public class HtmlFilterUtil {
 
	/**
	 * 把经过Filter编码的字符串解析成正常人理解的内容
	 * @param value
	 * @return
	 */
	public static String encoding(String value){
		if(value==null){
			return null;
		}
        //过滤可能的SQL注入
        value = value.replaceAll("&lt;","<").replaceAll("&gt;",">");
        value = value.replaceAll("&#40;","\\(").replaceAll( "&#41;","\\)");
        value = value.replaceAll("&#39;","'");
        value = value.replaceAll("eval\\((.*)\\)", "");
        return value;
	}
	
	public static void main(String[] args) {
		
		String value = HtmlFilterUtil.encoding(null);
		System.out.println(value);
	}
}
