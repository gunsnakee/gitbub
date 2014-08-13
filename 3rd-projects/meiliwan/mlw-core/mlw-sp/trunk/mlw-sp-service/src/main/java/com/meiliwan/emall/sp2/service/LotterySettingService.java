package com.meiliwan.emall.sp2.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.LotterySetting;
import com.meiliwan.emall.sp2.dao.LotterySettingDao;

@Service
public class LotterySettingService extends DefaultBaseServiceImpl {

	@Autowired
	private LotterySettingDao lotterySettingDao;
	
	public void insertUpdate(JsonObject resultObj, LotterySetting setting){
		if(setting==null){
			throw new ServiceException("LotterySetting can not be null");
		}
		if(setting.getId()!=null&&setting.getId().intValue()>0){
			lotterySettingDao.update(setting);
		}else{
			setting.setCreateTime(new Date());
			lotterySettingDao.insert(setting);
		}
	}
	
	public void allList(JsonObject resultObj){
			
		List<LotterySetting> list = lotterySettingDao.getAllEntityObj();
		JSONTool.addToResult(list, resultObj);
	}
	
	public void del(JsonObject resultObj,int id){
		lotterySettingDao.delete(id);
	}
	
	public void updateMinusOneTotal(JsonObject resultObj,int id){
		LotterySetting setting = lotterySettingDao.getEntityById(id);
		int temp=0;
		if(setting!=null){
			temp=setting.getTotal();
		}
		if(setting!=null&&setting.getTotal()>0){
			setting.minusTotal();
			lotterySettingDao.update(setting);
		}
		//更新前值
		JSONTool.addToResult(temp, resultObj);
		
	}
	
	public void findById(JsonObject resultObj,int id){
		LotterySetting setting = lotterySettingDao.getEntityById(id);
		JSONTool.addToResult(setting, resultObj);
		
	}
}