package com.meiliwan.emall.sp2.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.LotteryAddress;
import com.meiliwan.emall.sp2.dao.LotteryAddressDao;

@Repository
public class LotteryAddressDaoImpl  extends BaseDao<Integer, LotteryAddress> implements LotteryAddressDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return LotteryAddressDao.class.getName();
	}

}
