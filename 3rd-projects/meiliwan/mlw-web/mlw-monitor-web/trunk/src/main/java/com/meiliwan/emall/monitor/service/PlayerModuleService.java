package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.allocation.PlayerModule;
import com.meiliwan.emall.monitor.allocation.PlayerModuleDTO;
import com.meiliwan.emall.monitor.allocation.PlayerModuleKey;
import com.meiliwan.emall.monitor.dao.PlayerModuleDao;

@Service
public class PlayerModuleService {
	
	@Autowired
	private PlayerModuleDao playerModuleDao;
	
	public void add(PlayerModule playerModule){
		if(playerModule==null){
			throw new ServiceException("the playerModule can not be null");
		}
		playerModuleDao.insert(playerModule);
	}

	public void delete( PlayerModuleKey playerModuleKey) {
		// TODO Auto-generated method stub
		if(playerModuleKey==null){
			throw new ServiceException("the playerModuleKey can not be null");
		}
		playerModuleDao.delete(playerModuleKey);
	}
	
	public List<PlayerModule> getByPlayerId( int playerId) {
		// TODO Auto-generated method stub
		if(playerId<=0){
			throw new ServiceException("the playerId can not less than 0");
		}
		PlayerModule pm = new PlayerModule();
		pm.setPid(playerId);
		List<PlayerModule> list = playerModuleDao.getListByObj(pm);
		return list;
	}
	
	
	public List<PlayerModuleDTO> getAllPlayerModule() {
		
		List<PlayerModuleDTO> list = playerModuleDao.getAllPlayerModule();
		for (int i = 0; i < list.size(); i++) {
			PlayerModuleDTO pm = list.get(i);
			if(StringUtils.isEmpty(pm.getEmail())){
				list.remove(i);
			}
			if(pm.getEmail()!=null){
				break;
			}
		}
		return list;
	}
}
