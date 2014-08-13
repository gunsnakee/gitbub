package com.meiliwan.emall.commons.web;

import org.testng.annotations.Test;

public class TextUtilTest {

	@Test
	public void testTextUtil(){
		StringBuffer body = new StringBuffer() ;
		body.append("<html>");
		body.append("<div>");
		body.append("<H1>");
		body.append("--div--img--aaa----bbb---ddd----<html>---ccc----div--");
		body.append("</H1>");
		body.append("</div>");
		body.append("</html>");
		System.out.println(TextUtil.cleanHTML(body.toString()));
	}
}
