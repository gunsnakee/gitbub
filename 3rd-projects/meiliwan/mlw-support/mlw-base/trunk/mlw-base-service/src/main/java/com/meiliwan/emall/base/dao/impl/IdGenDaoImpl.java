package com.meiliwan.emall.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.base.dao.IdGenDao;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.bean.BaseEntity;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class IdGenDaoImpl extends BaseDao<String,BaseEntity> implements IdGenDao{
	
	@Override
    public long nextPaySeq() {
        try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".selectPaySeq", getShardParam(null, null, true));
            return (Long) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".nextPaySeq:", e);
        }
    }

	@Override
	public long nextCmbSeq() {
		try {
            Object selectOne = getSqlSession().selectOne(getMapperNameSpace() + ".selectCmbSeq", getShardParam(null, null, true));
            return (Long) selectOne;
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".selectCmbSeq:", e);
        }
	}

	@Override
	public String getMapperNameSpace() {
		return IdGenDaoImpl.class.getName();
	}

}
