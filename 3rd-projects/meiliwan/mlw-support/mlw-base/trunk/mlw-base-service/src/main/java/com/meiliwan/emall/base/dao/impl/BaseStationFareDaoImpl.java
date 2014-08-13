package com.meiliwan.emall.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.bean.BaseStationFare;
import com.meiliwan.emall.base.dao.BaseStationFareDao;
import com.meiliwan.emall.commons.jedisTool.JedisKey;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class BaseStationFareDaoImpl  extends BaseDao<Integer,BaseStationFare> implements BaseStationFareDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return BaseStationFareDao.class.getName();
	}
	
	@Override
	public JedisKey getUseJedisCacheKey() {
		// TODO Auto-generated method stub
		return JedisKey.base$stationFare;
	}
	 
}