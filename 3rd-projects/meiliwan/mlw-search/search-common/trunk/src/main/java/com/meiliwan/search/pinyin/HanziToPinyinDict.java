package com.meiliwan.search.pinyin;

import gnu.trove.map.hash.TCharObjectHashMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import com.meiliwan.emall.commons.util.BaseConfig;
//import com.meiliwan.search.common.Config;
//import com.meiliwan.search.util.Dictionary;

public class HanziToPinyinDict {
//	static ESLogger log = Loggers.getLogger(HanziToPinyinDict.class);
	
	/**
	 * Hanzi to pinying map, one hanzi may be have more then one pingyin
	 */
//	private static HashMap<String,String[]> pinyinTable = 
//		new HashMap<String, String[]>();
		
	private static TCharObjectHashMap<String[]> pinyinTable = new TCharObjectHashMap<String[]>();
	
	private static HanziToPinyinDict singleInstance = new HanziToPinyinDict();
	
//	private Dictionary wordLoader; 
	private HanziToPinyinDict(){
		try {
			this.load();
		} catch (IOException e) {
//			log.error("Load hanzi to pinyin file wrong, may be file is not exist");
		}
	}
	
	public static HanziToPinyinDict getInstance(){
		return singleInstance;
	}
	
	private void load() throws IOException{

		BufferedReader reader = null;
		if(BaseConfig.getValue("HanziToPingDict")!= null && new File(BaseConfig.getValue("HanziToPingDict")).isFile()){
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream( BaseConfig.getValue("HanziToPingDict") ),"utf-8"));
		}else{
			reader = new BufferedReader(new InputStreamReader(
				this.getClass().getResourceAsStream("hanzi_pinyin.txt"), "utf-8"));
		}
		
		String line = reader.readLine();
		for(;line!=null;line = reader.readLine()){

			String[] hanzi = line.split("\t");

			if (line.isEmpty() || hanzi.length < 2)
				continue;
			String[] pinyins = hanzi[1].split(",\\s+");

			pinyinTable.put(hanzi[0].charAt(0), pinyins);
		}
		pinyinTable.compact();
		reader.close();
	}
	
	public String[] getPinyinByWord(char ch){
		return pinyinTable.get(ch);
	}
	
	public int getHanziNum(){
		return pinyinTable.size();
	}
	
	public void addWordWithPinyin( String hanzi, String[] pingyinStrings ){
		if( pinyinTable.containsKey( hanzi.charAt(0)) ){
			String[] oldStrings = pinyinTable.get( hanzi.charAt(0) );
			ArrayList<String> tmpList = new ArrayList<String>();
			for (int i = 0; i < oldStrings.length; i++) 
				tmpList.add( oldStrings[i] );
			
			for (int i = 0; i < pingyinStrings.length; i++) {
				if( !tmpList.contains( pingyinStrings[i] ) )
					tmpList.add( pingyinStrings[i] );
			}
			
			pinyinTable.put(hanzi.charAt(0), tmpList.toArray( new String[tmpList.size()] ) );
		} else 
			pinyinTable.put( hanzi.charAt(0), pingyinStrings);
	}
	
	public String getPinyinByWords( String term ){
		if(term == null || term.length() == 0 )
			return term;
		
		char[] splitwords = term.toLowerCase().toCharArray();
		
		StringBuffer resultBuffer = new StringBuffer();
		for (int i = 0; i < splitwords.length; i++) {
			String[] pinyins = getPinyinByWord(splitwords[i]);
			if( pinyins == null || pinyins.length == 0 )
				resultBuffer.append( splitwords[i] );
			else 
				resultBuffer.append( pinyins[0] );
		}
			
		return resultBuffer.toString();
	}
}
