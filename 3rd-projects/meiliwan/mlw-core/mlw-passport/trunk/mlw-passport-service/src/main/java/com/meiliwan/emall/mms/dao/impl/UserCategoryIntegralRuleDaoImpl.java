package com.meiliwan.emall.mms.dao.impl;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserCategoryIntegralRule;
import com.meiliwan.emall.mms.dao.UserCategoryIntegralRuleDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserCategoryIntegralRuleDaoImpl extends BaseDao<Integer, UserCategoryIntegralRule> implements UserCategoryIntegralRuleDao {

	@Override
	public String getMapperNameSpace() {
		return UserCategoryIntegralRuleDao.class.getName();
	}

}
