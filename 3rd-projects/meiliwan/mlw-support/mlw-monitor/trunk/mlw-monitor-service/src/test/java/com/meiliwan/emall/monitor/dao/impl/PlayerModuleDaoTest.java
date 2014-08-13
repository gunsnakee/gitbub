package com.meiliwan.emall.monitor.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import com.meiliwan.emall.bkstage.MonitorBaseTest;
import com.meiliwan.emall.monitor.dao.PlayerModuleDao;

public class PlayerModuleDaoTest extends MonitorBaseTest{

	@Autowired
	private PlayerModuleDao playerModuleDao;
	
	@Test
	public void deleteByPlayerId(){
		playerModuleDao.deleteByPlayerId(1);
		
	}
	
	
}
