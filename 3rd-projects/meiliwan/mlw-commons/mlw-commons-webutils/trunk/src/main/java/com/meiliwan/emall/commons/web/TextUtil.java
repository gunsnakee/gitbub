package com.meiliwan.emall.commons.web;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class TextUtil {

	private final static Whitelist whitelist = new Whitelist()
	  .addTags("div", "span", "br", "p", "i", "b", "em",
	          "u", "strong", "img", "embed", "font",
	          "blockquote")
	  .addAttributes("i", "class")
	  .addAttributes("img", "src", "alt", "width")
	          //增加width白名单，用于前台图片大小压缩
	  .addProtocols("img", "src", "http")
	  .addAttributes("span", "class", "name", "style")
	  .addAttributes("font", "color", "size", "face")
	  .addAttributes("a", "href")
	  .addAttributes("embed", "src", "height", "width",
	          "align", "type", "wmode")
	  .addProtocols("a", "href", "http")
	  .addProtocols("embed", "src", "http");

	/**
	* 过滤HTML代码 过滤掉不含 whitelist 中定义的标签的html标签
	* 例如<html><div>aa</div></html> 返回 <div>aa</div>
	* @param body
	* @return
	*/
	public static String cleanHTML(String body) {
		return Jsoup.clean(body, whitelist);
	}
}
