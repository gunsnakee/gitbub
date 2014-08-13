package com.meiliwan.search.spell.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 正向匹配分词器。isMax为true是最长分词，否则是全切分
 * @author lgn-mop
 *
 */
public class FMMAnalyzer {

	//基础词库路径
	public  String DICT_PATH = "dic2.txt";
	//词库树根节点
	private Node _root;

//	public FMMAnalyzer() {
//		_root = new Node();
//	}
	public FMMAnalyzer(String w) {
		DICT_PATH = w;
		_root = new Node();
		this.initDictMap();
	}
	public Node getRoot() {
		return _root;
	}

	public void setRoot(Node _root) {
		this._root = _root;
	}

	public void initDictMap() {
		try {
			initDictMap(DICT_PATH);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 初始化词库树
	 * @throws java.io.IOException
	 */
	public void initDictMap(String dicPath) throws IOException {
		InputStream is = null;
        InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			is = new FileInputStream(dicPath);
	        isr = new InputStreamReader(is, "utf-8");
			br = new BufferedReader(isr);
			String line = null;
			while((line = br.readLine()) != null) {
				loadExternal(line);
			}
		} catch (IOException e) {
			throw e;
		} finally {
			if(br != null) {
				br.close();
			}
			if(isr != null) {
				isr.close();
			}
			if(is != null) {
				is.close();
			}
		}
	}
	
	public void loadExternal(String word) {
		Node temp = null;
		temp = _root;
		for(int i = 0; i < word.length(); i ++) {
			Character ch = word.charAt(i);
			//添加词库单字
			temp = temp.push(ch);
		}
		temp.set_allowEnd(true);
		//添加城市代码
	}
	
	/**
	 * 匹配词库树。isMax为true是最长分词，否则是全切分
	 * @param test
	 * @param isMax
	 * @return
	 */
	public List<String> analyze(String test, boolean isMax ) {
		ListData listData = new ListData();
		return process(listData, test, isMax);
	}
	
	
	public <T> T process(IData<T> data, String test, boolean isMax) {
		int count = 0; //强制循环次数不能太大，避免中间可能出现的BUG
		int index = 0;
		int len = test.length();
		
		//匹配词库单字过程中，当前匹配点
		Node current = null;
		
		//最后跳出时的node节点是否是词尾
		boolean lastAllow = false;
		String candidate = null;
		int candidateEnd = 0;
		while(index < len && count < 200) {
			StringBuilder _temp = new StringBuilder();
			//当前单字
			Character ch = test.charAt(index);
			//每次重新匹配都从根节点开始
			current = _root.get(ch);
			count ++;
			for(;current != null && count < 200;) {
				//若匹配到，则保存当前单字
				_temp.append(current.getSelfChar());
				lastAllow = current.is_allowEnd();
				//如果是词尾，而且 不是最大分词，则保存结果，继续分词
				if(lastAllow) {
					candidate = _temp.toString();
					candidateEnd = index ;
					if (!isMax){
						data.process(candidate);
						candidate = null;
					}
				}
				index ++;
				if(index >= len){
					//超出边界
					break;
				}
				//移动到下一个单字节点
				ch = test.charAt(index);
				current = current.get(ch);
				count ++;
				//若为空，则回退
				if(current == null) {
					index --;
					break;
				}
			}
			//最短匹配词长度必须大于一
			if(_temp.length() > 0){
				//如果 是最大分词，而且是词尾，则保存结果
				//非最大分词，结果在上面保存
				if(isMax && lastAllow){
					candidate = _temp.toString();
					candidateEnd = index;
					data.process(candidate);
					candidate = null;
				}else{
					//如果不是词尾，则回退
					if (isMax == false)
						index = index - (_temp.length() - 1);
					else {
						if (candidateEnd == 0)
							;
						else
							index = candidateEnd;//从上一次的词尾开始
					}
					if (candidate != null){
						data.process(candidate);
						candidate = null;
					}
				}
			}
			index ++;
			
		}
		return data.value();
	}
	
	
	public List<String> analyze(String s) {
		return analyze(s, true);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		FMMAnalyzer fmm = new FMMAnalyzer("data/indexes/product_1141/product.word");
		System.out.println(fmm.analyze("xiaoling", true));
		System.out.println(URLEncoder.encode("~", "UTF-8"));
	}
	
}
