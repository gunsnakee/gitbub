package com.meiliwan.emall.oms.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.oms.bean.OrdTransport;
import com.meiliwan.emall.oms.dao.OrdTransportDao;

@Repository
public class OrdTransportDaoImpl extends BaseDao<Integer,OrdTransport> implements OrdTransportDao{

	 @Override
	    public String getMapperNameSpace() {
	        return OrdTransportDao.class.getName();
	    }
}
