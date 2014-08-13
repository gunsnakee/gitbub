package com.meiliwan.search.pinyin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * 专门解析汉字拼音，汉字字典认为“永远”不变
 * @author lgn
 *
 */
public class PinyinEnumerator {
	final static HanziToPinyinDict hanziDict = HanziToPinyinDict.getInstance();
//	final static boolean[] vowels = new boolean[256];
	final static HashSet<Integer> concatenates = new HashSet<Integer>();
	static{

		concatenates.add(mergeChars('j','a'));
		concatenates.add(mergeChars('j','e'));
		concatenates.add(mergeChars('j','o'));
		concatenates.add(mergeChars('x','a'));
		concatenates.add(mergeChars('x','e'));
		concatenates.add(mergeChars('x','o'));
		concatenates.add(mergeChars('r','o'));
		concatenates.add(mergeChars('a','a'));
		concatenates.add(mergeChars('a','e'));
		concatenates.add(mergeChars('e','u'));
		concatenates.add(mergeChars('e','e'));
		concatenates.add(mergeChars('e','o'));
		concatenates.add(mergeChars('o','a'));
		concatenates.add(mergeChars('o','e'));
		concatenates.add(mergeChars(' ','a'));
		concatenates.add(mergeChars(' ','o'));
		concatenates.add(mergeChars('o','e'));
	}
	
	/**
	 * 枚举汉语的所有拼音可能性
	 * @param words
	 * @return
	 */
	public static List<String> enumPinyin(String words)
	{
		if(words==null || words.length()==0){
			return null;
		}
		List<String> pinyins = new ArrayList<String>();
		List<String> tmppy = new ArrayList<String>();
		String[] splitwords = words.toLowerCase().split("");

		String[] first = hanziDict.getPinyinByWord(splitwords[1].charAt(0));	
		// 最多出2个多音
		first = Arrays.copyOf(first, Math.min(first.length, 2));
		if(first==null)
			pinyins.add(" ");
		else{
			for (String py : first){
				pinyins.add(py);
			}
		}
		int u = 0;
		int last = 0;
		for(int i = 2; i < splitwords.length ; i++)
		{
			int j;
			String[] currentPY = hanziDict.getPinyinByWord(splitwords[i].charAt(0));
			// 最多出2个多音
			currentPY = Arrays.copyOf(currentPY, Math.min(currentPY.length, 2));
			for(last = pinyins.size(),j = pinyins.size()-1; j >= u ; j--)
			{

				if(currentPY==null)
					pinyins.add(pinyins.get(j)+" ");
				else{
					for (String py : currentPY){
						pinyins.add(pinyins.get(j)+py);
					}
				}
			}
			u = last;
		}
		for(int j = pinyins.size()-1 ; j >= u ; j--)
		{
			tmppy.add(pinyins.get(j));
		}
		return tmppy;
	}

	private static int mergeChars(char first, char second){
		return ((int)first) * 10000 +  ((int)second);
	}
	
	/**
	 * each hanzi is transform to only one pinyin
	 * 
	 * @param words
	 * @return
	 */
	public static String wordsToPinyin(String words){
		String[] splitwords = words.toLowerCase().split("");
		StringBuilder builder = new StringBuilder();
		for(int i = 1; i < splitwords.length ; i++){
			String[] currentPY = hanziDict.getPinyinByWord(splitwords[i].charAt(0));
			if (currentPY == null){
				builder.append(" ");
			}else{
				builder.append(currentPY[0]);
			}
		}
		return builder.toString();
	}
	
	public static boolean isVowel(char c){
		if (c == 'a' || c == 'e' || c == 'o')
			return true;
		return false;
	}
	
	public static boolean isVowel(String pinyin){
		if (pinyin != null && !pinyin.isEmpty())
			return isVowel(pinyin.charAt(0));
		else 
			return false;
	}
	
	public static boolean canBeConcatenate(char head, char tail){
		if (head == ' ' || !isVowel(tail)){
			return true;
		}else if ( isVowel(tail)){
			if (concatenates.contains(mergeChars(head, tail))){
				return true;
			}
			return false;
		}
		return false;
	}
	
	/**
	 * 枚举辅音, "中国" 变成zhg 和zhongg. 每个汉字只选一个音
	 * @param words
	 * @param prefixSequence 如果true，对辅音串产生所有2~length的前缀。
	 * @return
	 */
	public static List<String> yieldConsonants(String words, boolean prefixSequence){
		String[] splitwords = words.toLowerCase().split("");
		ArrayList<String> tmp = new ArrayList<String>();
//		char lastC = ' ';
		for(int i = 1 ; i < splitwords.length; i++){
			String[] currentPY = hanziDict.getPinyinByWord(splitwords[i].charAt(0));
			if (currentPY == null){
				tmp.add(" ");
			}else{
				tmp.add(currentPY[0]);
			}
		}
		ArrayList<String> result = new ArrayList<String>();
		StringBuilder full = new StringBuilder();
		for(int i = 0 ; i < tmp.size(); i++){
			StringBuilder currentAbbr = new StringBuilder(full);
			int j = i+1;
			for(; j < tmp.size(); j++){
				if (canBeConcatenate(tmp.get(j-1).charAt(0), tmp.get(j).charAt(0))){
					currentAbbr.append(tmp.get(j-1).charAt(0));
				}
			}
			//last
			currentAbbr.append(tmp.get(j-1).charAt(0));
			result.add(currentAbbr.toString());
			full.append(tmp.get(i));
		}
		//----------add-----------------
		if (prefixSequence){
			if (result.get(0).length() > 2){
				String first = result.get(0);
				for(int i = 2 ; i < result.get(0).length(); i++){
					result.add(first.substring(0, i));
				}
			}
		}
		return result;
	}
	

}
