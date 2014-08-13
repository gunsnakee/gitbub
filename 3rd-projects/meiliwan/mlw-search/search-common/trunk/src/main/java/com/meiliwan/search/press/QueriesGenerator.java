package com.meiliwan.search.press;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.meiliwan.search.pinyin.PinyinEnumerator;
import com.meiliwan.search.util.QueryStringUtil;
import com.meiliwan.search.util.io.FileUtil;

public class QueriesGenerator {
	static Random r = new Random();
	public static void cnToPy(String file, String out, float prob) throws IOException{
		BufferedReader reader = FileUtil.UTF8Reader(file);
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		for(String line = reader.readLine(); line!= null; line = reader.readLine()){
			String in = QueryStringUtil.cleanQuery(line.split("\t")[0], "");
			if (line.length() > 3){
				in = line.substring(0,3);
			}
			writer.write(in + "\n");
			String py = PinyinEnumerator.wordsToPinyin(in);
			List<String> cons = PinyinEnumerator.yieldConsonants(in, true);
			if (py.length() < 15 && r.nextDouble() > prob){
				writer.write(py + "\n");
			}
			for(String con : cons){
				if ( r.nextDouble() > prob){
					writer.write(con + "\n");
				}
			}
		}
		reader.close();
		writer.close();
	}

	

	public static void pinyinCut(String file, String out, float prob) throws IOException{
		BufferedReader reader = FileUtil.UTF8Reader(file);
		BufferedWriter writer = new BufferedWriter(new FileWriter(out));
		for(String line = reader.readLine(); line!= null; line = reader.readLine()){
			String in = line.split("\\s+")[0];
			if (in.length() < 4){
				writer.write(in + "\n");
				continue
				;
			}
			int yields = (int)Math.max(2, prob * in.length());
			for(int i = 0 ; i < yields; i++){
				writer.write(in.substring(0, r.nextInt(in.length()-3) + 3) + "\n");
			}
		}
		reader.close();
		writer.close();
	}
	/**
	 * @param args
	 * @throws java.io.IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String filePath = "d:/data/tmp.dat";
		String out = "d:/data/productq.txt";
//		cnToPy(filePath, out, 0.4f);
		pinyinCut(filePath, out, 0.7f);
	}

}
