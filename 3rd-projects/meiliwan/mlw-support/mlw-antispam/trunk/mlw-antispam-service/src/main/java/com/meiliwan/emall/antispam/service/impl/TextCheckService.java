package com.meiliwan.emall.antispam.service.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.meiliwan.emall.antispam.bean.AuditResult;
import com.meiliwan.emall.antispam.bean.AuditResultType;
import com.meiliwan.emall.antispam.filter.TextFilter;
import com.meiliwan.emall.antispam.filter.XSSFilter;
import com.meiliwan.emall.antispam.pojo.KeywordCheckResult;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.IPUtil;

public class TextCheckService {
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(TextCheckService.class);
	private static final Pattern unsafePattern = Pattern.compile("http://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?");
	
	private static TextCheckService instance = new TextCheckService();
	
	private TextCheckService(){};
	
	public static synchronized TextCheckService getInstance(){
		return instance;
	}
	
	/**
	 * 文本检查
	 * @param content
	 * @return
	 */
	public AuditResult checkText(String content) {
		// 1.文本预处理
		content = pretreat(content);
		// 2.繁简体转换
//		String stdContent = TextFilter.getInstance().standardFilter(content);
		
		AuditResult result = new AuditResult();
		// 3.链接检查
		for (Matcher matcher = unsafePattern.matcher(content);matcher.find();) {
			String url;
			try {
				url = matcher.group();
				result.getUrls().add(url);
			} catch (IllegalStateException e) {
				Map<String, Object> remark = new HashMap<String, Object>();
				remark.put("stdContent", content);
				remark.put("matcher", matcher);
				LOG.error(e, remark, IPUtil.getLocalIp());
			}
			result.setAuditResultType(AuditResultType.FORBIDDEN);
			return result;
		}
		// 4.xss过滤
		boolean isXSS = XSSFilter.detectXss(content);
		if(isXSS){
			result.setAuditResultType(AuditResultType.FORBIDDEN);
			Set<String> keywords = new HashSet<String>();
			keywords.add("xss");
			result.setForbiddenWords(keywords);
			return result;
		}
		// 5.违禁词检查
		KeywordCheckResult sresult = TextChecker.getInstance().search(content);
		if (sresult.getKeywordPositions() != null && sresult.getKeywordPositions().size() > 0) {
			result.setAuditResultType(AuditResultType.FORBIDDEN);
			Set<String> keywords = sresult.getKeywords();
			result.setForbiddenWords(keywords);
		}else{
			result.setAuditResultType(AuditResultType.NORMAL);
		}
		return result;
	}

	private String pretreat(String content) {
		String result = null;
		//1.空格压缩
		result = TextFilter.compress(content);
		//2.非法字符替换
		if(result != null){
			result = TextFilter.illegalCharFilter(result);
		}
		return result.toLowerCase();
	}
	
	
}
