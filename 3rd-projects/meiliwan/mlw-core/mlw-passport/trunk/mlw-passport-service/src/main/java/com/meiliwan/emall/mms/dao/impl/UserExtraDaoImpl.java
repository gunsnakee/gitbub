package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.commons.jedisTool.JedisKey;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserExtra;
import com.meiliwan.emall.mms.dao.UserExtraDao;

@Repository
public  class UserExtraDaoImpl extends BaseDao<Integer, UserExtra> implements UserExtraDao  {

	@Override
	public String getMapperNameSpace() {
		return UserExtraDao.class.getName();
	}

    @Override
    public JedisKey getUseJedisCacheKey() {
        return JedisKey.mms$extra;
    }
	
}