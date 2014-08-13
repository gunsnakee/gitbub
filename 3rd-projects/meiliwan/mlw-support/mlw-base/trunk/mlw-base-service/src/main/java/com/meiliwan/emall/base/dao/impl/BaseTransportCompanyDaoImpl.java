package com.meiliwan.emall.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.bean.BaseTransportCompany;
import com.meiliwan.emall.base.dao.BaseTransportCompanyDao;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class BaseTransportCompanyDaoImpl extends BaseDao<Integer,BaseTransportCompany>
		implements BaseTransportCompanyDao{

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return BaseTransportCompanyDao.class.getName();
	}
}