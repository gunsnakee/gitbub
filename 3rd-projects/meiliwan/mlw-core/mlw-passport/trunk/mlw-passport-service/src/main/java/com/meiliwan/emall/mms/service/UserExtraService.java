package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.mms.bean.UserExtra;
import com.meiliwan.emall.mms.dao.UserExtraDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

@Service
public class UserExtraService extends DefaultBaseServiceImpl  {
	@Autowired
	private UserExtraDao userExtraDao ;

	/**
	 * 保存用户额外信息
	 * @param entity
	 */
	public void add(JsonObject resultObj ,UserExtra entity){
		addToResult(userExtraDao.insert(entity)>0,resultObj);
	}
	/**
	 * 更新用户额外信息
	 * @param entity
	 */
	public void update(JsonObject resultObj,UserExtra entity){
		addToResult(userExtraDao.update(entity)>0,resultObj);
	}
	
	/**
	 * 获取用户额外信息
	 * @param uid 用户ID
	 * @return 
	 */
	public void getExtraByUid(JsonObject resultObj,Integer uid){
		addToResult(userExtraDao.getEntityById(uid), resultObj);
	}

}
