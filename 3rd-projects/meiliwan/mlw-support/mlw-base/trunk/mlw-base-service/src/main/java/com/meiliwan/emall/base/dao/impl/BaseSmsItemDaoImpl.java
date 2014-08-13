package com.meiliwan.emall.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.bean.BaseSmsItem;
import com.meiliwan.emall.base.dao.BaseSmsItemDao;
import com.meiliwan.emall.core.db.BaseDao;
@Repository
public class BaseSmsItemDaoImpl extends BaseDao<Integer, BaseSmsItem> implements BaseSmsItemDao {

	@Override
	public String getMapperNameSpace() {
		return BaseSmsItemDao.class.getName();
	}

}
