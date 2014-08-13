package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserProExtraIntegral;
import com.meiliwan.emall.mms.dao.UserProExtraIntegralDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserProExtraIntegralDaoImpl extends BaseDao<Integer, UserProExtraIntegral> implements
		UserProExtraIntegralDao {

	@Override
	public String getMapperNameSpace() {
		return UserProExtraIntegralDao.class.getName();
	}

	

}
