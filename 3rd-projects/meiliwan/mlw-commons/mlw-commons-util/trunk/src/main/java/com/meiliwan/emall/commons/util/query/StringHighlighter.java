package com.meiliwan.emall.commons.util.query;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.Highlighter.Highlight;

public class StringHighlighter {

	public static boolean canBeConcat(int type1, int type2){
		if (type1 == StringNode.HANZI && type2 ==  StringNode.HANZI){
			return true;
		}else if ((type1 == StringNode.LETTER || type1 == StringNode.NUMBER)
				&& (type2 == StringNode.LETTER || type2 == StringNode.NUMBER)){
			return true;
		}else if (type1 == StringNode.OTHER && type2 ==  StringNode.OTHER){
			return true;
		}
		return false;
	}

	public static int[] transformSentence(String sent, char[] newsent){
		int[] charTypes =  new int[newsent.length];
		for(int i = 0 ; i < sent.length(); i++){
			char ch =sent.charAt(i);
			if (ch >= 0x4E00 && ch <= 0x9FA5)
				charTypes[i] = StringNode.HANZI ;
			else if ((ch >= 0x0041 && ch <= 0x005A) ){
				charTypes[i] = StringNode.LETTER ;
				ch += 0x0020;
			}else if(ch >= 0x0061 && ch <= 0x007A)
				charTypes[i] = StringNode.LETTER ;
			else if (ch >= 0x0030 && ch <= 0x0039)
				charTypes[i] = StringNode.NUMBER ;
			else if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '　'){
				charTypes[i] = StringNode.SPACELIKE ;	
				ch = ' ';
			}
			// Punctuation Marks
			else if ((ch >= 0x0021 && ch <= 0x00BB) || (ch >= 0x2010 && ch <= 0x2642)
					|| (ch >= 0x3001 && ch <= 0x301E))
				charTypes[i] = StringNode.DELIMITER ;

			// Full-Width range
			else if ((ch >= 0xFF21 && ch <= 0xFF3A) ){
				charTypes[i] = StringNode.LETTER ;
				ch -= 0xFEE0 ;
				ch += 0x0020;
			}else if(ch >= 0xFF41 && ch <= 0xFF5A){
				charTypes[i] = StringNode.LETTER ;
				ch -= 0xFEE0 ; 
			}else if (ch >= 0xFF10 && ch <= 0xFF19){
				charTypes[i] = StringNode.NUMBER ;
				ch -= 0xFEE0 ;
			}else if (ch >= 0xFE30 && ch <= 0xFF63){
				if (ch> 65280&& ch< 65375)
					ch -= 0xFEE0 ;
				charTypes[i] = StringNode.DELIMITER ;
			}else{
				if (ch> 65280&& ch< 65375)
					ch -= 0xFEE0 ;
				charTypes[i] = StringNode.DELIMITER ;
			}
			newsent[i] = ch;
		}
		return charTypes;
	}


	public static List<String> cutSequence(String sequence){
		char[] newsent = new char[sequence.length()];
		int[] charTypes = transformSentence(sequence, newsent);
		List<String> result = new ArrayList<String>();
		int i = 0, j= 1;
		StringBuilder sb = new StringBuilder();
		sb.append(sequence.charAt(0));
		for( ;j < charTypes.length; i++, j++){
			if (canBeConcat (charTypes[i], charTypes[j])){
				sb.append(sequence.charAt(j));
			}else{
				if (sb.length() > 0)
					result.add(sb.toString());
				sb = new StringBuilder();
				if (charTypes[j] != StringNode.DELIMITER && charTypes[j] != StringNode.SPACELIKE){
					sb.append(sequence.charAt(j));
				}else{

				}
			}
		}
		if (sb.length() > 0){
			result.add(sb.toString());
		}
		return result;
	}


	public static String highlight(String orginal, String query, String preTag, String postTag){
		String replaceString = preTag + "$0" + postTag;
		List<String> queryList = cutSequence(query);
		StringBuilder regexBuilder = new StringBuilder();
		if (queryList.isEmpty()){
			return orginal;
		}
		for(String q : queryList){
			if (q.charAt(0) >= 0x4E00 && q.charAt(0) <= 0x9FA5){
				String[] hanzi = q.split("");
				for(int i = 1; i < hanzi.length;i++){
					regexBuilder.append(hanzi[i] + "|");
				}
			}else{
				regexBuilder.append(q + "|"); 
			}
		}
		String  regex = regexBuilder.toString();
		if (regex.endsWith("|"))
			regex = regex.substring(0, regex.length()-1);
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(orginal);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			//根据当前匹配的敏感词的级别设定颜色
				m.appendReplacement(sb, replaceString);
		}
		m.appendTail(sb);
		return sb.toString();
	}
	
	/**
	 * default use &ltem&gt  &lt/em&gt
	 * @param orginal
	 * @param query
	 * @return
	 */
	public static String highlight(String orginal, String query){
		return highlight(orginal, query, "<span style=\'color:red\'>", "</span>");
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String orginal = "捷安特电动车18年销售冠军";
		String query = "安特8";
		System.out.println(highlight(orginal, query));
	}
}



