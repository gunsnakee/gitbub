package com.meiliwan.emall.mms.service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.annotation.IceServiceMethod;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.dao.UserForeignDao;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

@Service
public class UserForeignService extends DefaultBaseServiceImpl  {
	
	private final static MLWLogger LOG = MLWLoggerFactory.getLogger(UserForeignService.class);

	@Autowired  
	private UserForeignDao userForeignDao ;
	
	/**
	 * 保存用户额外信息
	 * @param entity
	 */
	public void insert(JsonObject resultObj ,UserForeign entity){
		boolean boolarg = false; 
		
		try{
            UserForeign userForeign=userForeignDao.getEntityById(entity.getUid());
            if(userForeign ==null){
                userForeignDao.insert(entity);
            }
			boolarg = true;
		}catch(Exception e){
            throw new ServiceException("UserForeignService-insert: {}", entity.toString(), e);
		}
		addToResult(boolarg,resultObj);
	}
	/**
	 * 更新用户额外信息
	 * @param entity
	 */
	public void update(JsonObject resultObj,UserForeign entity){
		boolean boolarg = false; 
		try{
			userForeignDao.update(entity);
			boolarg = true;
		}catch(Exception e){
            throw new ServiceException("UserForeignService-update: {}", entity.toString(), e);
		}
		addToResult(boolarg,resultObj);
	}
	
	/**
	 * 
	 * @param resultObj
	 * @param newUid
	 * @param oldUid
	 * @param source
	 */
	public void update2NewUid(JsonObject resultObj, int newUid, int oldUid, String source){
		boolean result = userForeignDao.update2NewUid(newUid, oldUid, source);
		LOG.info("update to new uid", "newUid:" + newUid + ",oldUid:" + oldUid + ",source:" + source, null);
		addToResult(result,resultObj);
	}
	
	/**
	 * 获取用户额外信息
	 * @param uid 用户ID
	 * @return 
	 */
	public void getForeignByUid(JsonObject resultObj,Integer uid){
		UserForeign userForeign = new UserForeign();
		userForeign.setUid(uid);
		addToResult(userForeignDao.getEntityByObj(userForeign ), resultObj);
	}
	
	/**
	 * 
	 * @param resultObj 
	 * @param fid 第三方用户的唯一id
	 * @param source 第三方来源，有qq、sina等
	 */
	public void getForeignByFid(JsonObject resultObj, String fid, String source){
		UserForeign userForeign = new UserForeign();
		userForeign.setForeignUid(fid);
		userForeign.setSource(source);
		
		userForeign = userForeignDao.getEntityByObj(userForeign);
		
		addToResult(userForeign, resultObj);
	}
	

}
