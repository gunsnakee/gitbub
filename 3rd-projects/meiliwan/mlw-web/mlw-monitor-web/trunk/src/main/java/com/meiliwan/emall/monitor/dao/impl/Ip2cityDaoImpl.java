package com.meiliwan.emall.monitor.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.Ip2city;
import com.meiliwan.emall.monitor.dao.Ip2cityDao;

@Repository
public class Ip2cityDaoImpl  extends BaseDao<Integer, Ip2city> implements Ip2cityDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return Ip2cityDao.class.getName();
	}

	
}
