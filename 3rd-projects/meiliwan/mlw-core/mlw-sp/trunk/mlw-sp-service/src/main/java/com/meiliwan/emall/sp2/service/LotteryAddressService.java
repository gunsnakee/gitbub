package com.meiliwan.emall.sp2.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.LotteryAddress;
import com.meiliwan.emall.sp2.bean.LotteryResult;
import com.meiliwan.emall.sp2.client.LotteryAddressClient;
import com.meiliwan.emall.sp2.dao.LotteryAddressDao;

@Service
public class LotteryAddressService extends DefaultBaseServiceImpl {

	@Autowired
	private LotteryAddressDao lotteryAddressDao;
	
	public void insert(JsonObject resultObj, LotteryAddress addr){
		validate(addr);
		lotteryAddressDao.insert(addr);
	}
	public void getById(JsonObject resultObj, int id){
		if(id<=0){
			throw new ServiceException("id is illegal");
		}
		LotteryAddress addr = lotteryAddressDao.getEntityById(id);
		JSONTool.addToResult(addr, resultObj);
	}

	private void validate(LotteryAddress addr) {
		if(addr==null){
			throw new ServiceException("LotteryAddress can not be null");
		}
		if(addr.getUid()==null||addr.getUid().intValue()<=0){
			throw new ServiceException("LotteryAddress's uid can not be null");
		}
		if(StringUtils.isEmpty(addr.getName())){
			throw new ServiceException("LotteryAddress's name can not be null");
		}
		if(StringUtils.isEmpty(addr.getPhone())){
			throw new ServiceException("LotteryAddress's Phone can not be null");
		}
		if(StringUtils.isEmpty(addr.getCity())){
			throw new ServiceException("LotteryAddress's City can not be null");
		}
		if(StringUtils.isEmpty(addr.getProvince())){
			throw new ServiceException("LotteryAddress's Province can not be null");
		}
		if(StringUtils.isEmpty(addr.getTown())){
			throw new ServiceException("LotteryAddress's Town can not be null");
		}
		if(StringUtils.isEmpty(addr.getAddress())){
			throw new ServiceException("LotteryAddress's Address can not be null");
		}
	}
	
	
}
