package com.meiliwan.emall.monitor.client;

import java.util.LinkedHashMap;

import com.meiliwan.emall.commons.exception.BaseException;
import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.commons.util.ConfigOnZk;



public class AllocationClient {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(AllocationClient.class);
	
	public static final String DAO ="dao";
	public static final String ICES ="iceS";
	public static final String ICEC ="iceC";
	public static final String INTR ="intr";
	
	private static LinkedHashMap<String,Integer> resumeTimeConfigList = new LinkedHashMap<String,Integer>();
	private static LinkedHashMap<String,Integer> intoDBTimeConfigList = new LinkedHashMap<String,Integer>();
	static {
		try {
			ConfigOnZk configOnZk  = ConfigOnZk.getInstance();
			String dao = configOnZk.getValue("monitor-web/system.properties", "dao.resumeTime");
			String iceS = configOnZk.getValue("monitor-web/system.properties", "iceS.resumeTime");
			String iceC = configOnZk.getValue("monitor-web/system.properties", "iceC.resumeTime");
			String intr = configOnZk.getValue("monitor-web/system.properties", "intr.resumeTime");
			resumeTimeConfigList.put(DAO, Integer.parseInt(dao));
			resumeTimeConfigList.put(ICES, Integer.parseInt(iceS));
			resumeTimeConfigList.put(ICEC, Integer.parseInt(iceC));
			resumeTimeConfigList.put(INTR, Integer.parseInt(intr));
		} catch (BaseException e) {
			logger.error(e, resumeTimeConfigList, null);
			resumeTimeConfigList.put(DAO, 200);
			resumeTimeConfigList.put(ICES, 200);
			resumeTimeConfigList.put(ICEC, 200);
			resumeTimeConfigList.put(INTR, 200);
		}
		
		try {
			ConfigOnZk  configOnZk  = ConfigOnZk.getInstance();
			String dao = configOnZk.getValue("monitor-web/system.properties", "dao.db.time");
			String iceS = configOnZk.getValue("monitor-web/system.properties", "iceS.db.time");
			String iceC = configOnZk.getValue("monitor-web/system.properties", "iceC.db.time");
			String intr = configOnZk.getValue("monitor-web/system.properties", "intr.db.time");
			intoDBTimeConfigList.put(DAO, Integer.parseInt(dao));
			intoDBTimeConfigList.put(ICES, Integer.parseInt(iceS));
			intoDBTimeConfigList.put(ICEC, Integer.parseInt(iceC));
			intoDBTimeConfigList.put(INTR, Integer.parseInt(intr));
		} catch (BaseException e) {
			logger.error(e, intoDBTimeConfigList, null);
			intoDBTimeConfigList.put(DAO, 5);
			intoDBTimeConfigList.put(ICES, 10);
			intoDBTimeConfigList.put(ICEC, 10);
			intoDBTimeConfigList.put(INTR, 10);
		}
		
	}
	
	public static LinkedHashMap<String, Integer> getResumeTimeConfigList(){
		return resumeTimeConfigList;
	}
	
	public static LinkedHashMap<String, Integer> getIntoDBTimeConfigList(){
		return intoDBTimeConfigList;
	}
	
}
