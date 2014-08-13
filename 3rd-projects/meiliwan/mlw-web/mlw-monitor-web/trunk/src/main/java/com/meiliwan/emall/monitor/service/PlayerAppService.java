package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonObject;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.allocation.PlayerApp;
import com.meiliwan.emall.monitor.allocation.PlayerAppDTO;
import com.meiliwan.emall.monitor.dao.PlayerAppDao;

@Service
public class PlayerAppService  {
	
	@Autowired
	private PlayerAppDao playerAppDao;
	
	public void add(JsonObject resultObj,PlayerApp playerApp){
		if(playerApp==null){
			throw new ServiceException("the player can not be null");
		}
		playerAppDao.insert(playerApp);
	}
	

	public void delete(JsonObject resultObj,int playerId){
		if(playerId<=0){
			throw new ServiceException("the player playerId less than 0");
		}
		playerAppDao.delete(playerId);
	}
	
	public List<PlayerApp> getListByPlayerId(int playerId){
		if(playerId<=0){
			throw new ServiceException("the player playerId less than 0");
		}
		PlayerApp app = new PlayerApp();
		app.setPid(playerId);
		List<PlayerApp> list = playerAppDao.getListByObj(app);
		return list;
	}
	
	public void addOrUpdate(int pid,String[] appNames){
		
		if(appNames==null){
			throw new ServiceException("param can not be null");
		}
		playerAppDao.deleteByPid(pid);
		for (String name : appNames) {
			PlayerApp app = new PlayerApp();
			app.setPid(pid);
			app.setAppName(name);
			playerAppDao.insert(app);
		}
	}
	
	public List<PlayerAppDTO> getAllPlayerApp(){
		
		List<PlayerAppDTO> list = playerAppDao.getAllPlayerApp();
		return list;
	}

	
}
