package com.meiliwan.emall.monitor.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.core.db.BaseDao;
import com.meiliwan.emall.monitor.allocation.PlayerModule;
import com.meiliwan.emall.monitor.allocation.PlayerModuleDTO;
import com.meiliwan.emall.monitor.allocation.PlayerModuleKey;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.dao.PlayerModuleDao;

@Repository
public class PlayerModuleDaoImpl  extends BaseDao<PlayerModuleKey, PlayerModule> implements PlayerModuleDao {

	@Override
	public String getMapperNameSpace() {
		
		return PlayerModuleDao.class.getName();
	}

	@Override
	public List<PlayerModuleDTO> getAllPlayerModule() {
		List<PlayerModuleDTO> list = new ArrayList<PlayerModuleDTO>();
		list = getSqlSession().selectList(getMapperNameSpace() + ".getAllPlayerModule",
                getShardParam( null,getMapParams(null, null, null,null),false));
		return list;
	}

	@Override
	public int deleteByPlayerId(int pid) {
		// TODO Auto-generated method stub
		try {
            return getSqlSession().delete(getMapperNameSpace() + ".deleteByPlayerId",
                    getShardParam(pid, pid, true));
        } catch (Exception e) {
            throw new ServiceException("service-" + getMapperNameSpace() + ".deleteByPlayerId: {}", pid+"", e);
        }
	}
	
	
}
