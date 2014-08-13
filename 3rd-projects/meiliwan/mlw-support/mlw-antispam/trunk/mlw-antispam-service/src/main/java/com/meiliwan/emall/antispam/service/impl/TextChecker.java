package com.meiliwan.emall.antispam.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;

import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.dao.AntispamKeywordDao;
import com.meiliwan.emall.antispam.pojo.KeywordCheckResult;
import com.meiliwan.emall.antispam.pojo.KeywordPosition;
import com.meiliwan.emall.antispam.service.ACChecker;
import com.meiliwan.emall.antispam.utils.KeywordUtil;
import com.meiliwan.emall.commons.exception.JedisClientException;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.commons.jedisTool.ShardJedisTool;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.DateUtil;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.commons.util.SpringContextUtil;
import com.mop.labs.stringMatch.online.staticPattern.multi.prefix.ac.AhoCorasick;
import com.mop.labs.stringMatch.online.staticPattern.multi.prefix.ac.SearchResult;

public class TextChecker implements ACChecker {
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(TextChecker.class);
	
	private static volatile long localLoadTime;
	
	private static String jedisId = "loadTime";
	
	private static ACChecker instance = new TextChecker();
	
	public static ACChecker getInstance(){
		return instance;
	}
	
	/**
	 * 关联词 类如 "习近平|受伤" 
	 */
	private volatile HashMap<String, Set<String>> wordsLink;
	
	/**
	 * Ac 算法树
	 */
	private volatile AhoCorasick ac ;
	
	private AntispamKeywordDao antispamKeywordDao;
	
	private TextChecker(){
		try {
			antispamKeywordDao = (AntispamKeywordDao) SpringContextUtil.getBean("antispamKeywordDaoImpl");
		} catch (BeansException e) {
			throw new ServiceException("get bean 'antispamKeywordDao' failed", e);
			
		}
		load(false);
	}
	
	/**
	 * 注意，只有在load成功才更新load时间，并将该时间和ac上传到redis，
	 * @return
	 */
	private boolean load(boolean needUpdateLoadTime) {
		if(antispamKeywordDao == null){
			LOG.warn("antispamKeywordDao is null", null, IPUtil.getLocalIp());
			return false;
		}
		//监测词库初始化信息
		Map<String, Object> remark = new HashMap<String, Object>();
		remark.put("startTime", DateUtil.getCurrentDateStr());
		
		HashMap<String, Set<String>> wordsLinkTemp = new HashMap<String, Set<String>>();
        AhoCorasick acTemp = null;
        List<AntispamKeyword> keywordList = null;
        try {
        		keywordList = antispamKeywordDao.getAllEntityObj();
        } catch (Exception e) {
        		LOG.error(e, null, IPUtil.getLocalIp());
        }
        if(keywordList == null){
        		return false;
        }else if(keywordList.size() == 0){
	        	ac = acTemp;
	    		wordsLink = wordsLinkTemp;
	    		LOG.warn("keywordList is empty", null, IPUtil.getLocalIp());
	    		return true;
        }
        
        try {
			List<String> strings = new ArrayList<String>();
			for (AntispamKeyword keyword : keywordList) {
			    String word = keyword.getWord();
			    word = word.trim();
			    String[] words = word.split("\\|");
			    if (words.length > 1) {
			        words[0] = words[0].trim();
			        words[1] = words[1].trim();
			        if (wordsLinkTemp.containsKey(words[0])) {
			            wordsLinkTemp.get(words[0]).add(words[1]);
			        } else {
			            Set<String> s = new HashSet<String>();
			            s.add(words[1]);
			            wordsLinkTemp.put(words[0], s);
			        }

			        if (wordsLinkTemp.containsKey(words[1])) {
			            wordsLinkTemp.get(words[1]).add(words[0]);
			        } else {
			            Set<String> s = new HashSet<String>();
			            s.add(words[0]);
			            wordsLinkTemp.put(words[1], s);
			        }

			        strings.add(words[0]);
			        strings.add(words[1]);
			    } else {
			        strings.add(word);
			    }
			}
			Set<String> set = new HashSet<String>();
			for (String a : strings) {
			    set.add(a);
			}
			List<String> _list = new ArrayList<String>();
			for(Iterator<String> it = set.iterator();it.hasNext();){
				String keyword = it.next();
				_list.add(keyword);
				LOG.debug("antispam load keyword, keyword:{}", keyword);
			}
			
			acTemp = new AhoCorasick(_list, false);
			
			// 赋值 原子操作组合
			synchronized (this) {
			    ac = acTemp;
			    wordsLink = wordsLinkTemp;
			}
			localLoadTime = System.currentTimeMillis();
			if(needUpdateLoadTime){
				//更新时间并上传到redis
				ShardJedisTool.getInstance().set(JedisKey.antispam$loadTime, jedisId, localLoadTime);
			}
			//监测词库初始化信息
			remark.put("keywordCount", keywordList.size());
			remark.put("endTime", DateUtil.getCurrentDateStr());
			LOG.info("initTextChecker finished", remark, IPUtil.getLocalIp());
			return true;
		} catch (Exception e) {
			throw new ServiceException("load antispam keyword error", e);
		}
	}

