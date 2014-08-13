package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.monitor.bean.MLWLog;
import com.meiliwan.emall.monitor.bean.MLWLogVO;
import com.meiliwan.emall.monitor.dao.MLWLogDao;

/**
 *
 */
@Service
public class MLWLogService  {
	
	@Autowired
	private MLWLogDao mLWLogDao;
	
	public List<String> getAllApplication(){
		List<String> list = mLWLogDao.getAllApplication();
		return list;
	}
	
	public List<MLWLog> getMLWLogListByUuid(String uuid){
		
		MLWLogVO dto = new MLWLogVO();
		dto.setErrorUuid(uuid);
		List<MLWLog> list = mLWLogDao.getListByMLWLogVO(dto);
		return list;
	}
	
}
