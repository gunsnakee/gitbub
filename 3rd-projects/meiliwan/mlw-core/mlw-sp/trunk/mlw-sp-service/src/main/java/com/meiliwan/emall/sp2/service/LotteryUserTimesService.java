package com.meiliwan.emall.sp2.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.icetool.JSONTool;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes;
import com.meiliwan.emall.sp2.dao.LotteryUserTimesDao;

@Service
public class LotteryUserTimesService extends DefaultBaseServiceImpl {

	@Autowired
	private LotteryUserTimesDao lotteryUserTimesDao;
	
	public void insertTimes(JsonObject resultObj, LotteryUserTimes times) {
		validate(times);
		LotteryUserTimes old = lotteryUserTimesDao.getEntityById(times.getUid());
		int count=0;
		if(old!=null){
			count+=old.getTimes();
			times.addTimes(count);
			lotteryUserTimesDao.update(times);
		}else{
			lotteryUserTimesDao.insert(times);
		}
    }

	private void validate(LotteryUserTimes times) {
		if(times==null){
			throw new ServiceException("LotteryUserTimes can not be null");
		}
		if(times.getUid()==null){
			throw new ServiceException("LotteryUserTimes'uid can not be null");
		}
		if(times.getSource()==null||times.getSource().length()>32){
			throw new ServiceException("LotteryUserTimes'Source is illegal");
		}
		if(times.getIp()==null||times.getIp().length()>64){
			throw new ServiceException("LotteryUserTimes'IP is illegal");
		}
		if(times.getTimes()==null||times.getTimes()<=0){
			throw new ServiceException("LotteryUserTimes'times is illegal");
		}
	}
	
	public void getTimes(JsonObject resultObj, int uid) {
		LotteryUserTimes old = lotteryUserTimesDao.getEntityById(uid);
		if(old==null){
			JSONTool.addToResult(0, resultObj);
		}else{
			JSONTool.addToResult(old.getTimes(), resultObj);
		}
    }
	
	public void minusTimes(JsonObject resultObj, int uid) {
		LotteryUserTimes old = lotteryUserTimesDao.getEntityById(uid);
		if(old==null||old.getTimes().intValue()==0){
			return ;
		}
		old.addTimes(-1);
		lotteryUserTimesDao.update(old);
	}
	
}
