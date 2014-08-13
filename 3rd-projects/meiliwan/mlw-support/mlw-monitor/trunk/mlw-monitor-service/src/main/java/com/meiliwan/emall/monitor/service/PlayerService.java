package com.meiliwan.emall.monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.dao.PlayerAppDao;
import com.meiliwan.emall.monitor.dao.PlayerDao;
import com.meiliwan.emall.monitor.dao.PlayerModuleDao;
import com.meiliwan.emall.monitor.dao.PlayerRequestDao;
import com.mlcs.core.ice.service.impl.DefaultBaseServiceImpl;

@Service
public class PlayerService extends DefaultBaseServiceImpl {
	
	@Autowired
	private PlayerDao playerDao;
	@Autowired
	private PlayerAppDao playerAppDao;
	@Autowired
	private PlayerModuleDao playerModuleDao;
	@Autowired
	private PlayerRequestDao playerRequestDao;
	
	public Player getPlayerById(int id){
		if(id<=0){
			throw new ServiceException("the id can not less than 0");
		}
		Player player = playerDao.getEntityById(id);
		return player;
	}
	
	public PagerControl<Player> getPagePlayer(PageInfo pageInfo){
		
		PagerControl<Player> pc = playerDao.getPagerByObj(null, pageInfo, null);
		return pc;
	}
	
	public void add(Player player){
		if(player==null){
			throw new ServiceException("the player can not be null");
		}
		if(!player.valid()){
			throw new ServiceException("the player is not valid");
		}
		player.setStateValid();
		playerDao.insert(player);
	}
	
	public void update(Player player){
		if(player==null){
			throw new ServiceException("the player can not be null");
		}
		if(player.pidIsNull()&&!player.valid()){
			throw new ServiceException("the player is not valid");
		}
		playerDao.update(player);
	}

	public void delete(int playerId){
		if(playerId<=0){
			throw new ServiceException("the player playerId less than 0");
		}
		playerDao.delete(playerId);
		playerAppDao.deleteByPid(playerId);
		playerModuleDao.deleteByPlayerId(playerId);
		playerRequestDao.delete(playerId);
	}
	
}