	@Override
	public KeywordCheckResult search(String content) {
		KeywordCheckResult res = new KeywordCheckResult();
        List<KeywordPosition> positions = new ArrayList<KeywordPosition>();
        //获取词库更新时间，判断是否需要重载词库
        long loadTime = -1;
        String loadTimeStr = null;
        try {
			loadTimeStr = ShardJedisTool.getInstance().get(JedisKey.antispam$loadTime, jedisId);
		} catch (JedisClientException e) {
			//如果从redis获取更新时间失败，默认没有其他节点重载过，不再重载
			LOG.error(e, "get loadTime from redis error", IPUtil.getLocalIp());
		}
        if(!StringUtils.isBlank(loadTimeStr)){
        		loadTime = Long.parseLong(loadTimeStr);
        }
        if(loadTime > localLoadTime){//有其他节点重载了词库, 重载本地词库，不更新loadTime
        		load(false);
        }
        //如果词库一个词也没有，无法初始化ac
        if (ac == null) {
        		LOG.warn("ac is null", null, IPUtil.getLocalIp());
            return res;
        }
        List<SearchResult> searchResult = ac.searchByAutomata(content);

        for (SearchResult sr : searchResult) {
            if (wordsLink.containsKey(sr.getKeyword())) {
                Set<String> words = wordsLink.get(sr.getKeyword());
                for (SearchResult sr1 : searchResult) {
                    if (words.contains(sr1.getKeyword())) {
                        KeywordPosition p = new KeywordPosition();
                        p.setKeyword(sr.getKeyword());//?
                        p.setLength(sr.getEndPosition() - sr.getStartPosition());
                        p.setOffsize(sr.getStartPosition());
                        positions.add(p);
                    }
                }
            } else {
                KeywordPosition p = new KeywordPosition();
                p.setKeyword(sr.getKeyword());
                p.setLength(sr.getEndPosition() - sr.getStartPosition());
                p.setOffsize(sr.getStartPosition());
                positions.add(p);
            }
        }
        Set<String> keywords = new HashSet<String>();
        for (KeywordPosition position : positions) {
            keywords.add(position.getKeyword());
        }
        keywords = KeywordUtil.doKeyword(keywords, wordsLink);
        res.setKeywordPositions(positions);
        res.setKeywords(keywords);
        return res;
	}

	@Override
	public boolean reload() {
		return load(true);
	}

	@Override
	public List<SearchResult> searchKeywords(String text) {
		return ac.searchByAutomata(text);
	}

	@Override
	public HashMap<String, Set<String>> getWordsLink() {
		return wordsLink;
	}
	
	public void setWordsLink(HashMap<String, Set<String>> wordsLink) {
		this.wordsLink = wordsLink;
	}

	public static long getLocalLoadTime() {
		return localLoadTime;
	}

	public static void setLocalLoadTime(long localLoadTime) {
		TextChecker.localLoadTime = localLoadTime;
	}	
}
