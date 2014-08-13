package com.meiliwan.emall.imeiliwan.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.imeiliwan.bean.WeixinOrder;
import com.meiliwan.emall.imeiliwan.dao.WeixinOrderDao;
import org.springframework.stereotype.Repository;

@Repository
public class WeixinOrderDaoImpl extends BaseDao<Integer, WeixinOrder> implements WeixinOrderDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return WeixinOrderDao.class.getName();
	}

	
}
