package com.meiliwan.emall.union.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.mms.bean.UserPassport;
import com.meiliwan.emall.union.dao.UnionUserDao;

public class UnionUserDaoImpl extends BaseDao<Integer, UserPassport> implements UnionUserDao {
	
	@Override
	public String getMapperNameSpace(){
		return UnionUserDao.class.getName();
	}

	@Override
	public List<UserPassport> getUserList(String emailList) {
		// TODO Auto-generated method stub
		String method = ".getUserList";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("emallList", emailList);
		return getSqlSession().selectList(getMapperNameSpace()+method, getShardParam(null, map, true));
	}

}
