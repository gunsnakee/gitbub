package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.allocation.PlayerApp;
import com.meiliwan.emall.monitor.allocation.PlayerAppDTO;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.dao.PlayerAppDao;
import com.meiliwan.emall.monitor.dao.PlayerModuleDao;

@Repository
public class PlayerAppDaoImpl  extends BaseDao<Integer, PlayerApp> implements PlayerAppDao {

	@Override
	public String getMapperNameSpace() {
		
		return PlayerAppDao.class.getName();
	}

	@Override
	public int deleteByPid(int pid) {
		// TODO Auto-generated method stub
		try {
            return getSqlSession().delete(getMapperNameSpace() + ".deleteByPid",
                    getShardParam(pid, pid, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByPid: {}", pid+"", e);
        }
	}

	@Override
	public List<PlayerAppDTO> getAllPlayerApp() {
		List<PlayerAppDTO> list = new ArrayList<PlayerAppDTO>();
		list = getSqlSession().selectList(getMapperNameSpace() + ".getAllPlayerApp",
                getShardParam( null,
                        getMapParams(null, null, null,null),false));
		return list;
	}

	
}
