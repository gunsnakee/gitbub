package com.meiliwan.emall.antispam.service;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.meiliwan.emall.antispam.pojo.KeywordCheckResult;
import com.mop.labs.stringMatch.online.staticPattern.multi.prefix.ac.SearchResult;

public interface ACChecker {
	
	KeywordCheckResult search(String content);

	boolean reload();
	
	/**
	 * 使用AC算法，匹配关键词
	 * @param text
	 * @return
	 */
	List<SearchResult> searchKeywords(String text);
	
	/**
	 * 获取关联词
	 * @return
	 */
	HashMap<String, Set<String>> getWordsLink();
}
