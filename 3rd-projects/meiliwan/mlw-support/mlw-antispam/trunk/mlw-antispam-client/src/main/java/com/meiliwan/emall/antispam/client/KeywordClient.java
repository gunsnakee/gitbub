package com.meiliwan.emall.antispam.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.meiliwan.emall.antispam.bean.AntispamKeyword;
import com.meiliwan.emall.antispam.bean.KeywordAdminResult;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.icetool.IceClientTool;
import com.meiliwan.emall.icetool.JSONTool;

public class KeywordClient {
	
	/**
	 * 增加一个关键词
	 * @param antispamKeyword  关键词
	 * @return 
	 *    KeywordAdminResult.ADD_SUCC  添加成功
	 *    KeywordAdminResult.ADD_ERR   添加失败
	 *    KeywordAdminResult.HAS_EXIST 已经存在，添加失败
	 */
	public static KeywordAdminResult addKeyword(AntispamKeyword antispamKeyword){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("keywordAdminServiceImpl/addKeyword", antispamKeyword));
		return new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<KeywordAdminResult>(){}.getType());
	}
	
	/**
	 * 删除敏感词
	 * @param id
	 * @return
	 *    KeywordAdminResult.DEL_SUCC  删除成功
	 *    KeywordAdminResult.DEL_ERR   删除失败
	 *    KeywordAdminResult.NOT_FOUND 不存在，删除失败
	 */
	public static KeywordAdminResult delKeyword(Integer id){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("keywordAdminServiceImpl/delKeyword", id));
		
		return new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<KeywordAdminResult>(){}.getType());
	}
	
	/**
	 * 修改关键词
	 * @param antispamKeyword
	 * @return
	 */
	public static KeywordAdminResult updateKeyword(AntispamKeyword antispamKeyword){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("keywordAdminServiceImpl/updateKeyword", antispamKeyword));
		
		return new Gson().fromJson(returnObject.get("resultObj"), new TypeToken<KeywordAdminResult>(){}.getType());
	}
	
	/**
	 * 查询分页数据
	 * @param offset
	 * @param limit
	 * @return
	 */
	public static PagerControl<AntispamKeyword> getKeywordList(AntispamKeyword keyword, PageInfo pageInfo){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("keywordAdminServiceImpl/getKeywordList", keyword, pageInfo));		
		
		return new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<PagerControl<AntispamKeyword>>(){}.getType());
	}
	
	/**
	 * 搜索关键词
	 * @param word
	 * @return
	 */
	public static PagerControl<AntispamKeyword> search(AntispamKeyword keyword, String word, PageInfo pageInfo){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildParams("keywordAdminServiceImpl/searchKeyword", keyword, word, pageInfo));
		
		return new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<PagerControl<AntispamKeyword>>(){}.getType());
	}
	
	/**
	 * 批量删除敏感词
	 * @param ids
	 * @return
	 */
	public static KeywordAdminResult delKeywords(Integer[] ids){
		JsonObject returnObject = IceClientTool.sendMsg(IceClientTool.ANTISPAM_ICE_SERVICE, JSONTool
				.buildArrayParams("keywordAdminServiceImpl/delKeywords", ids));
		
		return new Gson().fromJson(returnObject.get("resultObj"), 
					new TypeToken<KeywordAdminResult>(){}.getType());
	}
}
