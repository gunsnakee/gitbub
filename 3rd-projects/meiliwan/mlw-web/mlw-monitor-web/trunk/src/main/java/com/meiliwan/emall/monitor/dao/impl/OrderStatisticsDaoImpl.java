package com.meiliwan.emall.monitor.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.OrderStatistics;
import com.meiliwan.emall.monitor.dao.OrderStatisticsDao;

@Repository
public class OrderStatisticsDaoImpl  extends BaseDao<Integer, OrderStatistics> implements OrderStatisticsDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return OrderStatisticsDao.class.getName();
	}

	
}
