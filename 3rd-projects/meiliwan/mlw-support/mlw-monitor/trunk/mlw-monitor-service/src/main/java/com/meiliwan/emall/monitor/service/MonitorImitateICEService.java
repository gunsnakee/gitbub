package com.meiliwan.emall.monitor.service;

import java.util.List;

import com.meiliwan.emall.monitor.bean.Player;
import com.meiliwan.emall.monitor.bean.RequestExcludeSetting;
import com.meiliwan.emall.monitor.dao.impl.MonitorImitateICEDaoImpl;

/**
 * 直接对外接口
 * @author rubi
 *
 */
public class MonitorImitateICEService {

	public static MonitorImitateICEService service = new MonitorImitateICEService();
	private MonitorImitateICEService(){}
	public static MonitorImitateICEService getInstance(){
		return service;
	}
	private static MonitorImitateICEDaoImpl monitorImitateICEDao=new MonitorImitateICEDaoImpl();
	
	public List<Player> getAllPlayer(){
		return monitorImitateICEDao.getAllPlayer();
	}
	
	public static List<RequestExcludeSetting> getAllRequestExcludeSetting(){
		return monitorImitateICEDao.getAllRequestExcludeSetting();
	}
	
	public void testLogger(){
		 monitorImitateICEDao.testLogger();
	}
	
	
}
