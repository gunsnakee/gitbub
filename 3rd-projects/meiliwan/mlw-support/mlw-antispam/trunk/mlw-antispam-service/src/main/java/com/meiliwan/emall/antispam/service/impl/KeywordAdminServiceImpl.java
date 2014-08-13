package com.meiliwan.emall.antispam.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.google.gson.JsonObject;
import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.bean.KeywordAdminResult;
import com.meiliwan.emall.antispam.dao.AntispamKeywordDao;
import com.meiliwan.emall.antispam.service.KeywordAdminService;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.IPUtil;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class KeywordAdminServiceImpl extends DefaultBaseServiceImpl implements KeywordAdminService {
	
	@Autowired
	AntispamKeywordDao dao;
	
	private static final MLWLogger LOG = MLWLoggerFactory.getLogger(KeywordAdminServiceImpl.class);
	
	@Override
	public void addKeyword(JsonObject resultObj, AntispamKeyword keyword) {
		KeywordAdminResult result = KeywordAdminResult.ADD_SUCC;
		//1.标准化处理
		String keywordContent = keyword.getWord();
//		keywordContent = StandardTranformCharFilter.getInstance().filter(keywordContent);
		keyword.setWord(keywordContent);
		//2.关联词排序
		if(keyword.getWord().contains("|")){
			String[] split = keyword.getWord().split("\\|");
			List<String> string = Arrays.asList(split);
			Collections.sort(string, new Comparator<String>() {
				
				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			String newKw = "";
			for (String s : string) {
				newKw += s;
				newKw += "|";
			}
			newKw = newKw.substring(0, newKw.length() - 1);
			keyword.setWord(newKw);
		}
		//3.检查是否已经存在
		AntispamKeyword ak = new AntispamKeyword();
		ak.setWord(keyword.getWord());
		List<AntispamKeyword> keywords = null;
		try {
			keywords = dao.getListByObj(ak);
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("keyword", ak);
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		if(keywords == null){
			result = KeywordAdminResult.ADD_ERR;
		}else if(keywords.size() > 0){
			result = KeywordAdminResult.HAS_EXIST;
		}else{//不存在，准备添加
			try {
				dao.insert(keyword);
			} catch (Exception e) {
				Map<String, Object> remark = new HashMap<String, Object>();
				remark.put("keyword", keyword);
				LOG.error(e, remark, IPUtil.getLocalIp());
				result = KeywordAdminResult.ADD_ERR;
			}
		}
		JSONTool.addToResult(result, resultObj);
	}

	@Override
	public void delKeyword(JsonObject resultObj, Integer id) {
		int effect = -1;
		try {
			effect = dao.delete(id);
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("id", id);
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		KeywordAdminResult result = null;
		if(effect == -1){
			result = KeywordAdminResult.DEL_ERR;
		}else if(effect == 0){
			result = KeywordAdminResult.NOT_FOUND;
		}else{
			result = KeywordAdminResult.DEL_SUCC;
		}
		JSONTool.addToResult(result, resultObj);
	}

	@Override
	public void updateKeyword(JsonObject resultObj, AntispamKeyword keyword) {
		int effect = -1;
		try {
			effect = dao.update(keyword);
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("keyword", keyword);
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		KeywordAdminResult result = null;
		if(effect == -1){
			result = KeywordAdminResult.UPDATE_ERR;
		}else{
			result = KeywordAdminResult.DEL_SUCC;
		}
		JSONTool.addToResult(result, resultObj);
	}

	@Override
	public void getKeywordList(JsonObject resultObj, AntispamKeyword keyword, PageInfo pageInfo) {
		int totalCounts = -1;
		List<AntispamKeyword> list = null;
		try {
			totalCounts = dao.getCountByObj(keyword);
			pageInfo.setTotalCounts(totalCounts);
			list = dao.getListByObj(keyword, pageInfo, null, " order by create_time desc ");
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("keyword", keyword);
			remark.put("pageInfo", pageInfo);
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		if(list == null){
			list = new ArrayList<AntispamKeyword>();
		}
		PagerControl<AntispamKeyword> result = new PagerControl<AntispamKeyword>();
		result.setEntityList(list);
		result.setPageInfo(pageInfo);
		JSONTool.addToResult(result, resultObj);
	}

	@Override
	public void searchKeyword(JsonObject resultObj, AntispamKeyword keyword, String word, PageInfo pageInfo) {
		String whereSql = " word like '%" + word + "%' ";
		int totalCount = 0;
		List<AntispamKeyword> list = null;
		try {
			totalCount = dao.getCountByObj(keyword, whereSql);
			pageInfo.setTotalCounts(totalCount);
			list = dao.getListByObj(keyword, pageInfo, whereSql, " order by create_time desc ");
		} catch (Exception e) {
			Map<String, Object> remark = new HashMap<String, Object>();
			remark.put("keyword", keyword);
			remark.put("pageInfo", pageInfo);
			remark.put("whereSql", "word like '%" + word + "%'");
			LOG.error(e, remark, IPUtil.getLocalIp());
		}
		
		if(list == null){
			list = new ArrayList<AntispamKeyword>();
		}
		PagerControl<AntispamKeyword> result = new PagerControl<AntispamKeyword>();
		result.setEntityList(list);
		result.setPageInfo(pageInfo);
		JSONTool.addToResult(result, resultObj);
	}

	@Override
	public void delKeywords(JsonObject resultObj, Integer[] ids) {
		KeywordAdminResult result = KeywordAdminResult.BATCH_DEL_SUCC;
		for(Integer id : ids){
			try {
				dao.delete(id);
			} catch (Exception e) {
				Map<String, Object> remark = new HashMap<String, Object>();
				remark.put("id", id);
				LOG.error(e, remark, IPUtil.getLocalIp());
				result = KeywordAdminResult.BATCH_DEL_ERR;
			}
		}
		JSONTool.addToResult(result, resultObj);
	}

}
