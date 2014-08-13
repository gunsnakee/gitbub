package com.meiliwan.emall.dao.union.impl;


import com.meiliwan.emall.bean.union.UnionOrder;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.dao.union.UnionOrderDao;

public class UnionOrderDaoImpl extends BaseDao<Integer, UnionOrder> implements UnionOrderDao {

	@Override
	public String getMapperNameSpace() {
		return UnionOrderDao.class.getName();
	}
	
}
