package com.meiliwan.emall.sp2.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.LotteryUserTimes;
import com.meiliwan.emall.sp2.dao.LotteryUserTimesDao;

@Repository
public class LotteryUserTimesDaoImpl extends BaseDao<Integer, LotteryUserTimes> implements LotteryUserTimesDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return LotteryUserTimesDao.class.getName();
	}

}
