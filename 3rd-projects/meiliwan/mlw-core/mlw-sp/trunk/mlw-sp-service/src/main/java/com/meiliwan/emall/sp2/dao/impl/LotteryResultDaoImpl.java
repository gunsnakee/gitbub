package com.meiliwan.emall.sp2.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.LotteryResult;
import com.meiliwan.emall.sp2.dao.LotteryResultDao;

@Repository
public class LotteryResultDaoImpl  extends BaseDao<Integer, LotteryResult> implements LotteryResultDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return LotteryResultDao.class.getName();
	}

}
