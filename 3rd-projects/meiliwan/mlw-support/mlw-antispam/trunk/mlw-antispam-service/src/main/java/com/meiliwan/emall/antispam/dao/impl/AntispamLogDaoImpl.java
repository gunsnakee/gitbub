package com.meiliwan.emall.antispam.dao.impl;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.antispam.bean.AntispamLog;
import com.meiliwan.emall.antispam.dao.AntispamLogDao;
import com.meiliwan.emall.core.db.BaseDao;

@Repository
public class AntispamLogDaoImpl extends BaseDao<Integer, AntispamLog> implements AntispamLogDao {

	@Override
	public String getMapperNameSpace() {
		return AntispamLogDao.class.getName();
	}

	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelective(AntispamLog record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AntispamLog selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(AntispamLog record) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateByPrimaryKey(AntispamLog record) {
		// TODO Auto-generated method stub
		return 0;
	}

}
