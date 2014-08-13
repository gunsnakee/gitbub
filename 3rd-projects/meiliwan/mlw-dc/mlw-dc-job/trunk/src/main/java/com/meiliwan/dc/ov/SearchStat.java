package com.meiliwan.dc.ov;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;
import com.meiliwan.dc.PageView;
import com.meiliwan.dc.StatWorker;
import com.meiliwan.dc.TableStat;

@SuppressWarnings("uncheck")
public class SearchStat implements TableStat{

	public Map<String, int[]> compute(List<PageView> pvs) {
		Map<String, Integer> keyMap = new HashMap<String, Integer>();
		for(PageView pv : pvs){
			String url = pv.getVisitPage();
			int idx = url.indexOf("/search?");
			if (idx > 0){
				Map<String, String> split = Splitter.on("&").omitEmptyStrings().withKeyValueSeparator("=").split(url.subSequence(idx+8, url.length()));
				String keyword = split.get("keyword");
				if (keyword != null){
					try {
						String decode = URLDecoder.decode(keyword, "utf-8");
						Integer integer = keyMap.get(decode);
						if (integer == null){
							keyMap.put("use-search::" + decode + "::使用搜索", 1);
						}else{
							keyMap.put("use-search::" + decode + "::使用搜索", 1 + integer);
						}
					} catch (UnsupportedEncodingException e) {
					}
				}
			}
		}
//		return keyMap;
		return null;
	}

	public String getSQLFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addStatWorker(StatWorker... workers) {
		// TODO Auto-generated method stub
		
	}
	
}
