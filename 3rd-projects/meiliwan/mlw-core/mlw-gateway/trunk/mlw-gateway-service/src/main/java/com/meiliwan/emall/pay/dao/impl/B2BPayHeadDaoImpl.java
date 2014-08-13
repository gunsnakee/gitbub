package com.meiliwan.emall.pay.dao.impl;


import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.pay.bean.PayHead;
import com.meiliwan.emall.pay.dao.B2BPayHeadDao;

import org.springframework.stereotype.Repository;

@Repository
public class B2BPayHeadDaoImpl  extends BaseDao<String,PayHead> implements B2BPayHeadDao {

	@Override
	public String getMapperNameSpace() {
		return B2BPayHeadDaoImpl.class.getName();
	}

}
