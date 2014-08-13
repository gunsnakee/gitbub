package com.meiliwan.emall.monitor.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.Config;
import com.meiliwan.emall.monitor.dao.ConfigDao;

@Repository
public class ConfigDaoImpl  extends BaseDao<Integer, Config> implements ConfigDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return ConfigDao.class.getName();
	}

	
}
