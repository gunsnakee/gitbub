package com.meiliwan.emall.monitor.dao.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.bean.Log;
import com.meiliwan.emall.monitor.bean.LogVO;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.dao.LogDao;
import com.meiliwan.emall.monitor.dao.PlayerDao;

@Repository
public class PlayerDaoImpl  extends BaseDao<Integer, Player> implements PlayerDao {

	@Override
	public String getMapperNameSpace() {
		// TODO Auto-generated method stub
		return PlayerDao.class.getName();
	}

	
}
