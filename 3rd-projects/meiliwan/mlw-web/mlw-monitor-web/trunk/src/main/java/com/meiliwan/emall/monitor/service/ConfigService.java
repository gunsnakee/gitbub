package com.meiliwan.emall.monitor.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meiliwan.emall.commons.PageInfo;
import com.meiliwan.emall.commons.PagerControl;
import com.meiliwan.emall.commons.exception.ServiceException;
import com.meiliwan.emall.monitor.bean.Config;
import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.dao.ConfigDao;

@Service
public class ConfigService {
	
	@Autowired
	private ConfigDao configDao;
	
	public Config getConfigById(int id){
		if(id<=0){
			throw new ServiceException("the id can not less than 0");
		}
		Config player = configDao.getEntityById(id);
		return player;
	}
	
	public Config getConfigByCode(String code,String type){
		Config conf= new Config();
		conf.setCode(code);
		conf.setType(type);
		List<Config> list = configDao.getListByObj(conf);
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	public PagerControl<Config> getPagePlayer(PageInfo pageInfo){
		
		PagerControl<Config> pc = configDao.getPagerByObj(null, pageInfo, null);
		return pc;
	}
	
	public void add(Config player){
		if(player==null){
			throw new ServiceException("the config can not be null");
		}
		configDao.insert(player);
	}
	
	public void update(Config player){
		if(player==null){
			throw new ServiceException("the config can not be null");
		}
		configDao.update(player);
	}

	public void delete(int playerId){
		if(playerId<=0){
			throw new ServiceException("the config playerId less than 0");
		}
		configDao.delete(playerId);
	}
	
}
