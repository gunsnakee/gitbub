package com.meiliwan.emall.sp2.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.sp2.bean.LotterySetting;
import com.meiliwan.emall.sp2.dao.LotterySettingDao;

@Repository
public class LotterySettingDaoImpl extends BaseDao<Integer, LotterySetting> implements LotterySettingDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return LotterySettingDao.class.getName();
	}

}
