package com.meiliwan.emall.mms.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserPassportPara;
import com.meiliwan.emall.mms.dao.UserSearchDao;
/**
 * 用户查询
 * @author jiawu.wu
 *
 */
@Repository
public class UserSearchDaoImpl extends BaseDao<Integer, UserPassportPara> implements UserSearchDao{

	@Override
	public String getMapperNameSpace() {
		return UserSearchDao.class.getName();
	}

}
