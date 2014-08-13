package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.RequestExcludeSetting;
import com.meiliwan.emall.monitor.dao.RequestExcludeSettingDao;
import com.mlcs.core.ice.icetool.JSONTool;
import com.mlcs.core.ice.service.impl.DefaultBaseServiceImpl;

@Service
public class RequestExcludeSettingService extends DefaultBaseServiceImpl {
	
	@Autowired
	private RequestExcludeSettingDao requestExcludeSettingDao;
	
	public List<RequestExcludeSetting> getAll(){
		
		List<RequestExcludeSetting> all = requestExcludeSettingDao.getAllEntityObj();
		return all;
	}
	
	public void getRequestSettingById(JsonObject resultObj,int id){
		if(id<=0){
			throw new ServiceException("the id can not less than 0");
		}
		RequestExcludeSetting requestSetting = requestExcludeSettingDao.getEntityById(id);
		JSONTool.addToResult(requestSetting, resultObj);
	}
	
	public PagerControl<RequestExcludeSetting> getPageRequestExcludeSetting(PageInfo pageInfo){
		
		PagerControl<RequestExcludeSetting> pc = requestExcludeSettingDao.getPagerByObj(null, pageInfo, null);
		return pc;
	}
	
	public void add(RequestExcludeSetting requestExcludeSetting){
		if(requestExcludeSetting==null){
			throw new ServiceException("the requestSetting can not be null");
		}
		if(!requestExcludeSetting.valid()){
			throw new ServiceException("the requestSetting do not valid");
		}
		requestExcludeSettingDao.insert(requestExcludeSetting);
	}
	
	public void update(JsonObject resultObj,RequestExcludeSetting requestExcludeSetting){
		if(requestExcludeSetting==null){
			throw new ServiceException("the RequestExcludeSetting can not be null");
		}
		
		requestExcludeSettingDao.update(requestExcludeSetting);
	}

	public void delete(int id){
		if(id<=0){
			throw new ServiceException("the RequestExcludeSetting id less than 0");
		}
		requestExcludeSettingDao.delete(id);
	}
	
}
