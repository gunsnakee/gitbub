package com.meiliwan.emall.mms.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserForeign;
import com.meiliwan.emall.mms.dao.UserForeignDao;

@Repository
public class UserForeignDaoImpl extends BaseDao<Integer, UserForeign> implements
		UserForeignDao {

	@Override
	public String getMapperNameSpace() {
		return UserForeignDao.class.getName();
	}

	@Override
	public boolean update2NewUid(int newUid, int oldUid, String source) {
		int result = 0;
		try {
			Map<String, Object> values = new HashMap<String, Object>();
			values.put("newUid", newUid);
			values.put("oldUid", oldUid);
			values.put("source", source);

			result = getSqlSession().update(
					getMapperNameSpace() + ".update2NewUid",
					getShardParam(oldUid > 0 ? oldUid : null, values, true));
		} catch (Exception e) {
			throw new ServiceException("service-" + getMapperNameSpace()
					+ ".updateEmailActive: {}", "newUid:" + newUid + ",oldUid:"
					+ oldUid + ",source:" + source, e);
		}

		return result > 0;
	}

}