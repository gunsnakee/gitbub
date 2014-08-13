package com.meiliwan.emall.base.service;

import static com.meiliwan.emall.icetool.JSONTool.addToResult;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.base.dao.BaseStationFareDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.commons.util.NumberUtil;
import com.meiliwan.emall.service.impl.DefaultBaseServiceImpl;

@Service
public class BaseStationFareService extends DefaultBaseServiceImpl {

	@Autowired
	private BaseStationFareDao baseStationFareDao;
	
	public void findById(JsonObject resultObj) {
		BaseStationFare fare = baseStationFareDao.getEntityById(BaseStationFare.DEFAULT_ID);
		addToResult(fare ,resultObj);
	}

	public void update(JsonObject resultObj, BaseStationFare fare) {
		if (fare == null) {
			throw new ServiceException("the param can not be null");
		}
		if(StringUtils.isEmpty(fare.getType())){
			throw new ServiceException("the type can not be null");
		}
		baseStationFareDao.update(fare);
	}

	@PostConstruct
	public void init() {
		System.out.println("BaseStationFareService init");
		BaseStationFare bean = baseStationFareDao.getEntityById(BaseStationFare.DEFAULT_ID);
		if(bean==null){
			bean = new BaseStationFare();
			bean.setType(BaseStationFare.fixed);
			bean.setFixedPrice(new BigDecimal(10.00));
			baseStationFareDao.insert(bean);
		}
	}
	
	public void getPrice(JsonObject resultObj,double totalAmout) {
		BaseStationFare fare = baseStationFareDao.getEntityById(BaseStationFare.DEFAULT_ID);
		if(fare.isFull()){
			//如果两个数一样则返回0，如果第一个数比第二个数大则返回1，反之返回-1
			int result = NumberUtil.compareTo(fare.getFullFreeLimit().doubleValue(), totalAmout);
			if(result<=0){
				addToResult(new BigDecimal(0) ,resultObj);
				return ;
			}else{
				addToResult(fare.getNotFullFreePrice() ,resultObj);
				return ;
			}
			
		}
		if(fare.isFixed()){
			addToResult(fare.getFixedPrice() ,resultObj);
			return ;
		}
		if(fare.isFree()){
			addToResult(new BigDecimal(0) ,resultObj);
			return ; 
		}
	}
	
	
}
