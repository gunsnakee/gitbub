package com.meiliwan.search.util;

import java.util.ArrayList;
import java.util.List;


/**
 * parse query
 * 
 * @author lgn
 */
public class QueryStringUtil {

	/**
	 * clean query 
	 * this method will filter some illegal character and return a new query with SPACE separator
	 * 
	 * @param query   the user input query
	 * @param separator separator for new query
	 * @return String the new query after clean
	 */
	public static String cleanQuery(String query, String separator){
		int length = query.length();
		StringBuilder chs = new StringBuilder();
		boolean separatorEnd = false;
		for(int i = 0 ; i < length; i++){
			char ch = (query.charAt(i));
			if ((ch >= 0x4E00 && ch <= 0x9FA5) 
					|| (ch >= 0x0030 && ch <= 0x0039) 
					||  (ch >= 0x0061 && ch <= 0x007A)){
				chs.append(ch);
				separatorEnd = false;
			}
			else if ((ch >= 0x0041 && ch <= 0x005A)){
				chs.append((char)(ch + 0x0020));
				separatorEnd = false;
			}else if ((ch >= 0xFF21 && ch <= 0xFF3A) ){ //full with uppercase
				ch -= 0xFEE0 ;
				ch += 0x0020;
				chs.append((char)(ch));
				separatorEnd = false;
			}
			else if ((ch >= 0xFF41 && ch <= 0xFF5A)  //full with lowercase or number
					|| ch >= 0xFF10 && ch <= 0xFF19){
				ch -= 0xFEE0 ;
				chs.append((char)(ch));
				separatorEnd = false;
			}			
			else{
				if (!separatorEnd){
					chs.append(separator);
					separatorEnd = true;
				}else{
					;
				}
			}
		}
		return chs.toString();
	}

	/**
	 * Judge whether the input string is a special string which be composed of English chars 
	 * 
	 * @param u the input String
	 * @return boolean true means than the input u is composed of English chars
	 */
	public static boolean isOnlyEnChar(String u){
		for(int i= 0; i < u.length(); i++){
			char ch = (u.charAt(i));
			if ((ch >= 0x0041 && ch <= 0x005A) || (ch >= 0x0061 && ch <= 0x007A)|| (ch >= 0x0030 && ch <= 0x0039) )
				;
			else
				return false;
		}
		return true;
	}

	/**
	 * segment input query
	 * Using space char as split char
	 * 
	 * @param query
	 * @return String[]
	 */
	public static String[] seg(String query){
		int length = query.length();
		StringBuilder chs = new StringBuilder();
		for(int i = 0 ; i < length; i++){
			char ch = (query.charAt(i));
			if ((ch >= 0x4E00 && ch <= 0x9FA5) )
				chs.append(ch+"."); //chinese
			else if (ch >= 0x0030 && ch <= 0x0039) 
				chs.append(ch+";"); //num
			else if ((ch >= 0x0061 && ch <= 0x007A))
				chs.append(ch+","); //english
			else if ((ch >= 0x0041 && ch <= 0x005A))
				chs.append((char)(ch + 0x0020)+","); 
			else
				chs.append("\t");
		}

		String[] wordstotal = chs.toString().trim().split("\t+");
		String[] neww = new String[wordstotal.length];

		/*
		 * type:
		 *   0、没有输入或空格
		 *   1、 中文
		 *   2、数字
		 *   3、中文数字混合
		 *   4、英文/拼音
		 *   5、中英混合
		 *   6、英文数字混合
		 *   7、中英数字混合
		 */
		for(int i = 0; i < wordstotal.length; i++){
			int type = 0;
			if (wordstotal[i].contains("."))
				type+=1;
			if (wordstotal[i].contains(";"))
				type+=2;
			if (wordstotal[i].contains(","))
				type+=4;
			neww[i] = wordstotal[i].replaceAll("\\.|;|,","")+":"+type;
		}

		return neww;
	}

	public static char escapeChar(char ch){
		if (ch >= 0x4E00 && ch <= 0x9FA5){
			return ch ;
        }
		else if ((ch >= 0x0041 && ch <= 0x005A) ){
			ch += 0x0020;
		}else if(ch >= 0x0061 && ch <= 0x007A)
			;
		else if (ch >= 0x0030 && ch <= 0x0039)
			;
		else if (ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n' || ch == '　'){	
			ch = ' ';
		}
		// Punctuation Marks
		else if ((ch >= 0x0021 && ch <= 0x00BB) || (ch >= 0x2010 && ch <= 0x2642)
				|| (ch >= 0x3001 && ch <= 0x301E))
		 ;

		// Full-Width range
		else if ((ch >= 0xFF21 && ch <= 0xFF3A) ){
			ch -= 0xFEE0 ;
			ch += 0x0020;
		}else if(ch >= 0xFF41 && ch <= 0xFF5A){
			ch -= 0xFEE0 ; 
		}else if (ch >= 0xFF10 && ch <= 0xFF19){
			ch -= 0xFEE0 ;
		}else if (ch >= 0xFE30 && ch <= 0xFF63){
			if (ch> 65280&& ch< 65375)
				ch -= 0xFEE0 ;
			;
		}else{
			if (ch> 65280&& ch< 65375)
				ch -= 0xFEE0 ;
			 ;
		}
		return ch;
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

	public static List<String> cutSequence(String sequence){
		char[] newsent = new char[sequence.length()];
		int[] charTypes = transformSentence(sequence, newsent);
		List<String> result = new ArrayList<String>();
		int i = 0, j= 1;
		StringBuilder sb = new StringBuilder();
		sb.append(sequence.charAt(0));
		for( ;j < charTypes.length; i++, j++){
			if (canBeConcat (charTypes[i], charTypes[j])){
				sb.append(escapeChar(sequence.charAt(j)));
			}else{
				if (sb.length() > 0)
					result.add(sb.toString());
				sb = new StringBuilder();
				if (charTypes[j] != StringNode.DELIMITER && charTypes[j] != StringNode.SPACELIKE){
					sb.append(escapeChar(sequence.charAt(j)));
				}else{
					
				}
			}
		}
		if (sb.length() > 0){
			result.add(sb.toString());
		}
		return result;
	}



	public static void main(String[] args) {
		//		for(String a : seg("^我们 chinese 123 ,., 我们123 chinese123 b汗 我们12chinese"))
		//			System.out.println(a);
		//		for(String a : seg("  AV啊"))
		//			System.out.println(a);
		String in = "今天１０.....G内存";

		System.out.println(cutSequence(in));
		System.out.println(cleanQuery("凳子 大象", ""));
		System.out.println(escapeChar('，'));
//		System.out.println(Strings.split(cleanQuery(in, " "), "\\s+"));
		
	}

}
