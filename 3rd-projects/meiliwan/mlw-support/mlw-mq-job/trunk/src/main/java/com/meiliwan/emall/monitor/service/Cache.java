package com.meiliwan.emall.monitor.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.meiliwan.emall.commons.log.MLWLogger;
import com.meiliwan.emall.commons.log.MLWLoggerFactory;
import com.meiliwan.emall.log.msgrcv.WatchLogTaskWorker;
import com.meiliwan.emall.monitor.bean.RequestLog;
/**
 *  监控缓存类，定时任务为清理缓存纪录
 * @author rubi
 *
 */
public class Cache {

	private static final MLWLogger logger = MLWLoggerFactory.getLogger(Cache.class);
	
	private static  int BUFFER_SIZE =10; //1级缓冲区大小
	private static Cache cache = new Cache();
	private MonitorBuffer monitorBuffer = new MonitorBuffer();
	
	
	/** 1级缓存日志 */
	private List<RequestLog> bufferLog = new CopyOnWriteArrayList<RequestLog>();
	
	
	public static Cache getInstance(){
		if(cache!=null){
			return cache;
		}
		return new Cache();
	}
	/**
	 * 设置缓存大小
	 */
	private Cache() {
		
	}
	
	//增加
	public synchronized void add(RequestLog log){
		if(log.isDAOType()&&log.getTimeConsume().longValue()<=WatchLogTaskWorker.DAO_NOT_INSERT){
			return ;
		}
		if(!log.isDAOType()&&log.getTimeConsume().longValue()<=WatchLogTaskWorker.OTHER_NOT_INSERT){
			return ;
		}
		
		bufferLog.add(log);
		if(bufferLog.size()>BUFFER_SIZE){
			monitorBuffer.add(bufferLog);
			bufferLog = new CopyOnWriteArrayList<RequestLog>();
		}
		
	}

	public synchronized void addToMonitorBuffer(){
		//logger.debug("addToMonitorBuffer "+bufferLog.size());
		if(!bufferLog.isEmpty()){
			monitorBuffer.add(bufferLog);
		}
	}

	
	
}
